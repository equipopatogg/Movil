package com.pactogg.app.integration

import org.junit.experimental.categories.Category
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.core.network.ApiClientFactory
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.data.LoginRequest
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.auth.domain.SessionStore
import com.pactogg.app.feature.connected.data.ApiProductRepository
import com.pactogg.app.feature.connected.data.ApiSocialRepository
import com.pactogg.app.feature.connected.domain.BoardFilter
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.feature.connected.domain.Group
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@Category(IntegrationChecks::class)
class SocialApiIntegrationTest {
    private val server = MockWebServer()
    private lateinit var api: PactoApi
    private lateinit var sessions: SessionManager
    private lateinit var repo: ApiSocialRepository
    private val store = object : SessionStore {
        var session: Session? = null
        override fun read() = session
        override fun write(session: Session) { this.session = session }
        override fun clear() { session = null }
    }
    @Before fun setup() {
        val cert = HeldCertificate.Builder().commonName("localhost").addSubjectAlternativeName("localhost")
            .addSubjectAlternativeName("127.0.0.1").addSubjectAlternativeName("::1").build()
        server.useHttps(HandshakeCertificates.Builder().heldCertificate(cert).build().sslSocketFactory(), false)
        server.start()
        val trust = HandshakeCertificates.Builder().addTrustedCertificate(cert.certificate).build()
        val client = OkHttpClient.Builder().sslSocketFactory(trust.sslSocketFactory(), trust.trustManager)
            .retryOnConnectionFailure(false).followRedirects(false).build()
        api = ApiClientFactory().create(server.url("/").toString(), client).create(PactoApi::class.java)
        sessions = SessionManager(api, store)
        repo = ApiSocialRepository(api, sessions)
    }
    @After fun cleanup() { server.shutdown() }

