package com.pactogg.app.feature.contracts.reject
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure



import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.ContractFixture
import com.pactogg.app.testing.shared.ContractId
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-07: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class RejectChallengeApiTest {
    @Test fun CON_07_sendsVersionWhenRejectingChallenge() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.REJECT)
            fixture.social.reject(ContractFixture)
            val request = fixture.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/contracts/$ContractId/reject", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertEquals(7, data?.get("version")?.jsonPrimitive?.int)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun CON_07_rejectReadThenAcceptConflictNeverProducesSuccess() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); val before = f.store.saved
            f.enqueueSuccess(Endpoint.REJECT)
            val rejected = f.social.reject(ContractFixture)
            assertEquals("cancelled", rejected.status); assertEquals("rejected", rejected.closedReason)
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope(ApiTestFixture.contractJson("cancelled", ",\"closed_reason\":\"rejected\""))))
            val fresh = f.products.challenge(rejected)
            assertEquals("cancelled", fresh.status)
            f.server.enqueue(ApiTestFixture.json("""{"code":"INVALID_CONTRACT_STATE"}""", 409))
            val failed = runCatching { f.social.accept(fresh) }.exceptionOrNull() as ApiFailure
            assertEquals(409, failed.status); assertEquals(before, f.store.saved)
            assertEquals("/api/v1/contracts/$ContractId/reject", f.server.takeRequest().path)
            assertEquals("/api/v1/contracts/$ContractId", f.server.takeRequest().path)
            assertEquals("/api/v1/contracts/$ContractId/accept", f.server.takeRequest().path)
            assertEquals(3, f.server.requestCount)
        }
    }


    @Test fun CON_07_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(409 to "INVALID_CONTRACT_STATE", 403 to "NOT_CONTRACT_TARGET")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.social.reject(ContractFixture) }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }

}
