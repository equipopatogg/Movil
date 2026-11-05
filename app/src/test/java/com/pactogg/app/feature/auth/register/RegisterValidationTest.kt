package com.pactogg.app.feature.auth.register

import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.testing.UnitChecks
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** USR-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class RegisterValidationTest {
    @Test fun USR_01_rejectsWeakPasswordMissingConsentAndOversizedNickname() = runBlocking {
        assertFalse(InputRules.access("qa@example.com", "a".repeat(11), "Player", true))
        assertFalse(InputRules.access("qa@example.com", "a".repeat(12), "Player", false))
        assertFalse(InputRules.access("qa@example.com", "a".repeat(12), "a".repeat(25), true))
    }
}
