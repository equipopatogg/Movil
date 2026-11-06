package com.pactogg.app.feature.connected.presentation



import androidx.lifecycle.ViewModelStore
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.domain.*
import com.pactogg.app.feature.connected.domain.*
import com.pactogg.app.testing.UnitChecks
import java.lang.reflect.Proxy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import org.junit.experimental.categories.Category

@OptIn(ExperimentalCoroutinesApi::class)
@Category(UnitChecks::class)
class ConnectedFlowTest {
    private val dispatcher = StandardTestDispatcher()
    private val lifecycle = ViewModelStore()
    private val handlers = mutableMapOf<String, (Array<out Any?>) -> Any?>()
    private val calls = mutableListOf<String>()
    private lateinit var model: ConnectedViewModel
    private lateinit var sessions: SessionManager
    private val account = Account("111111111111111111111111", "qa@example.com", "Player")
    private val group = Group("222222222222222222222222", "Equipo", "ABCDEF", "member", 100, listOf(25))
    private val contract = Challenge("333333333333333333333333", group.id, "Halo", "Win", "Reward",
        "2099-01-01T00:00:00Z", 25, "open", "444444444444444444444444", "member", account.id, version = 1)
    private inline fun <reified T> repository(): T = Proxy.newProxyInstance(T::class.java.classLoader,
        arrayOf(T::class.java)) { _, method, arguments ->
        calls += method.name
        try {
            handlers[method.name]?.invoke(arguments ?: emptyArray()) ?: error("Unexpected ${method.name}")
        } catch (error: Exception) {
            // Deliver suspend failures as Kotlin does, without Java proxy exception wrapping.
            @Suppress("UNCHECKED_CAST")
            val continuation = arguments?.lastOrNull() as? kotlin.coroutines.Continuation<Any?> ?: throw error
            continuation.resumeWith(Result.failure(error))
            kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
        }
    } as T

    @Before fun setup() = runTest(dispatcher) {
        Dispatchers.setMain(dispatcher)
        val session = Session(account, "synthetic-access", "synthetic-refresh", Long.MAX_VALUE)
        val store = object : SessionStore {
            override fun read() = session
            override fun write(session: Session) = Unit
            override fun clear() = Unit
        }
        val api = repository<PactoApi>()
        sessions = SessionManager(api, store)
        sessions.restore()
        model = ConnectedViewModel(sessions, repository<ProductRepository>(), repository<SocialRepository>())
        lifecycle.put("test", model)
        model.state.first { !it.restoring && it.account != null }
        model.mutable.value = ConnectedState(restoring = false, account = account, group = group, contract = contract)
    }
    @After fun cleanup() { lifecycle.clear(); Dispatchers.resetMain() }

    private suspend fun switchAccount() {
        val user = com.pactogg.app.feature.auth.data.UserDto("555555555555555555555555", "second@example.com", "Second")
        handlers["login"] = { retrofit2.Response.success(com.pactogg.app.core.network.Envelope(
            com.pactogg.app.feature.auth.data.SessionDto(user, "second-access", "second-refresh", "Bearer", 900))) }
        sessions.login(com.pactogg.app.feature.auth.data.LoginRequest("second@example.com", "synthetic"))
        model.state.first { it.account?.id == user.id }
    }

    @Test fun changingAccountClearsGroupDraftFiltersMessagesAndCredentials() = runTest(dispatcher) {
        model.mutable.value = model.mutable.value.copy(route = ConnectedRoute.CONTRACT,
            draft = ChallengeDraft(game = "previous game"), link = "https://youtu.be/previous",
            error = "old error", notice = "old notice",
            access = com.pactogg.app.feature.auth.presentation.AccessState(password = "old synthetic phrase"),
            social = SocialState(joinCode = "ABCDEF", pendingAction = "accept", groupQuery = "previous group"))
        switchAccount()
        val state = model.state.value
        assertEquals("555555555555555555555555", state.account?.id)
        assertEquals(ConnectedRoute.HOME, state.route)
        assertNull(state.group); assertNull(state.contract); assertNull(state.evidence)
        assertEquals(ChallengeDraft(), state.draft); assertEquals(SocialState(), state.social)
        assertEquals("", state.access.password); assertEquals("", state.link)
        assertNull(state.error); assertNull(state.notice)
    }

