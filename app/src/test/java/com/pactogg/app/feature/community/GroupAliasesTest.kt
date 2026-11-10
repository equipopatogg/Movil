package com.pactogg.app.feature.community

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(IntegrationChecks::class)
class GroupAliasesTest {
    private val draft = ChallengeDraft("Halo", "Win a game", "Choose next game",
        "2099-11-10T23:59:00Z", 25)

    @Test fun CON_01_groupAliasUsesSelectedGroupAndBearer() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.enqueueSuccess(Endpoint.CREATE_CONTRACT)
            assertEquals(GroupId, f.products.createChallengeInGroup(GroupFixture, draft).groupId)
            val request = f.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/groups/$GroupId/contracts", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }

    @Test fun CON_01_aliasFailureDoesNotReplayOnCanonicalRoute() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"code":"MODERATION_UNAVAILABLE"}""", 503))
            val error = runCatching { f.products.createChallengeInGroup(GroupFixture, draft) }.exceptionOrNull()
            assertEquals(503, (error as ApiFailure).status)
            assertEquals(1, f.server.requestCount)
        }
    }

    @Test fun TOR_02_groupAliasPreservesFiltersAndCursor() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.enqueueSuccess(Endpoint.TOURNAMENTS)
            f.products.groupTournaments(GroupId, "open", "Halo", "cursor")
            val request = f.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/groups/$GroupId/tournaments", request.requestUrl?.encodedPath)
            assertEquals("open", request.requestUrl?.queryParameter("status"))
            assertEquals("Halo", request.requestUrl?.queryParameter("game"))
            assertEquals("cursor", request.requestUrl?.queryParameter("cursor"))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
        }
    }

    @Test fun TOR_02_invalidGroupCannotSendAnAliasRequest() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            assertTrue(runCatching { f.products.groupTournaments("../other", null, null, null) }
                .exceptionOrNull() is IllegalArgumentException)
            assertEquals(0, f.server.requestCount)
        }
    }
}
