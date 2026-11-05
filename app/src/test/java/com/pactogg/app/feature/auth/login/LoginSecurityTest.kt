package com.pactogg.app.feature.auth.login

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.auth.data.LoginRequest
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Sensitive
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** AUTH-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(SecurityChecks::class)
class LoginSecurityTest {
    @Test fun AUTH_01_invalidCredentialsNeverCreateSession() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"UNAUTHORIZED","message":"$Sensitive","request_id":"safe-request","details":{"token":"$Sensitive"}}""", 401))
            val failure = runCatching { fixture.sessions.login(LoginRequest("qa@example.com", "synthetic password")) }.exceptionOrNull()
            assertTrue(failure is ApiFailure)
            failure as ApiFailure
            assertEquals(401, failure.status)
            assertEquals("UNAUTHORIZED", failure.code)
            assertEquals("safe-request", failure.requestId)
            assertFalse(failure.toString().contains(Sensitive))
            assertFalse(failure.toString().contains(Access))
            assertNull(failure.cause)
            assertEquals(1, fixture.server.requestCount)
            val request = fixture.server.takeRequest()
            assertEquals("/api/v1/auth/login", request.path?.substringBefore('?'))
            assertNull(request.getHeader("Authorization"))
            assertNull(fixture.store.saved)
        }
    }

    @Test fun AUTH_01_serviceFailureDoesNotRetryLogin() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"STORAGE_UNAVAILABLE","message":"$Sensitive"}""", 503))
            val error = runCatching { fixture.sessions.login(LoginRequest("qa@example.com", "synthetic password")) }.exceptionOrNull() as ApiFailure
            assertEquals(503, error.status)
            assertFalse(error.toString().contains(Sensitive))
            assertEquals(1, fixture.server.requestCount)
            assertNull(fixture.store.saved)
        }
    }

    @Test fun AUTH_01_rateLimitDoesNotRetryLogin() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"RATE_LIMITED","message":"$Sensitive"}""", 429))
            val error = runCatching { fixture.sessions.login(LoginRequest("qa@example.com", "synthetic password")) }.exceptionOrNull() as ApiFailure
            assertEquals(429, error.status)
            assertEquals(1, fixture.server.requestCount)
            assertFalse(error.toString().contains(Sensitive))
        }
    }

    @Test fun AUTH_01_malformedLoginSuccessIsRejected() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            fixture.server.enqueue(ApiTestFixture.json("""{"unexpected":"$Sensitive"}""", 200))
            assertNotNull(runCatching { fixture.sessions.login(LoginRequest("qa@example.com", "synthetic password")) }.exceptionOrNull())
            assertEquals(1, fixture.server.requestCount)
            assertNull(fixture.store.saved)
        }
    }
}