    @Test fun delayedErrorFromPreviousAccountCannotAppearInNextAccount() = runTest(dispatcher) {
        val finish = CompletableDeferred<Unit>()
        model.perform { finish.await(); throw ApiFailure(403, "NOT_GROUP_MEMBER", "old-request") }
        runCurrent(); assertTrue(model.state.value.busy)
        switchAccount()
        finish.complete(Unit); runCurrent()
        assertEquals("555555555555555555555555", model.state.value.account?.id)
        assertNull(model.state.value.error); assertNull(model.state.value.diagnostic)
        assertFalse(model.state.value.busy)
    }

    @Test fun invalidFormsNeverCallRepositories() = runTest(dispatcher) {
        runCurrent()
        model.createGroup(); model.joinCode("bad"); model.joinGroup(); model.saveProfile()
        model.mutable.value = model.mutable.value.copy(link = "http://example.invalid")
        model.sendLink(); runCurrent()
        assertTrue(calls.isEmpty()); assertNotNull(model.state.value.error)
    }
    @Test fun groupCreationResetsDependentDataOnlyAfterSuccess() = runTest(dispatcher) {
        runCurrent(); handlers["createGroup"] = { group }
        model.mutable.value = model.mutable.value.copy(groupName = "Equipo", reviewing = true)
        model.createGroup(); assertTrue(model.state.value.busy); runCurrent()
        assertEquals(group, model.state.value.group); assertNull(model.state.value.contract)
        assertFalse(model.state.value.reviewing); assertEquals("", model.state.value.groupName)
        assertFalse(model.state.value.busy); assertNotNull(model.state.value.notice)
    }
    @Test fun failedWriteRetainsInputAndNeverRetries() = runTest(dispatcher) {
        runCurrent(); handlers["createGroup"] = { throw ApiFailure(503, "STORAGE_UNAVAILABLE", "safe-id") }
        model.mutable.value = model.mutable.value.copy(groupName = "Equipo")
        model.createGroup(); model.createGroup(); runCurrent()
        assertEquals(listOf("createGroup"), calls); assertEquals("Equipo", model.state.value.groupName)
        assertFalse(model.state.value.busy); assertNotNull(model.state.value.error)
    }
    @Test fun apiFailureDiagnosticIsSanitizedAndBusyAlwaysClears() = runTest(dispatcher) {
        runCurrent()
        model.perform { throw ApiFailure(503, "STORAGE_UNAVAILABLE", "safe-id") }
        runCurrent()
        assertFalse(model.state.value.busy); assertEquals(503, model.state.value.diagnostic?.status)
        assertEquals("safe-id", model.state.value.diagnostic?.requestId)
    }
    @Test fun joinClearsInviteOnlyAfterProviderConfirmation() = runTest(dispatcher) {
        runCurrent(); handlers["join"] = { assertEquals("ABCDEF", it[0]); group }
        model.joinCode("ABCDEF"); model.joinGroup(); runCurrent()
        assertEquals("", model.state.value.social.joinCode); assertEquals(group, model.state.value.group)
        assertNotNull(model.state.value.notice)
    }
    @Test fun deniedJoinPreservesInviteAndAccount() = runTest(dispatcher) {
        runCurrent(); handlers["join"] = { throw ApiFailure(403, "NOT_GROUP_MEMBER") }
        model.joinCode("ABCDEF"); model.joinGroup(); runCurrent()
        assertEquals("ABCDEF", model.state.value.social.joinCode)
        assertEquals(account, model.state.value.account); assertNotNull(model.state.value.error)
    }
    @Test fun profileReadAndUpdateUseServerIdentity() = runTest(dispatcher) {
        runCurrent()
        val profile = Profile(account.copy(nickname = "Renamed"), "default", false, 1, 0, 1, 0, 90, 1)
        handlers["profile"] = { profile }; handlers["updateProfile"] = { profile }
        model.loadProfile(); runCurrent(); assertEquals("Renamed", model.state.value.social.editedNickname)
        model.saveProfile(); runCurrent(); assertEquals(profile.account, model.state.value.account)
        assertEquals(profile, model.state.value.social.profile)
    }
    @Test fun paginationAppendsWithoutDuplicatingAndStopsAtEnd() = runTest(dispatcher) {
        runCurrent(); var page = 0
        val second = group.copy(id = "555555555555555555555555")
        handlers["groups"] = { args ->
            if (page++ == 0) { assertNull(args[1]); Page(listOf(group), true, "next") }
            else { assertEquals("next", args[1]); Page(listOf(group, second), false, null) }
        }
        model.loadGroups(); runCurrent(); model.loadGroups(true); runCurrent(); model.loadGroups(true)
        assertEquals(listOf(group, second), model.state.value.social.groups.items)
        assertEquals(2, calls.count { it == "groups" })
        model.groupSearch("new"); assertFalse(model.state.value.social.groups.loaded)
        assertTrue(model.state.value.social.groups.items.isEmpty())
    }
    @Test fun membersBoardAndFiltersDoNotMixPages() = runTest(dispatcher) {
        runCurrent(); val member = Member(account.id, "Player", "default", "member", 100, "2026-01-01")
        handlers["members"] = { Page(listOf(member), false, null) }
        handlers["contracts"] = { Page(listOf(contract), false, null) }
        model.loadMembers(); runCurrent(); model.loadBoard(); runCurrent()
        assertEquals(member, model.state.value.social.members.items.single())
        assertEquals(contract, model.state.value.social.contracts.items.single())
        model.memberSearch("other"); model.boardFilter(BoardFilter(status = "cancelled"))
        assertFalse(model.state.value.social.members.loaded); assertFalse(model.state.value.social.contracts.loaded)
    }
    @Test fun anotherGroupClearsOldContractAndRival() = runTest(dispatcher) {
        runCurrent(); val other = group.copy(id = "555555555555555555555555")
        handlers["group"] = { other }; model.selectGroup(other.id); runCurrent()
        assertEquals(other, model.state.value.group); assertNull(model.state.value.contract)
        assertNull(model.state.value.social.selectedRival); assertFalse(model.state.value.social.members.loaded)
    }
    @Test fun rivalMustBeVisibleAndCannotBeSelf() = runTest(dispatcher) {
        runCurrent(); val rival = Member("444444444444444444444444", "Rival", "default", "member", 100, "date")
        model.mutable.value = model.mutable.value.copy(social = SocialState(members = ListState(listOf(rival))))
        model.targetMember(account.id); assertNull(model.state.value.draft.targetId)
        model.targetMember("unknown"); assertNull(model.state.value.draft.targetId)
        model.targetMember(rival.id); assertEquals(rival, model.state.value.social.selectedRival)
        model.targetMember(null); assertNull(model.state.value.social.selectedRival)
    }
    @Test fun challengeCreationRequiresReviewAndResetsDraftAfterConfirmation() = runTest(dispatcher) {
        runCurrent(); handlers["createChallenge"] = { contract }
        model.createChallenge(); assertTrue(calls.isEmpty())
        model.mutable.value = model.mutable.value.copy(reviewing = true, draft = ChallengeDraft(game = "Halo"))
        model.createChallenge(); runCurrent()
        assertEquals(ConnectedRoute.CONTRACT, model.state.value.route); assertEquals(contract, model.state.value.contract)
        assertEquals(ChallengeDraft(), model.state.value.draft); assertFalse(model.state.value.reviewing)
    }
    @Test fun acceptRefreshesThenRequiresExplicitConfirmationAndServerBalance() = runTest(dispatcher) {
        runCurrent(); handlers["challenge"] = { contract }; handlers["group"] = { group }
        handlers["accept"] = { contract.copy(status = "in_progress", myPoints = 75) }
        model.requestContractAction("accept"); runCurrent()
        assertEquals("accept", model.state.value.social.pendingAction)
        assertFalse(calls.contains("accept")); model.confirmContractAction(); runCurrent()
        assertEquals(75, model.state.value.group?.points); assertFalse(model.state.value.contractStateStale)
        assertNull(model.state.value.social.pendingAction); assertEquals(1, calls.count { it == "accept" })
    }
    @Test fun uncertainAcceptDisablesReplayUntilFreshDetail() = runTest(dispatcher) {
        runCurrent(); handlers["accept"] = { throw java.io.IOException("synthetic") }
        model.mutable.value = model.mutable.value.copy(social = SocialState(pendingAction = "accept"))
        model.confirmContractAction(); runCurrent(); model.confirmContractAction(); runCurrent()
        assertEquals(1, calls.count { it == "accept" }); assertTrue(model.state.value.contractStateStale)
        assertEquals(100, model.state.value.group?.points); assertNull(model.state.value.social.pendingAction)
    }
    @Test fun rejectDoesNotInventPointChangesAndCancelDoesNotWrite() = runTest(dispatcher) {
        runCurrent(); handlers["reject"] = { contract.copy(status = "cancelled", closedReason = "rejected") }
        model.mutable.value = model.mutable.value.copy(social = SocialState(pendingAction = "reject"))
        model.cancelContractAction(); model.confirmContractAction(); assertTrue(calls.isEmpty())
        model.mutable.value = model.mutable.value.copy(social = SocialState(pendingAction = "reject"))
        model.confirmContractAction(); runCurrent(); assertEquals(100, model.state.value.group?.points)
        assertEquals("cancelled", model.state.value.contract?.status)
    }
    @Test fun refreshDetailOpensCorrectGroupAndClearsEvidence() = runTest(dispatcher) {
        runCurrent(); handlers["challenge"] = { contract }; handlers["group"] = { group }
        model.openChallenge(contract); runCurrent(); assertEquals(contract, model.state.value.contract)
        assertEquals(ConnectedRoute.CONTRACT, model.state.value.route); assertNull(model.state.value.evidence)
    }
    @Test fun tournamentsAreDeduplicatedAndNoMoreMeansNoRequest() = runTest(dispatcher) {
        runCurrent(); val item = Tournament("id", "Halo", "single_elimination", "open", "date", emptyList())
        handlers["tournaments"] = { TournamentPage(listOf(item, item), false, null) }
        model.loadTournaments(); runCurrent(); model.loadTournaments(true)
        assertEquals(listOf(item), model.state.value.tournaments); assertTrue(model.state.value.tournamentsLoaded)
        assertEquals(1, calls.count { it == "tournaments" })
    }
    @Test fun photoBytesAreErasedEvenWhenUploadFails() = runTest(dispatcher) {
        runCurrent(); val bytes = byteArrayOf(1, 2, 3)
        model.mutable.value = model.state.value.copy(contract = contract.copy(status = "in_progress", acceptedBy = account.id))
        handlers["sendPhoto"] = { throw ApiFailure(503, "STORAGE_UNAVAILABLE") }
        model.sendPhoto { bytes to "image/png" }; runCurrent()
        assertTrue(bytes.all { it == 0.toByte() }); assertNotNull(model.state.value.error)
    }
    @Test fun busyStateBlocksNavigationAndEditing() = runTest(dispatcher) {
        runCurrent(); val old = model.state.value.copy(busy = true); model.mutable.value = old
        model.navigate(ConnectedRoute.GROUPS); model.joinCode("ABCDEF"); model.groupSearch("x")
        model.memberSearch("x"); model.editNickname("Changed"); model.boardFilter(BoardFilter(mine = true))
        assertEquals(old, model.state.value); assertTrue(calls.isEmpty())
    }

