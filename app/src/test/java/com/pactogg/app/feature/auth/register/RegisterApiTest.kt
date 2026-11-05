package com.pactogg.app.feature.auth.register

import com.pactogg.app.testing.shared.*

import com.pactogg.app.core.network.ApiFailure


import com.pactogg.app.feature.auth.data.RegisterRequest
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** USR-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class RegisterApiTest {
    @Test fun USR_01_sendsRegistrationAndAcceptsInitialSession() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            fixture.enqueueSuccess(Endpoint.REGISTER)
            fixture.sessions.register(RegisterRequest("qa@example.com", "synthetic password", "Player", true, "2026-10-01"))
            val request = fixture.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/auth/register", request.path?.substringBefore('?'))
            assertNull(request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertEquals(true, data?.get("accept_privacy")?.jsonPrimitive?.boolean)
            assertEquals("2026-10-01", data?.get("privacy_version")?.jsonPrimitive?.content)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun USR_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(409 to "EMAIL_ALREADY_EXISTS", 422 to "VALIDATION_ERROR")) {
            ApiTestFixture().use { f ->
                f.prepare(false); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.sessions.register(com.pactogg.app.feature.auth.data.RegisterRequest("qa@example.com", "synthetic password", "Player", true, "2026-10-01")) }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }


    @Test fun USR_01_lostResponseIsNotReplayedOrReportedAsSuccess() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(false); val before = f.store.saved
            f.server.enqueue(okhttp3.mockwebserver.MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST))
            assertTrue(runCatching { f.sessions.register(com.pactogg.app.feature.auth.data.RegisterRequest("qa@example.com", "synthetic password", "Player", true, "2026-10-01")) }.exceptionOrNull() is java.io.IOException)
            assertNull(f.store.saved); assertEquals(1, f.server.requestCount)
        }
    }


    @Test fun USR_01_failedCustodyCannotPublishRegisteredIdentity() = runBlocking {
        ApiTestFixture().use { f ->
            val brokenStore = object : com.pactogg.app.feature.auth.domain.SessionStore {
                override fun read(): com.pactogg.app.feature.auth.domain.Session? = null
                override fun write(session: com.pactogg.app.feature.auth.domain.Session) { throw java.io.IOException("synthetic storage failure") }
                override fun clear() = Unit
            }
            val sessions = com.pactogg.app.core.session.SessionManager(f.api, brokenStore)
            f.enqueueSuccess(Endpoint.REGISTER)
            assertTrue(runCatching { sessions.register(com.pactogg.app.feature.auth.data.RegisterRequest(
                "qa@example.com", "StrongSynthetic!25", "Player", true, "2026-10-01")) }.isFailure)
            assertNull(sessions.current.value); assertEquals(1, f.server.requestCount)
        }
    }

}
