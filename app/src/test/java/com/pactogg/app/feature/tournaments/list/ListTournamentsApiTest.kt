package com.pactogg.app.feature.tournaments.list
import com.pactogg.app.testing.shared.*

import com.pactogg.app.core.network.ApiFailure



import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** TOR-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class ListTournamentsApiTest {
    @Test fun TOR_02_sendsGameFilterForTournamentListing() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.TOURNAMENTS)
            fixture.products.tournaments("open", "Halo", null)
            val request = fixture.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/tournaments", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals("Halo", request.requestUrl?.queryParameter("game"))
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun TOR_02_nonemptyBracketPreservesRoundsParticipantsAndScoresWithoutWrites() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            val match = """{"id":"$ContractId","round":2,"position":1,"p1_group_id":"$GroupId","p2_group_id":null,"p1_score":3,"p2_score":null,"status":"in_progress"}"""
            val tournament = """{"id":"$OtherId","game":"Halo","format":"single_elimination","status":"in_progress","scheduled_at":"2099-11-10T00:00:00Z","bracket":{"matches":[$match]}}"""
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.page(tournament)))
            val item = f.products.tournaments("in_progress", "Halo", null).items.single()
            val actual = item.matches.single()
            assertEquals(2, actual.round); assertEquals(1, actual.position)
            assertEquals(GroupId, actual.first); assertNull(actual.second)
            assertEquals(3, actual.firstScore); assertNull(actual.secondScore)
            assertEquals("in_progress", actual.status)
            assertEquals("GET", f.server.takeRequest().method); assertEquals(1, f.server.requestCount)
        }
    }


    @Test fun TOR_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(422 to "VALIDATION_ERROR")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.products.tournaments(null, null, null) }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }


    @Test fun TOR_02_lostResponseIsNotReplayedOrReportedAsSuccess() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(true); val before = f.store.saved
            f.server.enqueue(okhttp3.mockwebserver.MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST))
            assertTrue(runCatching { f.products.tournaments(null, null, null) }.exceptionOrNull() is java.io.IOException)
            assertEquals(before, f.store.saved); assertEquals(1, f.server.requestCount)
        }
    }

}