    @Test fun existingMembershipConflictKeepsGroupBalanceAndAllowsCorrection() = runTest(dispatcher) {
        runCurrent(); handlers["join"] = { throw ApiFailure(409, "ALREADY_MEMBER") }
        model.joinCode("ABCDEF"); model.joinGroup(); runCurrent()
        assertEquals(group, model.state.value.group); assertEquals(100, model.state.value.group?.points)
        assertEquals("ABCDEF", model.state.value.social.joinCode)
        assertFalse(model.state.value.busy); assertNotNull(model.state.value.error)
        assertNull(model.state.value.notice); assertEquals(1, calls.count { it == "join" })
        model.joinCode("UVWXYZ"); handlers["join"] = { group.copy(id = "555555555555555555555555") }
        model.joinGroup(); runCurrent(); assertEquals("", model.state.value.social.joinCode)
        assertNull(model.state.value.error); assertEquals(2, calls.count { it == "join" })
    }
    @Test fun occupiedNicknameKeepsFormAndIdentityUntilCorrectedSuccessfully() = runTest(dispatcher) {
        runCurrent(); model.mutable.value = model.state.value.copy(social = SocialState(editedNickname = "Occupied"))
        handlers["updateProfile"] = { throw ApiFailure(409, "NICKNAME_ALREADY_EXISTS") }
        model.saveProfile(); runCurrent()
        assertEquals(account, model.state.value.account); assertEquals("Occupied", model.state.value.social.editedNickname)
        assertEquals(409, model.state.value.diagnostic?.status); assertNull(model.state.value.notice)
        assertFalse(model.state.value.busy)
        val profile = Profile(account.copy(nickname = "Available"), "default", false, 1, 0, 1, 0, 90, 1)
        model.editNickname("Available"); handlers["updateProfile"] = { profile }
        model.saveProfile(); runCurrent()
        assertEquals("Available", model.state.value.account?.nickname); assertNull(model.state.value.error)
        assertEquals(2, calls.count { it == "updateProfile" })
    }
    @Test fun rejectedChallengeCannotBeConfirmedAsAcceptedAndKeepsBalance() = runTest(dispatcher) {
        runCurrent(); val rejected = contract.copy(status = "cancelled", closedReason = "rejected")
        handlers["reject"] = { rejected }
        model.mutable.value = model.state.value.copy(social = SocialState(pendingAction = "reject"))
        model.confirmContractAction(); runCurrent()
        model.requestContractAction("accept"); model.confirmContractAction(); runCurrent()
        assertEquals(rejected, model.state.value.contract); assertEquals(100, model.state.value.group?.points)
        assertFalse(calls.contains("accept")); assertNull(model.state.value.social.pendingAction)
    }
    @Test fun memberPagesAppendDeduplicateAndStopAfterLastPage() = runTest(dispatcher) {
        runCurrent(); var count = 0
        val first = Member(account.id, "Player", "default", "member", 100, "2026-01-01")
        val second = first.copy(id = "555555555555555555555555", nickname = "Rival")
        handlers["members"] = { args ->
            if (count++ == 0) { assertNull(args[2]); Page(listOf(first), true, second.id) }
            else { assertEquals(second.id, args[2]); Page(listOf(first, second), false, null) }
        }
        model.loadMembers(); runCurrent(); model.loadMembers(true); runCurrent(); model.loadMembers(true); runCurrent()
        assertEquals(listOf(first, second), model.state.value.social.members.items)
        assertEquals(2, calls.count { it == "members" })
    }
    @Test fun nonemptyBracketSelectionIsReadOnlyAndCanReturnToList() = runTest(dispatcher) {
        runCurrent()
        val match = Match("333333333333333333333333", 1, 1, group.id, null, 3, null, "in_progress")
        val item = Tournament("444444444444444444444444", "Halo", "single_elimination", "in_progress", "date", listOf(match))
        handlers["tournaments"] = { TournamentPage(listOf(item), false, null) }
        model.loadTournaments(); runCurrent(); model.tournament(item)
        assertEquals(listOf(match), model.state.value.selectedTournament?.matches)
        model.tournament(null); assertNull(model.state.value.selectedTournament)
        assertEquals(listOf("tournaments"), calls)
    }


