package com.pactogg.app.feature.contracts.detail
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure


import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.ContractFixture
import com.pactogg.app.testing.shared.ContractId
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-03: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class ChallengeDetailsApiTest {
    @Test fun CON_03_mapsChallengeVersion() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.CONTRACT)
            val result = fixture.products.challenge(ContractFixture)
            val request = fixture.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/contracts/$ContractId", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(7, result.version)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun CON_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(404 to "NOT_FOUND")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.products.challenge(ContractFixture) }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }

}
