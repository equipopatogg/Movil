package com.pactogg.app.feature.contracts.create

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.GroupFixture
import com.pactogg.app.testing.shared.OtherId
import com.pactogg.app.testing.shared.Sensitive
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(SecurityChecks::class)
class CreateChallengeSecurityTest {
    @Test fun CON_01_forbiddenCreationPreservesExistingSession() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"NOT_ALLOWED","message":"$Sensitive","request_id":"safe-request","details":{"token":"$Sensitive"}}""", 403))
            val failure = runCatching { fixture.products.createChallenge(GroupFixture, ChallengeDraft("Halo", "Win a game", "Choose next game", "2099-11-10T23:59:00Z", 25, OtherId)) }.exceptionOrNull()
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
            assertEquals("/api/v1/contracts", request.path?.substringBefore('?'))
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun CON_01_serviceFailureDoesNotRetryCreateChallenge() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"STORAGE_UNAVAILABLE","message":"$Sensitive"}""", 503))
            val error = runCatching { fixture.products.createChallenge(GroupFixture, ChallengeDraft("Halo", "Win a game", "Choose next game", "2099-11-10T23:59:00Z", 25, OtherId)) }.exceptionOrNull() as ApiFailure
            assertEquals(503, error.status)
            assertFalse(error.toString().contains(Sensitive))
            assertEquals(1, fixture.server.requestCount)
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun CON_01_rateLimitDoesNotRetryCreateChallenge() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"RATE_LIMITED","message":"$Sensitive"}""", 429))
            val error = runCatching { fixture.products.createChallenge(GroupFixture, ChallengeDraft("Halo", "Win a game", "Choose next game", "2099-11-10T23:59:00Z", 25, OtherId)) }.exceptionOrNull() as ApiFailure
            assertEquals(429, error.status)
            assertEquals(1, fixture.server.requestCount)
            assertFalse(error.toString().contains(Sensitive))
        }
    }

    @Test fun CON_01_malformedCreateChallengeSuccessIsRejected() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"unexpected":"$Sensitive"}""", 201))
            assertNotNull(runCatching { fixture.products.createChallenge(GroupFixture, ChallengeDraft("Halo", "Win a game", "Choose next game", "2099-11-10T23:59:00Z", 25, OtherId)) }.exceptionOrNull())
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun CON_01_missingSessionPreventsCreateChallengeRequest() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            val result = runCatching { fixture.products.createChallenge(GroupFixture, ChallengeDraft("Halo", "Win a game", "Choose next game", "2099-11-10T23:59:00Z", 25, OtherId)) }
            assertEquals(401, (result.exceptionOrNull() as ApiFailure).status)
            assertEquals(0, fixture.server.requestCount)
            assertNull(fixture.store.saved)
        }
    }
}