    @Test fun validAccessDoubleSubmitWaitsForCustodyAndNavigatesOnlyAfterSuccess() = runTest(dispatcher) {
        for (register in listOf(false, true)) {
            runCurrent()
            model.mutable.value = ConnectedState(restoring = false, access =
                com.pactogg.app.feature.auth.presentation.AccessState(registering = register,
                    email = "qa@example.com", password = "StrongSynthetic!25", nickname = "Player", acceptedPrivacy = true))
            val method = if (register) "register" else "login"
            lateinit var pending: kotlin.coroutines.Continuation<Any?>
            handlers[method] = { args ->
                @Suppress("UNCHECKED_CAST")
                pending = args.last() as kotlin.coroutines.Continuation<Any?>
                kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
            }
            val previousCalls = calls.count { it == method }
            model.access(com.pactogg.app.feature.auth.presentation.AccessEvent.Submit); runCurrent()
            model.access(com.pactogg.app.feature.auth.presentation.AccessEvent.Submit); runCurrent()
            assertTrue(model.state.value.busy); assertNull(model.state.value.account)
            assertEquals(previousCalls + 1, calls.count { it == method })
            val user = com.pactogg.app.feature.auth.data.UserDto(account.id, account.email, account.nickname)
            val response = retrofit2.Response.success(if (register) 201 else 200, com.pactogg.app.core.network.Envelope(
                com.pactogg.app.feature.auth.data.SessionDto(user, "fresh-access", "fresh-refresh", "Bearer", 900)))
            pending.resumeWith(Result.success(response))
            model.state.first { !it.busy && it.account != null }
            assertEquals(ConnectedRoute.HOME, model.state.value.route)
            assertEquals("", model.state.value.access.password); assertNull(model.state.value.error)
        }
    }
    @Test fun validationAndModerationErrorsKeepDraftsEditableWithoutSuccessOrBalanceChanges() = runTest(dispatcher) {
        runCurrent()
        for ((status, code) in listOf(409 to "GROUP_LIMIT_REACHED", 422 to "VALIDATION_ERROR")) {
            model.mutable.value = model.state.value.copy(groupName = "Equipo")
            handlers["createGroup"] = { throw ApiFailure(status, code) }
            model.createGroup(); runCurrent()
            assertEquals(status, model.state.value.diagnostic?.status)
            assertEquals("Equipo", model.state.value.groupName); assertEquals(group, model.state.value.group)
            assertFalse(model.state.value.busy); assertNull(model.state.value.notice)
        }
        for ((status, code) in listOf(422 to "CONTENT_REJECTED", 503 to "MODERATION_UNAVAILABLE")) {
            val draft = ChallengeDraft(game = "Halo", condition = "Win", reward = "Reward")
            model.mutable.value = model.state.value.copy(reviewing = true, draft = draft)
            handlers["createChallenge"] = { throw ApiFailure(status, code) }
            model.createChallenge(); runCurrent()
            assertEquals(draft, model.state.value.draft); assertEquals(contract, model.state.value.contract)
            assertEquals(code, model.state.value.diagnostic?.code); assertNull(model.state.value.notice)
        }
    }
    @Test fun insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess() = runTest(dispatcher) {
        runCurrent()
        for ((status, code) in listOf(402 to "INSUFFICIENT_POINTS", 403 to "NOT_CONTRACT_TARGET", 409 to "INVALID_CONTRACT_STATE")) {
            model.mutable.value = model.state.value.copy(contract = contract, contractStateStale = false,
                social = SocialState(pendingAction = "accept"))
            handlers["accept"] = { throw ApiFailure(status, code) }
            model.confirmContractAction(); runCurrent()
            assertEquals(100, model.state.value.group?.points); assertEquals(contract, model.state.value.contract)
            assertTrue(model.state.value.contractStateStale); assertNull(model.state.value.notice)
            assertEquals(status, model.state.value.diagnostic?.status)
        }
    }
    @Test fun dismissingOldFeedbackCannotClearNewFeedbackOrFormData() = runTest(dispatcher) {
        runCurrent()
        model.mutable.value = model.state.value.copy(error = "New error", notice = "New notice",
            diagnostic = FailureDiagnostic(409, "NICKNAME_ALREADY_EXISTS", null),
            social = SocialState(editedNickname = "KeepDraft"))
        model.dismissError("Old error"); model.dismissNotice("Old notice")
        assertEquals("New error", model.state.value.error); assertEquals("New notice", model.state.value.notice)
        model.dismissError("New error"); model.dismissNotice("New notice")
        assertNull(model.state.value.error); assertNull(model.state.value.notice); assertNull(model.state.value.diagnostic)
        assertEquals("KeepDraft", model.state.value.social.editedNickname); assertTrue(calls.isEmpty())
    }
    @Test fun fieldErrorsStayWithTheirScreenAndDoNotHideOtherErrors() = runTest(dispatcher) {
        val profile = ConnectedState(account = account, route = ConnectedRoute.PROFILE, error = "Occupied",
            diagnostic = FailureDiagnostic(409, "NICKNAME_ALREADY_EXISTS", null))
        assertEquals("Occupied", profile.inlineFeedback())
        assertNull(profile.copy(route = ConnectedRoute.GROUPS).inlineFeedback())
        assertNull(profile.copy(diagnostic = FailureDiagnostic(503, "MODERATION_UNAVAILABLE", null)).inlineFeedback())
        val access = ConnectedState(error = "Credentials", diagnostic = FailureDiagnostic(401, "INVALID_CREDENTIALS", null))
        assertEquals("Credentials", access.inlineFeedback())
    }

