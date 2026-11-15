package com.pactogg.app.feature.auth.google

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.session.google
import com.pactogg.app.feature.auth.data.GoogleRequest
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.UserId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-08 (AUTH-02): acceso con Google contra un servidor simulado. */
@Category(IntegrationChecks::class)
class GoogleAccessApiTest {
    private val token = "g".repeat(40)

    @Test fun PL_08_newAccountSendsNicknameAndConsentAndStoresSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.sessionJson, 201))
            f.sessions.google(GoogleRequest(token, "Player", true, "2026-10-01"))
            val request = f.server.takeRequest()
            assertEquals("POST", request.method); assertEquals("/api/v1/auth/google", request.path)
            assertNull(request.getHeader("Authorization"))
            val body = request.body.readUtf8()
            listOf("\"id_token\":\"$token\"", "\"nickname\":\"Player\"", "\"accept_privacy\":true")
                .forEach { assertTrue(it, body.contains(it)) }
            assertEquals(UserId, f.store.saved?.account?.id)
        }
    }

    @Test fun PL_08_existingAccountSignsInWithTokenOnly() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.sessionJson, 200))
            f.sessions.google(GoogleRequest(token))
            assertTrue(f.server.takeRequest().body.readUtf8().contains("\"nickname\":null"))
            assertEquals(UserId, f.sessions.current.value?.account?.id)
        }
    }

    @Test fun PL_08_nicknameRequiredCreatesNoSessionAndIsNotRetried() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            f.server.enqueue(ApiTestFixture.json("""{"code":"NICKNAME_REQUIRED","message":"private"}""", 422))
            val error = runCatching { f.sessions.google(GoogleRequest(token)) }.exceptionOrNull() as ApiFailure
            assertEquals(422, error.status); assertEquals("NICKNAME_REQUIRED", error.code)
            assertNull(f.store.saved); assertNull(f.sessions.current.value)
            assertEquals(1, f.server.requestCount)
        }
    }

    @Test fun PL_08_malformedTokenOrNicknameWithoutConsentNeverReachesTheNetwork() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            listOf(GoogleRequest("corto"), GoogleRequest("con espacio ".repeat(4)),
                GoogleRequest(token, "Player", false, "2026-10-01"), GoogleRequest(token, "x", true, "2026-10-01"))
                .forEach { assertTrue(runCatching { f.sessions.google(it) }.exceptionOrNull() is IllegalArgumentException) }
            assertEquals(0, f.server.requestCount)
        }
    }
}
