package com.pactogg.app.feature.connected.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.pactogg.app.ui.theme.PactoGGTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FeedbackDialogsTest {
    @get:Rule val compose = createComposeRule()

    @Test fun generalErrorIsDismissibleWithoutRetryingOperation() {
        val state = mutableStateOf(ConnectedState(restoring = false, error = "Revisa tu conexión."))
        var dismissals = 0
        compose.setContent { PactoGGTheme { SafeAppContent {
            AppFeedback(state.value, { dismissals++; state.value = state.value.copy(error = null) }, {})
        } } }
        compose.onNodeWithText("Revisa tu conexión.").assertIsDisplayed()
        compose.onNodeWithText("Entendido").performClick()
        compose.onAllNodes(isDialog()).assertCountEquals(0)
        compose.runOnIdle { assertEquals(1, dismissals) }
    }

    @Test fun invalidConfirmationCanStillBeCancelledWithoutExecutingAction() {
        val open = mutableStateOf(true)
        var confirms = 0
        compose.setContent { PactoGGTheme {
            if (open.value) ActionConfirmation("¿Aceptar este reto?", "Se retendrán 25 puntos virtuales.",
                "Aceptar reto", true, { confirms++ }, { open.value = false }, confirmEnabled = false)
        } }
        compose.onNodeWithText("Aceptar reto").assertIsNotEnabled()
        compose.onNodeWithText("Cancelar").performClick()
        compose.onAllNodes(isDialog()).assertCountEquals(0)
        compose.runOnIdle { assertEquals(0, confirms) }
    }

    @Test fun successNoticeCanBeDismissedWithoutChangingScreen() {
        val state = mutableStateOf(ConnectedState(restoring = false, notice = "Apodo actualizado."))
        var dismissals = 0
        compose.setContent { PactoGGTheme { SafeAppContent {
            AppFeedback(state.value, {}, { dismissals++; state.value = state.value.copy(notice = null) })
        } } }
        compose.onNodeWithText("Apodo actualizado.").assertIsDisplayed()
        compose.onNodeWithText("Entendido").performClick()
        compose.waitUntil(5000) { dismissals == 1 }
        compose.onAllNodes(isDialog()).assertCountEquals(0)
        compose.runOnIdle { assertEquals(ConnectedRoute.HOME, state.value.route) }
    }

    @Test fun occupiedNicknameIsShownAtFieldWithoutGeneralDialog() {
        val state = ConnectedState(restoring = false, error = "El apodo ya está en uso.",
            diagnostic = FailureDiagnostic(409, "NICKNAME_ALREADY_EXISTS", null))
        compose.setContent { PactoGGTheme { SafeAppContent {
            com.pactogg.app.feature.auth.presentation.AccessScreen(
                com.pactogg.app.feature.auth.presentation.AccessState(registering = true), {},
                fieldError = state.inlineFeedback(), fieldErrorCode = state.diagnostic?.code)
            AppFeedback(state, {}, {})
        } } }
        compose.onAllNodes(isDialog()).assertCountEquals(0)
        compose.onNodeWithText("El apodo ya está en uso.").assertExists()
    }
}
