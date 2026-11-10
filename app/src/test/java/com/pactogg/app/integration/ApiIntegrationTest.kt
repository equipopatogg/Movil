package com.pactogg.app.integration


import org.junit.experimental.categories.Category
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.core.network.ApiClientFactory
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.data.LoginRequest
import com.pactogg.app.feature.auth.data.RegisterRequest
import com.pactogg.app.feature.auth.domain.Account
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.auth.domain.SessionStore
import com.pactogg.app.feature.connected.data.ApiProductRepository
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@Category(IntegrationChecks::class)
class ApiIntegrationTest {
    private val server = MockWebServer()
    private lateinit var api: PactoApi
    private val store = MemoryStore()
    private lateinit var sessions: SessionManager
    private lateinit var products: ApiProductRepository
    private var nowMillis = 1000L

    @Before fun setup() {
        val certificate = HeldCertificate.Builder().commonName("localhost").addSubjectAlternativeName("localhost")
            .addSubjectAlternativeName("127.0.0.1").addSubjectAlternativeName("::1").build()
        server.useHttps(HandshakeCertificates.Builder().heldCertificate(certificate).build().sslSocketFactory(), false)
        server.start(java.net.InetAddress.getByName("127.0.0.1"), 0)
        val trusted = HandshakeCertificates.Builder().addTrustedCertificate(certificate.certificate).build()
        val client = OkHttpClient.Builder().sslSocketFactory(trusted.sslSocketFactory(), trusted.trustManager)
            .retryOnConnectionFailure(false).followRedirects(false).build()
        api = ApiClientFactory().create(server.url("/").newBuilder().host("127.0.0.1").build().toString(), client).create(PactoApi::class.java)
        sessions = SessionManager(api, store) { nowMillis }
        products = ApiProductRepository(api, sessions)
    }

    @After fun cleanup() { server.shutdown() }

    @Test fun registrationGroupContractEvidenceTournamentsAndLogoutUseRealWireContract() = runBlocking {
        server.enqueue(json(sessionJson(), 201))
        sessions.register(RegisterRequest("sample@example.com", "synthetic password", "Player", true, "2026-10-01"))
        val registration = server.takeRequest()
        assertEquals("/api/v1/auth/register", registration.path)
        assertTrue(registration.body.readUtf8().contains("\"accept_privacy\":true"))
        assertNull(registration.getHeader("Authorization"))

        server.enqueue(json("""{"data":{"id":"$GroupId","name":"Equipo","invite_code":"ABCDEF","my_role":"admin","my_points":100,"settings":{"bond_options":[25,50]}}}""", 201))
        val group = products.createGroup("Equipo")
        assertEquals(100, group.points)
        val groupRequest = server.takeRequest()
        assertEquals("Bearer access-one", groupRequest.getHeader("Authorization"))
        assertEquals("{\"name\":\"Equipo\"}", groupRequest.body.readUtf8())

        server.enqueue(json("""{"data":{"id":"$ContractId","group_id":"$GroupId","game":"Valorant","condition":"Ganar una partida","reward":"Elegir el juego","deadline":"2099-11-10T23:59:00Z","bond_required":25,"status":"open"}}""", 201))
        val challenge = products.createChallenge(group, ChallengeDraft("Valorant", "Ganar una partida",
            "Elegir el juego", "2099-11-10T23:59:00Z", 25))
        val request = server.takeRequest()
        assertEquals("/api/v1/contracts", request.path)
        assertTrue(request.body.readUtf8().contains("\"bond_required\":25"))

        server.enqueue(json(evidenceJson("link"), 201))
        assertEquals("approved", products.sendLink(challenge, "https://youtu.be/example").moderation)
        assertEquals("/api/v1/contracts/$ContractId/evidence", server.takeRequest().path)

        server.enqueue(json(evidenceJson("photo"), 201))
        products.sendPhoto(challenge, byteArrayOf(1, 2, 3), "image/png")
        val photo = server.takeRequest()
        assertTrue(photo.getHeader("Content-Type").orEmpty().startsWith("multipart/form-data"))
        val multipart = photo.body.readUtf8()
        assertTrue(multipart.contains("name=\"file\""))
        assertTrue(multipart.contains("name=\"type\""))

        server.enqueue(json("""{"data":[],"meta":{"has_more":false,"next_cursor":null}}"""))
        assertTrue(products.tournaments("open", "Halo", null).items.isEmpty())
        assertEquals("/api/v1/tournaments?status=open&game=Halo&limit=20", server.takeRequest().path)

        server.enqueue(MockResponse().setResponseCode(204))
        sessions.logout()
        assertTrue(server.takeRequest().body.readUtf8().contains("\"refresh_token\":\"refresh-one\""))
        assertNull(sessions.current.value)
        assertNull(store.saved)
    }

