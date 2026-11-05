package com.pactogg.app.feature.contracts.list

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.connected.domain.BoardFilter
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.GroupId
import com.pactogg.app.testing.shared.Sensitive
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(SecurityChecks::class)
class ListChallengesSecurityTest {
    @Test fun CON_02_forbiddenBoardReadPreservesExistingSession() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"NOT_ALLOWED","message":"$Sensitive","request_id":"safe-request","details":{"token":"$Sensitive"}}""", 403))
            val failure = runCatching { fixture.social.contracts(GroupId, BoardFilter("open", true, "Halo", "Win"), null) }.exceptionOrNull()
            assertTrue(failure is ApiFailure)
            failure as ApiFailure
            assertEquals(403, failure.status)
            assertEquals("NOT_ALLOWED", failure.code)
            assertEquals("safe-request", failure.requestId)
            assertFalse(failure.toString().contains(Sensitive))
            assertFalse(failure.toString().contains(Access))
            assertNull(failure.cause)
            assertEquals(1, fixture.server.requestCount)
            val request = fixture.server.takeRequest()
            assertEquals("/api/v1/groups/$GroupId/contracts", request.path?.substringBefore('?'))
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun CON_02_serviceFailureDoesNotRetryListChallenges() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"STORAGE_UNAVAILABLE","message":"$Sensitive"}""", 503))
            val error = runCatching { fixture.social.contracts(GroupId, BoardFilter("open", true, "Halo", "Win"), null) }.exceptionOrNull() as ApiFailure
            assertEquals(503, error.status)
            assertFalse(error.toString().contains(Sensitive))
            assertEquals(1, fixture.server.requestCount)
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun CON_02_rateLimitDoesNotRetryListChallenges() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"RATE_LIMITED","message":"$Sensitive"}""", 429))
            val error = runCatching { fixture.social.contracts(GroupId, BoardFilter("open", true, "Halo", "Win"), null) }.exceptionOrNull() as ApiFailure
            assertEquals(429, error.status)
            assertEquals(1, fixture.server.requestCount)
            assertFalse(error.toString().contains(Sensitive))
        }
    }

    @Test fun CON_02_malformedListChallengesSuccessIsRejected() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"unexpected":"$Sensitive"}""", 200))
            assertNotNull(runCatching { fixture.social.contracts(GroupId, BoardFilter("open", true, "Halo", "Win"), null) }.exceptionOrNull())
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun CON_02_missingSessionPreventsListChallengesRequest() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            val result = runCatching { fixture.social.contracts(GroupId, BoardFilter("open", true, "Halo", "Win"), null) }
            assertEquals(401, (result.exceptionOrNull() as ApiFailure).status)
            assertEquals(0, fixture.server.requestCount)
            assertNull(fixture.store.saved)
        }
    }
}
