package com.pactogg.app.navigation

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcel
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.text.AnnotatedString
import com.pactogg.app.MainActivity
import java.util.concurrent.atomic.AtomicBoolean
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AccessNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun emptyLoginCannotBypassAuthentication() {
        compose.activityRule.scenario.onActivity { assertEquals(Intent.ACTION_MAIN, it.intent.action) }
        compose.waitUntil(5000) {
            compose.onAllNodes(hasText("Iniciar sesión") and hasClickAction()).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNode(hasText("Iniciar sesión") and hasClickAction()).performScrollTo().performClick()
        compose.onNodeWithText("Escribe un correo válido, como nombre@correo.com.")
            .performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Escribe tu contraseña.").performScrollTo().assertIsDisplayed()
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

    @Test fun rotationAndRecreationKeepDraftWithoutSavingPasswordOrAuthenticating() {
        waitForLogin()
        val email = "rotation@example.invalid"
        val password = "synthetic rotation phrase 753!"
        compose.onNode(hasText("Correo electrónico") and hasSetTextAction())
            .performScrollTo().performTextInput(email)
        compose.onNode(hasText("Contraseña") and hasSetTextAction())
            .performScrollTo().performTextInput(password)
        val observer = SnapshotObserver(password)
        val application = compose.activity.application
        application.registerActivityLifecycleCallbacks(observer)
        try {
            compose.activityRule.scenario.onActivity { it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE }
            compose.waitUntil(15000) {
                compose.activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            }
            compose.activityRule.scenario.recreate()
            compose.activityRule.scenario.onActivity { assertEquals(Intent.ACTION_MAIN, it.intent.action) }
            compose.waitForIdle()
            compose.onNode(hasText("Correo electrónico") and hasSetTextAction())
                .performScrollTo().assertTextContains(email)
            compose.onNodeWithText("Mostrar").performScrollTo().performClick()
            compose.onNode(hasText("Contraseña") and hasSetTextAction())
                .performScrollTo().assertTextContains(password)
            compose.onNodeWithText("Grupos").assertDoesNotExist()
            assertTrue("Actual saved-state callback must run", observer.checked.get())
            assertFalse("Password must not be serialized in activity state", observer.leaked.get())
        } finally {
            application.unregisterActivityLifecycleCallbacks(observer)
            compose.activityRule.scenario.onActivity { it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED }
        }
    }

    @Test fun switchingRegistrationAndLoginClearsCredentialsAndConsent() {
        waitForLogin()
        compose.onNode(hasText("Correo electrónico") and hasSetTextAction())
            .performScrollTo().performTextInput("switch@example.invalid")
        compose.onNode(hasText("Contraseña") and hasSetTextAction())
            .performScrollTo().performTextInput("synthetic switch phrase")
        compose.onNodeWithText("¿Nuevo por aquí? Crea tu cuenta").performScrollTo().performClick()
        assertEmpty("Correo electrónico")
        assertEmpty("Contraseña")
        compose.onNode(isToggleable()).performScrollTo().performClick().assertIsOn()
        compose.onNodeWithText("Ya tengo cuenta").performScrollTo().performClick()
        assertEmpty("Correo electrónico")
        assertEmpty("Contraseña")
        compose.onNodeWithText("He leído y acepto el aviso de privacidad").assertDoesNotExist()
        compose.onNodeWithText("Grupos").assertDoesNotExist()
        compose.onNodeWithText("¿Nuevo por aquí? Crea tu cuenta").performScrollTo().performClick()
        compose.onNode(isToggleable()).performScrollTo().assertIsOff()
    }

    private fun waitForLogin() = compose.waitUntil(10000) {
        compose.onAllNodes(hasText("Iniciar sesión") and hasClickAction()).fetchSemanticsNodes().isNotEmpty()
    }

    private fun assertEmpty(label: String) {
        compose.onNode(hasText(label) and hasSetTextAction()).performScrollTo()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText, AnnotatedString("")))
    }

    private class SnapshotObserver(private val marker: String) : Application.ActivityLifecycleCallbacks {
        val checked = AtomicBoolean(false)
        val leaked = AtomicBoolean(false)
        override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
            if (activity !is MainActivity) return
            val parcel = Parcel.obtain()
            try {
                parcel.writeBundle(outState)
                val bytes = parcel.marshall()
                if (bytes.toString(Charsets.UTF_8).contains(marker) ||
                    bytes.toString(Charsets.UTF_16LE).contains(marker)) leaked.set(true)
                checked.set(true)
                bytes.fill(0)
            } finally { parcel.recycle() }
        }
        override fun onActivityCreated(activity: Activity, state: Bundle?) = Unit
        override fun onActivityStarted(activity: Activity) = Unit
        override fun onActivityResumed(activity: Activity) = Unit
        override fun onActivityPaused(activity: Activity) = Unit
        override fun onActivityStopped(activity: Activity) = Unit
        override fun onActivitySaveInstanceState(activity: Activity, state: Bundle) = Unit
        override fun onActivityDestroyed(activity: Activity) = Unit
    }
}
