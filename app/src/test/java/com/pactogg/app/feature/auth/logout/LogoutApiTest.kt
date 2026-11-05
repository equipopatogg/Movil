package com.pactogg.app.feature.auth.logout
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure



import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import com.pactogg.app.testing.shared.Refresh
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** AUTH-04: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class LogoutApiTest {
    @Test fun AUTH_04_revokesSessionAndClearsLocalCustody() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.LOGOUT)
            fixture.sessions.logout()
            val request = fixture.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/auth/logout", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertNull(fixture.store.saved)
            assertEquals(Refresh, data?.get("refresh_token")?.jsonPrimitive?.content)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun AUTH_04_transportDisconnectStillErasesLocalTokensWithoutRetry() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(okhttp3.mockwebserver.MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST))
            assertTrue(runCatching { f.sessions.logout() }.exceptionOrNull() is java.io.IOException)
            assertNull(f.store.saved); assertNull(f.sessions.current.value)
            assertEquals(1, f.server.requestCount)
        }
    }


    @Test fun AUTH_04_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(401 to "UNAUTHORIZED")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.sessions.logout() }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertNull(f.store.saved); assertNull(f.sessions.current.value)
                assertEquals(1, f.server.requestCount)
            }
        }
    }

}