    @Test fun evidenceModerationAndLimitErrorsKeepLinkWithoutInventingApprovedEvidence() = runTest(dispatcher) {
        runCurrent()
        for ((status, code) in listOf(422 to "CONTENT_REJECTED", 409 to "EVIDENCE_LIMIT_REACHED")) {
            model.mutable.value = model.state.value.copy(link = "https://youtu.be/synthetic",
                contract = contract.copy(status = "in_progress", acceptedBy = account.id), evidenceBlockedId = null)
            handlers["sendLink"] = { throw ApiFailure(status, code) }
            model.sendLink(); runCurrent()
            assertNull(model.state.value.evidence); assertNull(model.state.value.notice)
            assertEquals("https://youtu.be/synthetic", model.state.value.link)
            assertEquals(code, model.state.value.diagnostic?.code); assertFalse(model.state.value.busy)
        }
    }


    @Test fun preparedPhotoIsNotUploadedUntilConfirmedAndIsErasedWhenLeaving() = runTest(dispatcher) {
        runCurrent()
        model.mutable.value = model.state.value.copy(route = ConnectedRoute.EVIDENCE,
            contract = contract.copy(status = "in_progress", acceptedBy = account.id))
        val bytes = byteArrayOf(1, 2, 3)
        model.preparePhoto { com.pactogg.app.feature.connected.data.PreparedPhoto(bytes, 10, 20) }
        runCurrent()
        assertNotNull(model.state.value.selectedPhoto); assertTrue(calls.isEmpty())
        model.navigate(ConnectedRoute.HOME)
        assertNull(model.state.value.selectedPhoto); assertTrue(bytes.all { it == 0.toByte() })
    }

