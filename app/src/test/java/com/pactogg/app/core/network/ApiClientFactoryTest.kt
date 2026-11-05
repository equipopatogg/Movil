package com.pactogg.app.core.network

import org.junit.experimental.categories.Category
import com.pactogg.app.testing.SecurityChecks
import kotlinx.serialization.Serializable
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.http.GET

@Category(SecurityChecks::class)
class ApiClientFactoryTest {
    @Test
    fun rejectsUnconfiguredOrUnsafeUrls() {
        listOf("", "http://localhost/", "https://user:pass@example.com/",
            "https://example.com/api", "https://example.com/?token=x", "https://example.com/#x")
            .forEach { assertFalse(it, ApiClientFactory.isValidBaseUrl(it)) }
        assertTrue(ApiClientFactory.isValidBaseUrl("https://example.com/api/v1/"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun cannotCreateCleartextClient() { ApiClientFactory().create("http://localhost/") }

    @Test
    fun parsesJsonOverTrustedTlsWithoutFollowingRedirects() {
        val certificate = HeldCertificate.Builder().commonName("localhost")
            .addSubjectAlternativeName("localhost").addSubjectAlternativeName("127.0.0.1")
            .addSubjectAlternativeName("::1").build()
        val serverCertificates = HandshakeCertificates.Builder().heldCertificate(certificate).build()
        val clientCertificates = HandshakeCertificates.Builder()
            .addTrustedCertificate(certificate.certificate).build()
        MockWebServer().use { server ->
            server.useHttps(serverCertificates.sslSocketFactory(), false)
            server.enqueue(MockResponse().setHeader("Content-Type", "application/json")
                .setBody("""{"status":"ok","future_field":true}"""))
            server.enqueue(MockResponse().setResponseCode(302).setHeader("Location", "/other"))
            val client = ApiClientFactory.defaultClient().newBuilder()
                .sslSocketFactory(clientCertificates.sslSocketFactory(), clientCertificates.trustManager).build()
            val api = ApiClientFactory().create(server.url("/").toString(), client).create(ProbeApi::class.java)
            assertEquals("ok", api.probe().execute().body()?.status)
            assertEquals("/probe", server.takeRequest().path)
            assertEquals(302, api.probe().execute().code())
            assertEquals(2, server.requestCount)
        }
    }

    interface ProbeApi { @GET("probe") fun probe(): Call<ProbeDto> }
    @Serializable data class ProbeDto(val status: String)
}
