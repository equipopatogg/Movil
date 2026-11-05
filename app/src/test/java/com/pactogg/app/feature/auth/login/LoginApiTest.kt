package com.pactogg.app.feature.auth.login
import com.pactogg.app.testing.shared.*

import com.pactogg.app.core.network.ApiFailure


import com.pactogg.app.feature.auth.data.LoginRequest
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** AUTH-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class LoginApiTest {
    @Test fun AUTH_01_sendsCredentialsAndConsumesLoginResponse() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            fixture.enqueueSuccess(Endpoint.LOGIN)
            fixture.sessions.login(LoginRequest("qa@example.com", "synthetic password"))
            val request = fixture.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/auth/login", request.path?.substringBefore('?'))
            assertNull(request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertEquals("qa@example.com", data?.get("email")?.jsonPrimitive?.content)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun AUTH_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(422 to "VALIDATION_ERROR")) {
            ApiTestFixture().use { f ->
                f.prepare(false); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.sessions.login(com.pactogg.app.feature.auth.data.LoginRequest("qa@example.com", "synthetic password")) }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }


    @Test fun AUTH_01_lostResponseIsNotReplayedOrReportedAsSuccess() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(false); val before = f.store.saved
            f.server.enqueue(okhttp3.mockwebserver.MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST))
            assertTrue(runCatching { f.sessions.login(com.pactogg.app.feature.auth.data.LoginRequest("qa@example.com", "synthetic password")) }.exceptionOrNull() is java.io.IOException)
            assertNull(f.store.saved); assertEquals(1, f.server.requestCount)
        }
    }

}
