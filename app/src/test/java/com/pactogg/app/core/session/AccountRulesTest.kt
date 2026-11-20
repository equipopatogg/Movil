package com.pactogg.app.core.session

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.push.validPushToken
import com.pactogg.app.feature.auth.data.GoogleRequest
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.FixtureStore
import java.lang.reflect.Proxy
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-08, PL-10 y PL-11: reglas de entrada de la cuenta que se aplican en el teléfono, sin red. */
@Category(UnitChecks::class)
class AccountRulesTest {
    private val sessions = SessionManager(Proxy.newProxyInstance(PactoApi::class.java.classLoader,
        arrayOf(PactoApi::class.java)) { _, _, _ -> throw AssertionError("Sin red en una prueba unitaria") } as PactoApi,
        FixtureStore())

    private fun rejected(block: suspend () -> Unit) =
        assertTrue(runCatching { runBlocking { block() } }.exceptionOrNull() is IllegalArgumentException)

    @Test fun PL_11_recoveryRejectsBadEmailTokenAndShortPassword() {
        rejected { sessions.forgotPassword("") }
        rejected { sessions.forgotPassword("correo@") }
        rejected { sessions.resetPassword("", "una-clave-nueva-larga") }
        rejected { sessions.resetPassword("Ab3_-".repeat(9), "123") }
    }

    @Test fun PL_08_googleRejectsMalformedTokenAndNicknameWithoutConsent() {
        rejected { sessions.google(GoogleRequest("corto")) }
        rejected { sessions.google(GoogleRequest("g".repeat(40) + " ")) }
        rejected { sessions.google(GoogleRequest("g".repeat(40), "Player", acceptPrivacy = false)) }
    }

    @Test fun PL_10_pushTokenMustHaveSaneLengthAndNoSpaces() {
        assertTrue(validPushToken("f".repeat(60)))
        assertFalse(validPushToken("corto"))
        assertFalse(validPushToken("f".repeat(30) + " " + "f".repeat(30)))
        assertFalse(validPushToken("f".repeat(5000)))
    }

    @Test fun PL_13_verifyEmailSendsOnlyTheTokenAndResendNeedsSession() = runBlocking {
        val json = kotlinx.serialization.json.Json.encodeToString(
            com.pactogg.app.feature.auth.data.VerifyEmailRequest.serializer(),
            com.pactogg.app.feature.auth.data.VerifyEmailRequest("abc"))
        org.junit.Assert.assertEquals("""{"token":"abc"}""", json)
        val error = runCatching { sessions.authorized { sessions.api.resendVerification(it) } }.exceptionOrNull()
        org.junit.Assert.assertEquals(401, (error as com.pactogg.app.core.network.ApiFailure).status)
    }
}
