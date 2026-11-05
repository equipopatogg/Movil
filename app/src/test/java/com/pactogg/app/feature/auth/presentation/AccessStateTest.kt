package com.pactogg.app.feature.auth.presentation

import org.junit.experimental.categories.Category
import com.pactogg.app.testing.UnitChecks
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@Category(UnitChecks::class)
class AccessStateTest {
    @Test fun switchingFormsDiscardsSensitiveInputs() {
        val state = AccessState(email = "sample@example.com", password = "synthetic", passwordVisible = true)
            .reduce(AccessEvent.SwitchMode)
        assertTrue(state.registering)
        assertEquals("", state.password)
        assertEquals("", state.email)
        assertFalse(state.passwordVisible)
    }

    @Test fun submissionDoesNotCreateSessionOrNavigate() {
        val state = AccessState().reduce(AccessEvent.Submit)
        assertTrue(state.submitted)
        assertFalse(state.loading)
        assertFalse(state.registering)
    }
}