    @Test fun ownProfileAndPatchPreserveCredentialsAndPersistIdentity() = runBlocking {
        login()
        server.enqueue(json(profile("Player")))
        assertEquals(50, repo.profile().wordIndex)
        assertEquals("/api/v1/users/me", server.takeRequest().path)
        server.enqueue(json(profile("NewPlayer")))
        assertEquals("NewPlayer", repo.updateProfile("NewPlayer").account.nickname)
        val patch = server.takeRequest()
        assertEquals("PATCH", patch.method)
        assertEquals("{\"nickname\":\"NewPlayer\"}", patch.body.readUtf8())
        assertEquals("Bearer synthetic-access", patch.getHeader("Authorization"))
        assertEquals("synthetic-access", store.session?.accessToken)
        assertEquals("synthetic-refresh", store.session?.refreshToken)
        assertEquals("NewPlayer", store.session?.account?.nickname)
    }
    @Test fun anotherProfileCannotOverwriteSession() = runBlocking {
        login()
        server.enqueue(json(profile("Other").replace(User, Other)))
        assertEquals("INVALID_RESPONSE", (runCatching { repo.profile() }.exceptionOrNull() as ApiFailure).code)
        assertEquals(User, store.session?.account?.id)
    }
    @Test fun availabilityIsPublicAndRateLimitDoesNotLoop() = runBlocking {
        server.enqueue(json("""{"data":{"nickname":"Player","available":false}}"""))
        assertFalse(repo.nickname("Player").available)
        val query = server.takeRequest()
        assertNull(query.getHeader("Authorization"))
        assertEquals("/api/v1/users/check-nickname?nickname=Player", query.path)
        server.enqueue(json("""{"code":"RATE_LIMITED"}""", 429))
        assertEquals(429, (runCatching { repo.nickname("Player") }.exceptionOrNull() as ApiFailure).status)
        assertEquals(2, server.requestCount)
    }
    @Test fun groupListDetailAndJoinUseServerRoleBalanceAndNormalizedCode() = runBlocking {
        login()
        server.enqueue(json("""{"data":[$group],"meta":{"has_more":true,"next_cursor":"$GroupId"}}"""))
        assertEquals(GroupId, repo.groups("Equipo", null).cursor)
        assertEquals("/api/v1/groups?q=Equipo&limit=20", server.takeRequest().path)
        server.enqueue(json("""{"data":$group}"""))
        assertEquals(100, repo.group(GroupId).points)
        assertEquals("/api/v1/groups/$GroupId", server.takeRequest().path)
        server.enqueue(json("""{"data":$group}"""))
        assertEquals("admin", repo.join("abcdef").role)
        assertEquals("{\"code\":\"ABCDEF\"}", server.takeRequest().body.readUtf8())
    }
    @Test fun malformedPageAndWrongGroupAreRejected() = runBlocking {
        login()
        server.enqueue(json("""{"data":[],"meta":{"has_more":true}}"""))
        assertTrue(runCatching { repo.groups("", null) }.isFailure)
        server.takeRequest()
        server.enqueue(json("""{"data":${group.replace(GroupId, Other)}}"""))
        assertTrue(runCatching { repo.group(GroupId) }.isFailure)
    }
    @Test fun membersPrefixAndBoardFiltersFollowCursorContract() = runBlocking {
        login()
        server.enqueue(json("""{"data":[{"user_id":"$Other","nickname":"Rival","avatar_id":"default","role":"member","points_balance":75,"joined_at":"2026-11-04T00:00:00Z"}],"meta":{"has_more":false}}"""))
        assertEquals(75, repo.members(GroupId, "Ri", null).items.single().points)
        assertEquals("/api/v1/groups/$GroupId/members?q=Ri&limit=20", server.takeRequest().path)
        server.enqueue(json("""{"data":[${contractJson()}],"meta":{"has_more":false}}"""))
        val challenge = repo.contracts(GroupId, BoardFilter("open", true, "Halo", "Ganar"), null).items.single()
        assertEquals(1, challenge.version)
        assertEquals(User, challenge.challengerId)
        assertEquals("/api/v1/groups/$GroupId/contracts?status=open&mine=true&game=Halo&q=Ganar&limit=20",
            server.takeRequest().path)
        server.enqueue(json("""{"data":${contractJson()}}"""))
        assertEquals(0, ApiProductRepository(api, sessions).challenge(challenge).evidenceCount)
        assertEquals("/api/v1/contracts/$ContractId", server.takeRequest().path)
    }
    @Test fun acceptSendsObservedVersionAndUsesConfirmedBalance() = runBlocking {
        login()
        server.enqueue(json("""{"data":${contractJson("in_progress", ",\"my_points\":75")}}"""))
        val result = repo.accept(challenge)
        val request = server.takeRequest()
        assertEquals("/api/v1/contracts/$ContractId/accept", request.path)
        assertEquals("{\"version\":1}", request.body.readUtf8())
        assertEquals(75, result.myPoints)
        assertEquals("in_progress", result.status)
    }
    @Test fun acceptErrorsNeverRetryOrEraseSession() = runBlocking {
        login()
        for ((status, code) in listOf(402 to "INSUFFICIENT_POINTS", 403 to "NOT_CONTRACT_TARGET",
            409 to "INVALID_CONTRACT_STATE")) {
            server.enqueue(json("""{"code":"$code"}""", status))
            assertEquals(code, (runCatching { repo.accept(challenge) }.exceptionOrNull() as ApiFailure).code)
            server.takeRequest()
            assertNotNull(sessions.current.value)
        }
        assertEquals(4, server.requestCount)
    }
    @Test fun rejectionUsesCancelledReasonAndVersionWithoutInventingPoints() = runBlocking {
        login()
        server.enqueue(json("""{"data":${contractJson("cancelled", ",\"closed_reason\":\"rejected\"")}}"""))
        val result = repo.reject(challenge)
        val request = server.takeRequest()
        assertEquals("/api/v1/contracts/$ContractId/reject", request.path)
        assertEquals("{\"version\":1}", request.body.readUtf8())
        assertEquals("rejected", result.closedReason)
        assertNull(result.myPoints)
    }
    @Test fun directedChallengeSerializesRealTarget() = runBlocking {
        login()
        server.enqueue(json("""{"data":${contractJson()}}""", 201))
        ApiProductRepository(api, sessions).createChallenge(Group(GroupId,"Equipo","ABCDEF","admin",100,listOf(25)),
            ChallengeDraft("Halo", "Ganar partida", "Elegir el juego", "2099-11-10T23:59:00Z",25,Other))
        val body = server.takeRequest().body.readUtf8()
        assertTrue(body.contains("\"target_type\":\"member\""))
        assertTrue(body.contains("\"target_id\":\"$Other\""))
    }
    private suspend fun login() {
        server.enqueue(json("""{"data":{"user":{"id":"$User","email":"qa@example.com","nickname":"Player"},"access_token":"synthetic-access","refresh_token":"synthetic-refresh","token_type":"Bearer","expires_in":900}}"""))
        sessions.login(LoginRequest("qa@example.com", "synthetic"))
        server.takeRequest()
    }
    private fun profile(nickname: String) = """{"data":{"id":"$User","email":"qa@example.com","nickname":"$nickname","avatar_id":"default","email_verified":false,"groups_count":1,"stats":{"completed":0,"cheated":0,"streak":0,"trophies":0,"word_index":50}}}"""
    private fun contractJson(status: String = "open", extra: String = "") = """{"id":"$ContractId","group_id":"$GroupId","challenger_id":"$User","target_type":"member","target_id":"$Other","game":"Halo","condition":"Ganar partida","reward":"Elegir juego","deadline":"2099-11-10T23:59:00Z","bond_required":25,"status":"$status","evidence_count":0,"version":1$extra}"""
    private fun json(body: String, status: Int = 200) = MockResponse().setResponseCode(status)
        .setHeader("Content-Type", "application/json").setBody(body)
    private val group get() = """{"id":"$GroupId","name":"Equipo","invite_code":"ABCDEF","my_role":"admin","my_points":100,"settings":{"bond_options":[25,50]}}"""
    private val challenge get() = Challenge(ContractId, GroupId,"Halo","Ganar partida","Elegir juego",
        "2099-11-10T23:59:00Z",25,"open",User,"member",Other,version=1)
    companion object {
        private const val User = "111111111111111111111111"
        private const val Other = "444444444444444444444444"
        private const val GroupId = "222222222222222222222222"
        private const val ContractId = "333333333333333333333333"
    }
}