    @Test fun forbiddenEvidenceHidesFurtherUploadsAndDoesNotReadAnotherPhoto() = runTest(dispatcher) {
        runCurrent()
        model.mutable.value = model.state.value.copy(link = "https://youtu.be/synthetic",
            contract = contract.copy(status = "in_progress", acceptedBy = account.id))
        handlers["sendLink"] = { throw ApiFailure(403, "NOT_CONTRACT_TARGET") }
        model.sendLink(); runCurrent()
        assertEquals(contract.id, model.state.value.evidenceBlockedId)
        assertFalse(model.state.value.canUploadEvidence())
        model.sendPhoto { error("Must not read") }; runCurrent()
        assertEquals(1, calls.size)
    }

    @Test fun delayedPhotoPreparationCannotPopulateNextAccount() = runTest(dispatcher) {
        runCurrent()
        model.mutable.value = model.state.value.copy(contract = contract.copy(status = "in_progress", acceptedBy = account.id))
        val finish = CompletableDeferred<Unit>(); val bytes = byteArrayOf(1, 2, 3)
        model.preparePhoto { finish.await(); com.pactogg.app.feature.connected.data.PreparedPhoto(bytes, 10, 20) }
        runCurrent(); switchAccount(); finish.complete(Unit); runCurrent()
        assertNull(model.state.value.selectedPhoto); assertTrue(bytes.all { it == 0.toByte() })
    }

    @Test fun lostPhotoResponseBlocksAnotherWriteUntilContractRefresh() = runTest(dispatcher) {
        runCurrent()
        model.mutable.value = model.state.value.copy(contract = contract.copy(status = "in_progress", acceptedBy = account.id))
        handlers["sendPhoto"] = { throw java.io.IOException("connection lost") }
        val bytes = byteArrayOf(1, 2, 3)
        model.sendPhoto { bytes to "image/jpeg" }; runCurrent()
        assertTrue(model.state.value.contractStateStale); assertNull(model.state.value.evidence)
        model.sendPhoto { error("Must not resend") }; runCurrent()
        assertEquals(1, calls.size); assertTrue(bytes.all { it == 0.toByte() })
    }

    @Test fun unavailableEvidenceListDoesNotInventAnEmptyHistoryOrResend() = runTest(dispatcher) {
        runCurrent(); handlers["evidence"] = { throw ApiFailure(405, "HTTP_ERROR") }
        model.loadEvidence(); runCurrent()
        assertTrue(model.state.value.evidenceUnavailable)
        assertFalse(model.state.value.evidenceLoaded); assertNull(model.state.value.error)
        assertEquals(listOf("evidence"), calls)
    }
}