    @Test fun challengeRefreshUsesAuthenticatedGetAndRejectsAnotherContract() = runBlocking {
        login()
        val challenge = com.pactogg.app.feature.connected.domain.Challenge(ContractId, GroupId,
            "Halo", "Ganar", "Elegir juego", "2099-11-10T23:59:00Z", 25, "open")
        val body = """{"data":{"id":"$ContractId","group_id":"$GroupId","game":"Halo","condition":"Ganar","reward":"Elegir juego","deadline":"2099-11-10T23:59:00Z","bond_required":25,"status":"voting"}}"""
        server.enqueue(json(body))
        assertEquals("voting", products.challenge(challenge).status)
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/api/v1/contracts/$ContractId", request.path)
        assertEquals("Bearer access-one", request.getHeader("Authorization"))
        server.enqueue(json(body.replace(ContractId, "555555555555555555555555")))
        assertEquals("INVALID_RESPONSE", (runCatching { products.challenge(challenge) }
            .exceptionOrNull() as ApiFailure).code)
        server.takeRequest()
        server.enqueue(json(body.replace(GroupId, "666666666666666666666666")))
        assertEquals("INVALID_RESPONSE", (runCatching { products.challenge(challenge) }
            .exceptionOrNull() as ApiFailure).code)
    }

    @Test fun concurrentExpiredRequestsRotateOnlyOnceAndUseFreshBearer() = runBlocking {
        store.saved = Session(Account(UserId, "sample@example.com", "Player"), "expired", "old-refresh", 50000)
        sessions.restore()
        nowMillis = 100000L
        server.enqueue(json(sessionJson("access-new", "refresh-new")))
        server.enqueue(json("""{"data":[],"meta":{"has_more":false}}"""))
        server.enqueue(json("""{"data":[],"meta":{"has_more":false}}"""))
        val first = async { products.tournaments(null, null, null) }
        val second = async { products.tournaments(null, null, null) }
        first.await(); second.await()
        assertEquals("/api/v1/auth/refresh", server.takeRequest().path)
        repeat(2) { assertEquals("Bearer access-new", server.takeRequest().getHeader("Authorization")) }
        assertEquals("refresh-new", store.saved?.refreshToken)
    }

    @Test fun forbiddenDoesNotRefreshOrEraseSessionAndHandlesArrayDetails() = runBlocking {
        login()
        server.enqueue(json("""{"code":"NOT_GROUP_MEMBER","message":"private","request_id":"qa-request-1","details":[{"field":"name"}]}""", 403))
        val failure = runCatching { products.createGroup("Equipo") }.exceptionOrNull() as ApiFailure
        assertEquals(403, failure.status)
        assertEquals("NOT_GROUP_MEMBER", failure.code)
        assertEquals("qa-request-1", failure.requestId)
        assertEquals(2, server.requestCount)
        assertTrue(sessions.current.value != null)
        assertFalse(failure.toString().contains("private"))
    }

    @Test fun unauthorizedWriteRenewsButNeverReplaysPost() = runBlocking {
        login()
        server.enqueue(json("""{"code":"UNAUTHORIZED"}""", 401))
        server.enqueue(json(sessionJson("access-new", "refresh-new")))
        val failure = runCatching { products.createGroup("Equipo") }.exceptionOrNull() as ApiFailure
        assertEquals("SESSION_RENEWED", failure.code)
        assertEquals(3, server.requestCount)
        assertEquals("refresh-new", sessions.current.value?.refreshToken)
    }

