package com.pactogg.app.feature.connected.presentation

import org.junit.experimental.categories.Category
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.Group
import org.junit.Assert.*
import org.junit.Test

@Category(UnitChecks::class)
class ContractPolicyTest {
    private val group = Group("group", "Equipo", "ABCDEF", "member", 100, listOf(25, 50))
    private val challenge = Challenge("id", "group", "Halo", "Ganar", "Elegir", "2099-11-10T23:59:00Z",
        25, "open", challengerId = "creator", targetType = "group", version = 1)
    @Test fun onlyEligibleMemberWithKnownBalanceAndVersionCanAccept() {
        assertTrue(canAccept(challenge, "rival", group, false))
        assertFalse(canAccept(challenge, "creator", group, false))
        assertFalse(canAccept(challenge, "rival", group.copy(points = 0), false))
        assertFalse(canAccept(challenge, "rival", group.copy(id = "other"), false))
        assertFalse(canAccept(challenge.copy(version = null), "rival", group, false))
        assertFalse(canAccept(challenge, "rival", group, true))
        assertFalse(canAccept(challenge.copy(deadline = "2020-11-10T23:59:00Z"), "rival", group, false))
        assertTrue(canAccept(challenge.copy(deadline = "2099-11-10T23:59:00.1234567Z"), "rival", group, false))
    }
    @Test fun directedActionsAreExclusiveAndRejectionDoesNotNeedFunds() {
        val directed = challenge.copy(targetType = "member", targetId = "rival")
        assertTrue(canReject(directed, "rival", false))
        assertFalse(canReject(directed, "other", false))
        assertFalse(canReject(challenge, "rival", false))
        assertFalse(canAccept(directed, "other", group, false))
        assertFalse(canReject(directed.copy(status = "in_progress"), "rival", false))
        assertFalse(canReject(directed, "rival", true))
    }
    @Test fun pagesMergeByRealIdentityAndResetForNewFilter() {
        val state = ListState(listOf(group), loaded = true)
        val page = com.pactogg.app.feature.connected.domain.Page(listOf(group.copy(points = 75)), false, null)
        assertEquals(1, state.merge(page, true) { it.id }.items.size)
        assertEquals(75, state.merge(page, false) { it.id }.items.single().points)
    }
}
