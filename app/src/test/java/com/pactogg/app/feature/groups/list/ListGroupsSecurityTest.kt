package com.pactogg.app.feature.groups.list

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Sensitive
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** GRP-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(SecurityChecks::class)
class ListGroupsSecurityTest {
    @Test fun GRP_02_forbiddenListingPreservesExistingSession() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"NOT_ALLOWED","message":"$Sensitive","request_id":"safe-request","details":{"token":"$Sensitive"}}""", 403))
            val failure = runCatching { fixture.social.groups("Equipo", null) }.exceptionOrNull()
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
            assertEquals("/api/v1/groups", request.path?.substringBefore('?'))
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun GRP_02_serviceFailureDoesNotRetryListGroups() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"STORAGE_UNAVAILABLE","message":"$Sensitive"}""", 503))
            val error = runCatching { fixture.social.groups("Equipo", null) }.exceptionOrNull() as ApiFailure
            assertEquals(503, error.status)
            assertFalse(error.toString().contains(Sensitive))
            assertEquals(1, fixture.server.requestCount)
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun GRP_02_rateLimitDoesNotRetryListGroups() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"RATE_LIMITED","message":"$Sensitive"}""", 429))
            val error = runCatching { fixture.social.groups("Equipo", null) }.exceptionOrNull() as ApiFailure
            assertEquals(429, error.status)
            assertEquals(1, fixture.server.requestCount)
            assertFalse(error.toString().contains(Sensitive))
        }
    }

    @Test fun GRP_02_malformedListGroupsSuccessIsRejected() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"unexpected":"$Sensitive"}""", 200))
            assertNotNull(runCatching { fixture.social.groups("Equipo", null) }.exceptionOrNull())
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun GRP_02_missingSessionPreventsListGroupsRequest() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            val result = runCatching { fixture.social.groups("Equipo", null) }
            assertEquals(401, (result.exceptionOrNull() as ApiFailure).status)
            assertEquals(0, fixture.server.requestCount)
            assertNull(fixture.store.saved)
        }
    }
}
