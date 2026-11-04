package com.pactogg.app.navigation

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.pactogg.app.MainActivity
import org.junit.Rule
import org.junit.Test

class AccessNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun emptyLoginCannotBypassAuthentication() {
        compose.waitUntil(5000) {
            compose.onAllNodes(hasText("Iniciar sesión") and hasClickAction()).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNode(hasText("Iniciar sesión") and hasClickAction()).performScrollTo().performClick()
        compose.onNodeWithText("Revisa correo, contraseña, apodo y consentimiento de privacidad.")
            .performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Explorar").assertDoesNotExist()
        compose.onNodeWithText("Grupos").assertDoesNotExist()
    }

    @Test fun registerDisplaysProvisionalPrivacyBeforeAnySubmission() {
        compose.waitUntil(5000) {
            compose.onAllNodes(hasText("Iniciar sesión") and hasClickAction()).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("¿Nuevo por aquí? Crea tu cuenta").performScrollTo().performClick()
        compose.onNodeWithText("He leído y acepto el aviso de privacidad").performScrollTo().performClick()
        compose.onNodeWithText("Aviso de privacidad · versión 2026-10-01").assertIsDisplayed()
    }
}
