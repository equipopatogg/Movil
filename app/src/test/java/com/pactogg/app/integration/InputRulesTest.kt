package com.pactogg.app.integration

import org.junit.experimental.categories.Category
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.feature.connected.domain.Group
import com.pactogg.app.feature.connected.domain.InputRules
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@Category(UnitChecks::class)
class InputRulesTest {
    @Test fun loginDoesNotApplyRegistrationMinimumAndConsentStartsFalse() {
        assertTrue(InputRules.access("a@example.com", "x", null, false))
        assertFalse(InputRules.access("a@example.com", "x", "Player", true))
        assertFalse(InputRules.access("a@example.com", "long password", "Player", false))
        assertTrue(InputRules.access("a@example.com", "long password", "Player", true))
    }

    @Test fun evidenceRejectsSpoofedHostsCredentialsAndInsecureUrls() {
        listOf("http://youtube.com/video", "https://youtube.com.evil.test/a", "https://x@youtube.com/a",
            "https://youtube.com:8443/a").forEach { assertFalse(InputRules.evidenceLink(it)) }
        assertTrue(InputRules.evidenceLink("https://www.youtube.com/watch?v=sample"))
        assertTrue(InputRules.evidenceLink("https://m.youtube.com/watch?v=sample"))
    }

    @Test fun challengeRejectsUnreturnedBondsPastDatesAndCurrency() {
        val group = Group("id", "Team", "CODE12", "admin", 0, listOf(25))
        val draft = ChallengeDraft("Halo", "Ganar", "Elegir juego", "2099-01-01T00:00:00Z", 25)
        assertTrue(InputRules.challenge(group, draft, 0))
        assertFalse(InputRules.challenge(group, draft.copy(bond = 50), 0))
        assertFalse(InputRules.challenge(group, draft.copy(reward = "€100"), 0))
        assertFalse(InputRules.challenge(group, draft.copy(deadline = "2026-02-31T00:00:00Z"), 0))
    }
}
