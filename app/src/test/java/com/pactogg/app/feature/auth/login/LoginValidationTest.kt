package com.pactogg.app.feature.auth.login

import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.testing.UnitChecks
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** AUTH-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class LoginValidationTest {
    @Test fun AUTH_01_rejectsInvalidEmailAndOversizedPassword() = runBlocking {
        assertFalse(InputRules.access("not-an-email", "pass", null, false))
        assertFalse(InputRules.access("qa@example.com", "a".repeat(129), null, false))
    }
}
