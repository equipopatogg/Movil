package com.pactogg.app.feature.auth.recovery

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.session.forgotPassword
import com.pactogg.app.core.session.resetPassword
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-11 (AUTH-06, AUTH-07) contra un servidor simulado. */
@Category(IntegrationChecks::class)
class PasswordRecoveryApiTest {
    private val token = "Ab3_-".repeat(9)

    @Test fun PL_11_forgotPasswordSendsOnlyTheEmailWithoutSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            f.server.enqueue(ApiTestFixture.json("", 202))
            f.sessions.forgotPassword("  qa@example.com ")
            val request = f.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/auth/forgot-password", request.path)
            assertNull(request.getHeader("Authorization"))
            assertEquals("""{"email":"qa@example.com"}""", request.body.readUtf8())
            assertNull(f.store.saved)
        }
    }

    @Test fun PL_11_resetPasswordSendsTokenAndNewPassword() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("""{"password_reset":true}""")))
            f.sessions.resetPassword(token, "una-clave-nueva-larga")
            val request = f.server.takeRequest()
            assertEquals("/api/v1/auth/reset-password", request.path)
            val body = request.body.readUtf8()
            assertTrue(body.contains("\"token\":\"$token\""))
            assertTrue(body.contains("\"new_password\":\"una-clave-nueva-larga\""))
            assertNull(f.store.saved)
        }
    }

    @Test fun PL_11_expiredLinkReportsItsCode() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            f.server.enqueue(ApiTestFixture.json("""{"code":"INVALID_OR_EXPIRED_TOKEN"}""", 400))
            val error = runCatching { f.sessions.resetPassword(token, "una-clave-nueva-larga") }.exceptionOrNull()
            assertEquals("INVALID_OR_EXPIRED_TOKEN", (error as ApiFailure).code)
        }
    }

    @Test fun PL_11_badEmailTokenOrShortPasswordNeverReachTheNetwork() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            listOf<suspend () -> Unit>(
                { f.sessions.forgotPassword("sin-arroba") },
                { f.sessions.resetPassword("corto", "una-clave-nueva-larga") },
                { f.sessions.resetPassword("token con espacios largo", "una-clave-nueva-larga") },
                { f.sessions.resetPassword(token, "corta") },
            ).forEach { call ->
                assertTrue(runCatching { call() }.exceptionOrNull() is IllegalArgumentException)
            }
            assertEquals(0, f.server.requestCount)
        }
    }
}