    @Test fun failedRotationClearsTokensAndDoesNotLoop() = runBlocking {
        store.saved = Session(Account(UserId, "sample@example.com", "Player"), "expired", "old-refresh", 50000)
        sessions.restore()
        nowMillis = 100000L
        server.enqueue(json("""{"code":"UNAUTHORIZED"}""", 401))
        assertTrue(runCatching { products.tournaments(null, null, null) }.isFailure)
        assertNull(sessions.current.value)
        assertNull(store.saved)
        assertEquals(1, server.requestCount)
    }

    @Test fun lateBusinessResponseAfterLogoutCannotBeAccepted() = runBlocking {
        login()
        val started = CompletableDeferred<Unit>()
        val finish = CompletableDeferred<Unit>()
        val inFlight = async {
            runCatching { sessions.authorized { started.complete(Unit); finish.await(); Response.success(Unit) } }
        }
        started.await()
        server.enqueue(MockResponse().setResponseCode(204))
        sessions.logout()
        finish.complete(Unit)
        assertEquals("SESSION_CHANGED", (inFlight.await().exceptionOrNull() as ApiFailure).code)
        assertNull(sessions.current.value)
    }

    @Test fun incompleteSessionAndStorageFailureNeverAuthenticate() = runBlocking {
        server.enqueue(json("""{"data":{"user":{"id":"$UserId","email":"sample@example.com","nickname":"Player"},"access_token":"access"}}"""))
        assertTrue(runCatching { sessions.login(LoginRequest("sample@example.com", "synthetic")) }.isFailure)
        assertNull(sessions.current.value)
        server.enqueue(json(sessionJson()))
        store.failWrite = true
        assertTrue(runCatching { sessions.login(LoginRequest("sample@example.com", "synthetic")) }.isFailure)
        assertNull(sessions.current.value)
    }

    @Test fun logoutNetworkFailureStillClearsLocalSession() = runBlocking {
        login()
        server.enqueue(json("""{"code":"INTERNAL_ERROR"}""", 503))
        assertTrue(runCatching { sessions.logout() }.isFailure)
        assertNull(sessions.current.value)
        assertNull(store.saved)
    }

    @Test fun malformedPaginationIsNotReportedAsEmptySuccess() = runBlocking {
        login()
        server.enqueue(json("""{"data":[],"meta":{"has_more":true}}"""))
        assertEquals("INVALID_RESPONSE", (runCatching { products.tournaments(null, null, null) }
            .exceptionOrNull() as ApiFailure).code)
    }

    @Test fun unauthorizedReadRefreshesAndRetriesOnlyOnce() = runBlocking {
        login()
        server.enqueue(json("""{"code":"UNAUTHORIZED"}""", 401))
        server.enqueue(json(sessionJson("access-new", "refresh-new")))
        server.enqueue(json("""{"code":"UNAUTHORIZED"}""", 401))
        assertTrue(runCatching { products.tournaments(null, null, null) }.isFailure)
        assertEquals(4, server.requestCount)
        assertEquals("Bearer access-one", server.takeRequest().getHeader("Authorization"))
        assertEquals("/api/v1/auth/refresh", server.takeRequest().path)
        assertEquals("Bearer access-new", server.takeRequest().getHeader("Authorization"))
        assertNull(store.saved)
        assertNull(sessions.current.value)
    }

    @Test fun restoredExpiredSessionRenewsBeforeReturningToTheUi() = runBlocking {
        store.saved = Session(Account(UserId, "sample@example.com", "Player"), "expired", "old-refresh", 1)
        server.enqueue(json(sessionJson("access-new", "refresh-new")))
        sessions.restore()
        assertEquals("access-new", sessions.current.value?.accessToken)
        assertEquals("/api/v1/auth/refresh", server.takeRequest().path)
    }

    @Test fun protectedRequestDoesNotFollowRedirectsOrForwardBearer() = runBlocking {
        login()
        server.enqueue(MockResponse().setResponseCode(302).setHeader("Location", server.url("/other")))
        assertTrue(runCatching { products.tournaments(null, null, null) }.isFailure)
        assertEquals(2, server.requestCount)
        assertTrue(sessions.current.value != null)
    }

