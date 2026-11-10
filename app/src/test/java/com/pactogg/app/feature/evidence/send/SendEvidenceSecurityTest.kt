package com.pactogg.app.feature.evidence.send

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.ContractFixture
import com.pactogg.app.testing.shared.ContractId
import com.pactogg.app.testing.shared.Sensitive
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** EVI-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(SecurityChecks::class)
class SendEvidenceSecurityTest {
    @Test fun EVI_02_forbiddenEvidencePreservesExistingSession() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"NOT_ALLOWED","message":"$Sensitive","request_id":"safe-request","details":{"token":"$Sensitive"}}""", 403))
            val failure = runCatching { fixture.products.sendLink(ContractFixture, "https://youtu.be/example") }.exceptionOrNull()
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
            assertEquals("/api/v1/contracts/$ContractId/evidence", request.path?.substringBefore('?'))
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun EVI_02_serviceFailureDoesNotRetrySendEvidence() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"STORAGE_UNAVAILABLE","message":"$Sensitive"}""", 503))
            val error = runCatching { fixture.products.sendLink(ContractFixture, "https://youtu.be/example") }.exceptionOrNull() as ApiFailure
            assertEquals(503, error.status)
            assertFalse(error.toString().contains(Sensitive))
            assertEquals(1, fixture.server.requestCount)
            assertNotNull(fixture.store.saved)
        }
    }

    @Test fun EVI_02_rateLimitDoesNotRetrySendEvidence() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"code":"RATE_LIMITED","message":"$Sensitive"}""", 429))
            val error = runCatching { fixture.products.sendLink(ContractFixture, "https://youtu.be/example") }.exceptionOrNull() as ApiFailure
            assertEquals(429, error.status)
            assertEquals(1, fixture.server.requestCount)
            assertFalse(error.toString().contains(Sensitive))
        }
    }

    @Test fun EVI_02_malformedSendEvidenceSuccessIsRejected() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.server.enqueue(ApiTestFixture.json("""{"unexpected":"$Sensitive"}""", 201))
            assertNotNull(runCatching { fixture.products.sendLink(ContractFixture, "https://youtu.be/example") }.exceptionOrNull())
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun EVI_02_missingSessionPreventsSendEvidenceRequest() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(false)
            val result = runCatching { fixture.products.sendLink(ContractFixture, "https://youtu.be/example") }
            assertEquals(401, (result.exceptionOrNull() as ApiFailure).status)
            assertEquals(0, fixture.server.requestCount)
            assertNull(fixture.store.saved)
        }
    }

    @Test fun EVI_02_photoErrorsNeverRetryOrApproveAndKeepSession() = runBlocking {
        for ((status, code) in listOf(422 to "IMAGE_TOO_LARGE", 422 to "UNSUPPORTED_MEDIA_TYPE",
            422 to "CONTENT_REJECTED", 403 to "NOT_CONTRACT_TARGET", 409 to "INVALID_CONTRACT_STATE")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"No se aceptan más evidencias.","details":{"labels":["private-label"]}}""", status))
                val error = runCatching { f.products.sendPhoto(ContractFixture, byteArrayOf(1), "image/jpeg") }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertEquals(before, f.store.saved); assertEquals(1, f.server.requestCount)
                assertFalse(error.toString().contains("private-label"))
                if (status == 409) assertEquals("No se aceptan más evidencias.", error.displayMessage)
            }
        }
    }

    @Test fun EVI_02_evidenceListWithoutSessionMakesNoRequest() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(false)
            assertEquals(401, (runCatching { f.products.evidence(ContractFixture) }.exceptionOrNull() as ApiFailure).status)
            assertEquals(0, f.server.requestCount)
        }
    }

    @Test fun EVI_02_conflictDoesNotExposeUnsafeServerText() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(true)
            f.server.enqueue(ApiTestFixture.json("""{"code":"INVALID_CONTRACT_STATE","message":"Bearer private-token"}""", 409))
            val error = runCatching { f.products.sendLink(ContractFixture, "https://youtu.be/test") }.exceptionOrNull() as ApiFailure
            assertNull(error.displayMessage); assertFalse(error.toString().contains("private-token"))
        }
    }

    @Test fun EVI_02_imageClientHasNoSessionOrRedirectsAndRejectsCleartext() {
        val client = com.pactogg.app.core.network.EvidenceImageClient.create()
        assertFalse(client.followRedirects); assertFalse(client.followSslRedirects)
        assertTrue(client.authenticator === okhttp3.Authenticator.NONE)
        assertTrue(client.cookieJar === okhttp3.CookieJar.NO_COOKIES)
        assertNotNull(runCatching { client.newCall(okhttp3.Request.Builder().url("http://127.0.0.1:9/image.jpg").build()).execute() }.exceptionOrNull())
    }

    @Test fun EVI_02_cdnRequestStripsAuthorizationAndNeverFollowsRedirect() {
        val certificate = okhttp3.tls.HeldCertificate.Builder().commonName("localhost")
            .addSubjectAlternativeName("localhost").addSubjectAlternativeName("127.0.0.1")
            .addSubjectAlternativeName("::1").build()
        val trust = okhttp3.tls.HandshakeCertificates.Builder().addTrustedCertificate(certificate.certificate).build()
        okhttp3.mockwebserver.MockWebServer().use { server ->
            server.useHttps(okhttp3.tls.HandshakeCertificates.Builder().heldCertificate(certificate).build().sslSocketFactory(), false)
            server.start(java.net.InetAddress.getByName("127.0.0.1"), 0)
            server.enqueue(okhttp3.mockwebserver.MockResponse().setResponseCode(302).setHeader("Location", "https://other.example/photo"))
            val client = com.pactogg.app.core.network.EvidenceImageClient.create().newBuilder()
                .sslSocketFactory(trust.sslSocketFactory(), trust.trustManager).build()
            assertFalse(client.retryOnConnectionFailure)
            client.newCall(okhttp3.Request.Builder().url(server.url("/photo.jpg").newBuilder().host("127.0.0.1").build())
                .header("Authorization", "Bearer synthetic-should-not-be-sent").build()).execute().use {
                assertEquals(302, it.code)
            }
            assertNull(server.takeRequest().getHeader("Authorization"))
            assertEquals(1, server.requestCount)
        }
    }
}
