package com.pactogg.app.core.session

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.push.DeviceRequest
import com.pactogg.app.core.push.PushRegistration
import com.pactogg.app.feature.auth.data.GoogleRequest
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-10 (push), PL-11 (recuperar contraseña) y PL-13 (correo verificado): reglas de seguridad de la cuenta. */
@Category(SecurityChecks::class)
class AccountSecurityTest {
    private val pushToken = "f".repeat(60)

    @Test fun PL_10_pushIsNeverRegisteredWithoutAccountPermissionOrValidToken() = runBlocking {
        val registered = mutableListOf<String>()
        var owner: String? = null
        val registration = PushRegistration({ owner }, { _, token -> registered += token }, { _, _ -> })
        registration.sync(pushToken, permitted = true)
        owner = "account-a"
        registration.sync(pushToken, permitted = false)
        registration.sync("token con espacios ".repeat(3), permitted = true)
        registration.sync("corto", permitted = true)
        assertTrue("sin cuenta, sin permiso o con token inválido no se registra nada", registered.isEmpty())
        registration.sync(pushToken, permitted = true)
        assertEquals(listOf(pushToken), registered)
    }

    @Test fun PL_10_closingSessionBlocksLateRegistrations() = runBlocking {
        val registered = mutableListOf<String>()
        val removed = mutableListOf<String>()
        val registration = PushRegistration({ "account-a" }, { _, token -> registered += token },
            { _, token -> removed += token })
        registration.sync(pushToken, permitted = true)
        registration.detach(pushToken)
        registration.sync(pushToken, permitted = true)
        assertEquals("al cerrar sesión se da de baja el teléfono", listOf(pushToken), removed)
        assertEquals("un token tardío no vuelve a registrar el teléfono", 1, registered.size)
    }

    @Test fun PL_10_deviceRoutesWithoutSessionNeverReachTheServer() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            val register = runCatching {
                f.sessions.authorized { f.api.registerDevice(it, DeviceRequest(pushToken, version = "1.0.0")) }
            }.exceptionOrNull()
            val remove = runCatching { f.sessions.authorized { f.api.removeDevice(it, pushToken) } }.exceptionOrNull()
            assertEquals(401, (register as ApiFailure).status)
            assertEquals(401, (remove as ApiFailure).status)
            assertEquals(0, f.server.requestCount)
        }
    }

    @Test fun PL_11_recoveryNeverSendsTheSessionTokenNorTouchesTheSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            val before = f.store.saved
            f.server.enqueue(ApiTestFixture.json("", 202))
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("""{"password_reset":true}""")))
            f.sessions.forgotPassword("qa@example.com")
            f.sessions.resetPassword("Ab3_-".repeat(9), "una-clave-nueva-larga")
            repeat(2) {
                val request = f.server.takeRequest()
                assertNull("${request.path} es pública: no lleva el token de la sesión", request.getHeader("Authorization"))
                assertTrue(request.body.readUtf8().contains(Access).not())
            }
            assertEquals("recuperar la contraseña no reemplaza la sesión actual", before, f.store.saved)
        }
    }

    @Test fun PL_13_resendVerificationRequiresSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            val error = runCatching { f.sessions.authorized { f.api.resendVerification(it) } }.exceptionOrNull()
            assertEquals(401, (error as ApiFailure).status)
            assertEquals(0, f.server.requestCount)
        }
    }

    @Test fun PL_08_rejectedGoogleTokenNeverCreatesSessionNorSendsBearer() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            f.server.enqueue(ApiTestFixture.json("""{"code":"INVALID_GOOGLE_TOKEN","message":"private"}""", 401))
            val error = runCatching { f.sessions.google(GoogleRequest("g".repeat(40))) }.exceptionOrNull()
            assertEquals("INVALID_GOOGLE_TOKEN", (error as ApiFailure).code)
            assertNull(f.server.takeRequest().getHeader("Authorization"))
            assertNull(f.store.saved); assertNull(f.sessions.current.value)
            assertEquals("no reintenta un token rechazado", 1, f.server.requestCount)
        }
    }

    @Test fun PL_13_verifyEmailLinkNeverCarriesTheSessionToken() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            val before = f.store.saved
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("""{"email_verified":true}""")))
            f.api.verifyEmail(com.pactogg.app.feature.auth.data.VerifyEmailRequest("v".repeat(43)))
            val request = f.server.takeRequest()
            assertNull("el enlace del correo es público: no lleva el token", request.getHeader("Authorization"))
            assertEquals("verificar el correo no cambia la sesión", before, f.store.saved)
        }
    }
}
