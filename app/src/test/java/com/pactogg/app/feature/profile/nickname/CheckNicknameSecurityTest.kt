package com.pactogg.app.feature.profile.nickname

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Sensitive
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** USR-06: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(SecurityChecks::class)
class CheckNicknameSecurityTest {
    @Test fun USR_06_publicRateLimitDoesNotEraseExistingSession() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"RATE_LIMITED","message":"$Sensitive","request_id":"safe-request","details":{"token":"$Sensitive"}}""", 429))
            val failure = runCatching { fixture.social.nickname("AvailableName") }.exceptionOrNull()
            assertTrue(failure is ApiFailure)
            failure as ApiFailure
            assertEquals(429, failure.status)
            assertEquals("RATE_LIMITED", failure.code)
            assertEquals("safe-request", failure.requestId)
            assertFalse(failure.toString().contains(Sensitive))
            assertFalse(failure.toString().contains(Access))
            assertNull(failure.cause)
            assertEquals(1, fixture.server.requestCount)
            val request = fixture.server.takeRequest()
            assertEquals("/api/v1/users/check-nickname", request.path?.substringBefore('?'))
            assertNull(request.getHeader("Authorization"))
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun USR_06_serviceFailureDoesNotRetryCheckNickname() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"STORAGE_UNAVAILABLE","message":"$Sensitive"}""", 503))
            val error = runCatching { fixture.social.nickname("AvailableName") }.exceptionOrNull() as ApiFailure
            assertEquals(503, error.status)
            assertFalse(error.toString().contains(Sensitive))
            assertEquals(1, fixture.server.requestCount)
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun USR_06_rateLimitDoesNotRetryCheckNickname() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"RATE_LIMITED","message":"$Sensitive"}""", 429))
            val error = runCatching { fixture.social.nickname("AvailableName") }.exceptionOrNull() as ApiFailure
            assertEquals(429, error.status)
            assertEquals(1, fixture.server.requestCount)
            assertFalse(error.toString().contains(Sensitive))
        }
    }

    @Test fun USR_06_malformedCheckNicknameSuccessIsRejected() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"unexpected":"$Sensitive"}""", 200))
            assertNotNull(runCatching { fixture.social.nickname("AvailableName") }.exceptionOrNull())
            assertEquals(1, fixture.server.requestCount)
        }
    }
}
