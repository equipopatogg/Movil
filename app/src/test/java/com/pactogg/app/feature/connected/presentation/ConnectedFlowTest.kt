package com.pactogg.app.feature.connected.presentation

import androidx.lifecycle.ViewModelStore
import com.pactogg.app.core.network.ApiClientFactory
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.domain.*
import com.pactogg.app.feature.connected.domain.*
import com.pactogg.app.testing.UnitChecks
import java.lang.reflect.Proxy
import kotlinx.coroutines.*
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
    private val account = Account("111111111111111111111111", "qa@example.com", "Player")
    private val group = Group("222222222222222222222222", "Equipo", "ABCDEF", "member", 100, listOf(25))
    private val contract = Challenge("333333333333333333333333", group.id, "Halo", "Win", "Reward",
        "2099-01-01T00:00:00Z", 25, "open", "444444444444444444444444", "member", account.id, version = 1)
    private inline fun <reified T> repository(): T = Proxy.newProxyInstance(T::class.java.classLoader,
        arrayOf(T::class.java)) { _, method, arguments ->
        calls += method.name
        handlers[method.name]?.invoke(arguments ?: emptyArray()) ?: error("Unexpected ${method.name}")
    } as T

    @Before fun setup() {
        Dispatchers.setMain(dispatcher)
        val session = Session(account, "synthetic-access", "synthetic-refresh", Long.MAX_VALUE)
        val store = object : SessionStore {
            override fun read() = session
            override fun write(session: Session) = Unit
            override fun clear() = Unit
        }
        val api = ApiClientFactory().create("https://localhost/").create(PactoApi::class.java)
        val sessions = SessionManager(api, store)
        runBlocking { sessions.restore() }
        model = ConnectedViewModel(sessions, repository<ProductRepository>(), repository<SocialRepository>())
        lifecycle.put("test", model)
        model.mutable.value = ConnectedState(restoring = false, account = account, group = group, contract = contract)
    }
    @After fun cleanup() { lifecycle.clear(); Dispatchers.resetMain() }

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
}