    private suspend fun login() {
        server.enqueue(json(sessionJson()))
        sessions.login(LoginRequest("sample@example.com", "synthetic"))
        server.takeRequest()
    }

    @Test fun lateResponseFromAccountACannotReplaceAccountB() = runBlocking {
        login()
        val started = CompletableDeferred<Unit>()
        val finish = CompletableDeferred<Unit>()
        val oldRead = async {
            runCatching { sessions.authorized(readOnly = true) {
                assertEquals("Bearer access-one", it)
                started.complete(Unit)
                finish.await()
                Response.success(Unit)
            } }
        }
        started.await()
        val secondId = "555555555555555555555555"
        server.enqueue(json(sessionJson("access-two", "refresh-two").replace(UserId, secondId)))
        sessions.login(LoginRequest("second@example.com", "synthetic"))
        finish.complete(Unit)
        assertEquals("SESSION_CHANGED", (oldRead.await().exceptionOrNull() as ApiFailure).code)
        assertEquals(secondId, sessions.current.value?.account?.id)
        assertEquals("access-two", store.saved?.accessToken)
    }

    @Test fun pendingLoginCannotUndoLogoutOrOverwriteNextAccount() = runBlocking {
        val arrived = java.util.concurrent.CountDownLatch(1)
        val release = java.util.concurrent.CountDownLatch(1)
        val secondId = "555555555555555555555555"
        server.dispatcher = object : okhttp3.mockwebserver.Dispatcher() {
            override fun dispatch(request: okhttp3.mockwebserver.RecordedRequest): MockResponse {
                return if (request.body.readUtf8().contains("first@example.com")) {
                    arrived.countDown()
                    check(release.await(10, java.util.concurrent.TimeUnit.SECONDS))
                    json(sessionJson())
                } else json(sessionJson("access-two", "refresh-two").replace(UserId, secondId))
            }
        }
        val pending = async(start = kotlinx.coroutines.CoroutineStart.UNDISPATCHED) {
            runCatching { sessions.login(LoginRequest("first@example.com", "synthetic")) }
        }
        try {
            assertTrue(kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                arrived.await(5, java.util.concurrent.TimeUnit.SECONDS)
            })
            sessions.logout()
            sessions.login(LoginRequest("second@example.com", "synthetic"))
        } finally { release.countDown() }
        assertEquals("SESSION_CHANGED", (pending.await().exceptionOrNull() as ApiFailure).code)
        assertEquals(secondId, sessions.current.value?.account?.id)
        assertEquals("refresh-two", store.saved?.refreshToken)
    }

    private fun json(body: String, status: Int = 200) = MockResponse().setResponseCode(status)
        .setHeader("Content-Type", "application/json").setBody(body)

    private fun sessionJson(access: String = "access-one", refresh: String = "refresh-one") =
        """{"data":{"user":{"id":"$UserId","email":"sample@example.com","nickname":"Player"},"access_token":"$access","refresh_token":"$refresh","token_type":"Bearer","expires_in":900}}"""

    private fun evidenceJson(type: String) =
        """{"data":{"id":"$EvidenceId","contract_id":"$ContractId","type":"$type","moderation":{"status":"approved"}}}"""

    private class MemoryStore : SessionStore {
        var saved: Session? = null
        var failWrite = false
        override fun read() = saved
        override fun write(session: Session) {
            check(!failWrite)
            saved = session
        }
        override fun clear() { saved = null }
    }

    companion object {
        private const val UserId = "111111111111111111111111"
        private const val GroupId = "222222222222222222222222"
        private const val ContractId = "333333333333333333333333"
        private const val EvidenceId = "444444444444444444444444"
    }

    @Test fun rotatingTokenPersistenceFailureClearsOldAndFreshIdentityAtomically() = runBlocking {
        login(); nowMillis = 2000000L; store.failWrite = true
        server.enqueue(json(sessionJson("new-access", "new-refresh")))
        assertTrue(runCatching { sessions.restore() }.isFailure)
        assertNull(sessions.current.value); assertNull(store.saved)
        assertEquals(2, server.requestCount)
    }

}
