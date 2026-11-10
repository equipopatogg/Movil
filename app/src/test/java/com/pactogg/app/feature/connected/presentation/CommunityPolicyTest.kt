package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.community.domain.VoteSummaryModel
import com.pactogg.app.testing.shared.*
import org.junit.Assert.*
import org.junit.Test

@org.junit.experimental.categories.Category(com.pactogg.app.testing.UnitChecks::class)
class CommunityPolicyTest {
    private val summary = VoteSummaryModel(ContractId, "voting", 3, 0, 3,
        iAmJuror = true, iVoted = false, adminCanResolve = false)
    private val state = ConnectedState(account = com.pactogg.app.feature.auth.domain.Account(UserId, "qa@example.com", "QA"),
        community = CommunityState(loaded = true, summary = summary))
    @Test fun VOT_02_onlyAssignedJurorBeforeFirstVoteCanConfirm() {
        val action = CommunityAction("vote", ContractId, "legit")
        assertTrue(communityActionAllowed(state, action))
        assertFalse(communityActionAllowed(state.copy(community = state.community.copy(
            summary = summary.copy(iAmJuror = false))), action))
        assertFalse(communityActionAllowed(state.copy(community = state.community.copy(
            summary = summary.copy(iVoted = true))), action))
        assertFalse(communityActionAllowed(state, action.copy(id = OtherId)))
    }
    @Test fun VOT_04_requiresServerPermissionAndKnownOutcome() {
        val action = CommunityAction("resolve", ContractId, "fulfilled")
        assertFalse(communityActionAllowed(state, action))
        val allowed = state.copy(community = state.community.copy(summary = summary.copy(adminCanResolve = true)))
        assertTrue(communityActionAllowed(allowed, action))
        assertFalse(communityActionAllowed(allowed, action.copy(value = "arbitrary")))
    }
    @Test fun VOT_02_uncertainMutationRequiresRefreshBeforeAnotherAction() {
        assertFalse(communityActionAllowed(state.copy(community = state.community.copy(loaded = false)),
            CommunityAction("vote", ContractId, "legit")))
    }
    @Test fun NOT_03_emptyOrSignedOutCannotMarkAll() {
        assertFalse(communityActionAllowed(state, CommunityAction("readAll")))
        assertFalse(communityActionAllowed(state.copy(account = null), CommunityAction("vote", ContractId, "legit")))
    }

    @Test fun VOT_04_closedContractCannotBeResolvedAgain() {
        val closed = state.copy(community = state.community.copy(summary = summary.copy(
            status = "fulfilled", adminCanResolve = true)))
        assertFalse(communityActionAllowed(closed, CommunityAction("resolve", ContractId, "fulfilled")))
    }

    @Test fun pagedCommunityResponsesRejectMissingOrRepeatingCursor() {
        val routes = listOf(ConnectedRoute.JURY, ConnectedRoute.POINTS, ConnectedRoute.COMMITMENTS,
            ConnectedRoute.NOTIFICATIONS, ConnectedRoute.FAME)
        routes.forEach { route ->
            listOf(null, com.pactogg.app.core.network.PageMeta(true),
                com.pactogg.app.core.network.PageMeta(true, "same")).forEach { meta ->
                val error = runCatching { validateCommunityPage(meta, "same", route) }.exceptionOrNull()
                assertTrue(error is com.pactogg.app.core.network.ApiFailure)
            }
            validateCommunityPage(com.pactogg.app.core.network.PageMeta(false), null, route)
            validateCommunityPage(com.pactogg.app.core.network.PageMeta(true, "next"), "same", route)
        }
    }

    @Test fun communityRoutesKeepTheirNavigationSectionSelected() {
        assertEquals(ConnectedRoute.HOME, navigationSection(ConnectedRoute.VOTING))
        assertEquals(ConnectedRoute.HOME, navigationSection(ConnectedRoute.NOTIFICATIONS))
        listOf(ConnectedRoute.POINTS, ConnectedRoute.RANKING, ConnectedRoute.COMMITMENTS,
            ConnectedRoute.FAME).forEach { assertEquals(ConnectedRoute.GROUPS, navigationSection(it)) }
    }
}
