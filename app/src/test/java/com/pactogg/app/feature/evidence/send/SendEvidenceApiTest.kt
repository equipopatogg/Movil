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


    @Test fun EVI_02_photoHasExplicitMultipartTypeMimeFilenameAndResponseMetadata() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(true)
            val data = """{"id":"$OtherId","contract_id":"$ContractId","type":"photo","url":"https://cdn.example/photo.jpg","created_at":"2026-11-05T00:00:00Z","expires_at":"2026-12-05T00:00:00Z","moderation":{"status":"approved"}}"""
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope(data), 201))
            val evidence = f.products.sendPhoto(ContractFixture, byteArrayOf(1, 2, 3), "image/jpeg")
            val request = f.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/contracts/$ContractId/evidence", request.path)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertTrue(request.getHeader("Content-Type")!!.startsWith("multipart/form-data; boundary="))
            val body = request.body.readUtf8()
            assertTrue(body.contains("name=\"type\"")); assertTrue(body.contains("text/plain"))
            assertTrue(body.contains("photo")); assertTrue(body.contains("name=\"file\"; filename=\"evidencia.jpg\""))
            assertTrue(body.contains("Content-Type: image/jpeg"))
            assertEquals("https://cdn.example/photo.jpg", evidence.url)
            assertEquals("2026-12-05T00:00:00Z", evidence.expiresAt)
            assertEquals("2026-11-05T00:00:00Z", evidence.createdAt)
        }
    }

    @Test fun EVI_02_listsSavedEvidenceWithSessionAndRejectsOtherContract() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(true)
            val data = """{"id":"$OtherId","contract_id":"$ContractId","type":"photo","url":"https://cdn.example/photo.jpg","moderation":{"status":"approved"}}"""
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("[$data]")))
            assertEquals("https://cdn.example/photo.jpg", f.products.evidence(ContractFixture).single().url)
            val request = f.server.takeRequest()
            assertEquals("GET", request.method); assertEquals("/api/v1/contracts/$ContractId/evidence", request.path)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("[" + data.replace(ContractId, UserId) + "]")))
            assertNotNull(runCatching { f.products.evidence(ContractFixture) }.exceptionOrNull())
        }
    }

    @Test fun EVI_02_acknowledgedPhotoWithNoUrlRemainsSuccessfulForFollowupGet() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(true)
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("""{"id":"$OtherId","contract_id":"$ContractId","type":"photo","url":null,"moderation":{"status":"approved"}}"""), 201))
            assertNull(f.products.sendPhoto(ContractFixture, byteArrayOf(1), "image/png").url)
            assertEquals(1, f.server.requestCount)
        }
    }

    @Test fun EVI_02_photoCannotBeConfirmedByALinkResponse() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(true); f.enqueueSuccess(Endpoint.EVIDENCE)
            assertEquals("INVALID_RESPONSE", (runCatching { f.products.sendPhoto(ContractFixture, byteArrayOf(1), "image/jpeg") }
                .exceptionOrNull() as ApiFailure).code)
            assertEquals(1, f.server.requestCount)
        }
    }
}
