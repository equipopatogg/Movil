package com.pactogg.app.feature.contracts.accept
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

/** CON-06: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class AcceptChallengeApiTest {
    @Test fun CON_06_sendsVersionWhenAcceptingChallenge() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.ACCEPT)
            fixture.social.accept(ContractFixture)
            val request = fixture.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/contracts/$ContractId/accept", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertEquals(7, data?.get("version")?.jsonPrimitive?.int)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun CON_06_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(409 to "CANNOT_ACCEPT_OWN_CONTRACT", 402 to "INSUFFICIENT_POINTS", 403 to "NOT_CONTRACT_TARGET")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.social.accept(ContractFixture) }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }

}
