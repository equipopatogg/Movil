package com.pactogg.app.feature.contracts.list
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure


import com.pactogg.app.feature.connected.domain.BoardFilter
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import com.pactogg.app.testing.shared.GroupId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class ListChallengesApiTest {
    @Test fun CON_02_sendsOwnChallengesAndStatusFilters() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.CONTRACTS)
            fixture.social.contracts(GroupId, BoardFilter("open", true, "Halo", "Win"), null)
            val request = fixture.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/groups/$GroupId/contracts", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals("true", request.requestUrl?.queryParameter("mine"))
            assertEquals("open", request.requestUrl?.queryParameter("status"))
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun CON_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(422 to "VALIDATION_ERROR")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.social.contracts(GroupId, com.pactogg.app.feature.connected.domain.BoardFilter(), null) }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }

}
