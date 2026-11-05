package com.pactogg.app.feature.evidence.send
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

/** EVI-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class SendEvidenceApiTest {
    @Test fun EVI_02_sendsAllowedEvidenceLink() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.EVIDENCE)
            fixture.products.sendLink(ContractFixture, "https://youtu.be/example")
            val request = fixture.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/contracts/$ContractId/evidence", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertEquals("https://youtu.be/example", data?.get("url")?.jsonPrimitive?.content)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun EVI_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(422 to "CONTENT_REJECTED", 409 to "EVIDENCE_LIMIT_REACHED")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.products.sendLink(ContractFixture, "https://youtu.be/synthetic") }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }


    @Test fun EVI_02_lostResponseIsNotReplayedOrReportedAsSuccess() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(true); val before = f.store.saved
            f.server.enqueue(okhttp3.mockwebserver.MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST))
            assertTrue(runCatching { f.products.sendLink(ContractFixture, "https://youtu.be/synthetic") }.exceptionOrNull() is java.io.IOException)
            assertEquals(before, f.store.saved); assertEquals(1, f.server.requestCount)
        }
    }

}
