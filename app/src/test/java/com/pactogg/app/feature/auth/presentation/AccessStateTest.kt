package com.pactogg.app.feature.auth.presentation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

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
