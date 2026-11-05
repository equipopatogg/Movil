package com.pactogg.app.testing

import com.pactogg.app.core.network.ApiClientFactory
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.data.*
import com.pactogg.app.feature.auth.domain.*
import com.pactogg.app.feature.connected.data.*
import com.pactogg.app.feature.connected.domain.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import java.util.concurrent.TimeUnit

enum class Endpoint(val id: String, val verb: String, val route: String, val protected: Boolean = true,
    val success: Int = 200) {
    REGISTER("USR-01", "POST", "auth/register", false, 201),
    LOGIN("AUTH-01", "POST", "auth/login", false),
    REFRESH("AUTH-03", "POST", "auth/refresh", false),
    LOGOUT("AUTH-04", "POST", "auth/logout", success = 204),
    CREATE_GROUP("GRP-01", "POST", "groups", success = 201),
    CREATE_CONTRACT("CON-01", "POST", "contracts", success = 201),
    EVIDENCE("EVI-02", "POST", "contracts/$ContractId/evidence", success = 201),
    TOURNAMENTS("TOR-02", "GET", "tournaments"),
    PROFILE("USR-02", "GET", "users/me"),
    PATCH_PROFILE("USR-03", "PATCH", "users/me"),
    NICKNAME("USR-06", "GET", "users/check-nickname", false),
    GROUPS("GRP-02", "GET", "groups"),
    GROUP("GRP-03", "GET", "groups/$GroupId"),
    JOIN("MEM-01", "POST", "groups/join"),
    MEMBERS("MEM-02", "GET", "groups/$GroupId/members"),
    CONTRACTS("CON-02", "GET", "groups/$GroupId/contracts"),
    CONTRACT("CON-03", "GET", "contracts/$ContractId"),
    ACCEPT("CON-06", "POST", "contracts/$ContractId/accept"),
    REJECT("CON-07", "POST", "contracts/$ContractId/reject");
    override fun toString() = id
}
const val UserId = "111111111111111111111111"
const val GroupId = "222222222222222222222222"
const val ContractId = "333333333333333333333333"
const val OtherId = "444444444444444444444444"
const val Access = "synthetic-access-fixture"
const val Refresh = "synthetic-refresh-fixture"
const val Sensitive = "DO_NOT_EXPOSE_SYNTHETIC_PASSWORD_OR_TOKEN"
val GroupFixture = Group(GroupId, "Equipo", "ABCDEF", "member", 100, listOf(25, 50))
val ContractFixture = Challenge(ContractId, GroupId, "Halo", "Win a game", "Choose next game",
    "2099-11-10T23:59:00Z", 25, "open", OtherId, "member", UserId, version = 7)

class FixtureStore : SessionStore {
    var saved: Session? = null
    override fun read() = saved
    override fun write(session: Session) { saved = session }
    override fun clear() { saved = null }
}
class EndpointFixture : AutoCloseable {
    val server = MockWebServer()
    val store = FixtureStore()
    var now = 1000L
    val api: PactoApi
    val sessions: SessionManager
    val products: ApiProductRepository
    val social: ApiSocialRepository
    init {
        val certificate = HeldCertificate.Builder().commonName("localhost")
            .addSubjectAlternativeName("localhost").addSubjectAlternativeName("127.0.0.1")
            .addSubjectAlternativeName("::1").build()
        server.useHttps(HandshakeCertificates.Builder().heldCertificate(certificate).build().sslSocketFactory(), false)
        server.start()
        val trust = HandshakeCertificates.Builder().addTrustedCertificate(certificate.certificate).build()
        val client = OkHttpClient.Builder().sslSocketFactory(trust.sslSocketFactory(), trust.trustManager)
            .retryOnConnectionFailure(false).followRedirects(false).followSslRedirects(false)
            .callTimeout(5, TimeUnit.SECONDS).build()
        api = ApiClientFactory().create(server.url("/").toString(), client).create(PactoApi::class.java)
        sessions = SessionManager(api, store) { now }
        products = ApiProductRepository(api, sessions)
        social = ApiSocialRepository(api, sessions)
    }
    suspend fun prepare(signedIn: Boolean = true) {
        if (signedIn) store.saved = Session(Account(UserId, "qa@example.com", "Player"), Access, Refresh, 100000)
        sessions.restore()
    }
    suspend fun call(endpoint: Endpoint): Any = when (endpoint) {
        Endpoint.REGISTER -> sessions.register(RegisterRequest("qa@example.com", "synthetic password", "Player",
            true, "2026-10-01"))
        Endpoint.LOGIN -> sessions.login(LoginRequest("qa@example.com", "synthetic password"))
        Endpoint.REFRESH -> { now = 200000; sessions.restore() }
        Endpoint.LOGOUT -> sessions.logout()
        Endpoint.CREATE_GROUP -> products.createGroup("Equipo")
        Endpoint.CREATE_CONTRACT -> products.createChallenge(GroupFixture, ChallengeDraft("Halo", "Win a game",
            "Choose next game", "2099-11-10T23:59:00Z", 25, OtherId))
        Endpoint.EVIDENCE -> products.sendLink(ContractFixture, "https://youtu.be/example")
        Endpoint.TOURNAMENTS -> products.tournaments("open", "Halo", null)
        Endpoint.PROFILE -> social.profile()
        Endpoint.PATCH_PROFILE -> social.updateProfile("NewPlayer")
        Endpoint.NICKNAME -> social.nickname("AvailableName")
        Endpoint.GROUPS -> social.groups("Equipo", null)
        Endpoint.GROUP -> social.group(GroupId)
        Endpoint.JOIN -> social.join("abcdef")
        Endpoint.MEMBERS -> social.members(GroupId, "Ri", null)
        Endpoint.CONTRACTS -> social.contracts(GroupId, BoardFilter("open", true, "Halo", "Win"), null)
        Endpoint.CONTRACT -> products.challenge(ContractFixture)
        Endpoint.ACCEPT -> social.accept(ContractFixture)
        Endpoint.REJECT -> social.reject(ContractFixture)
    }
    fun success(endpoint: Endpoint) {
        val body = when (endpoint) {
            Endpoint.REGISTER, Endpoint.LOGIN, Endpoint.REFRESH -> sessionJson
            Endpoint.LOGOUT -> ""
            Endpoint.CREATE_GROUP, Endpoint.GROUP, Endpoint.JOIN -> envelope(groupJson)
            Endpoint.CREATE_CONTRACT, Endpoint.CONTRACT -> envelope(contractJson())
            Endpoint.ACCEPT -> envelope(contractJson("in_progress", ",\"my_points\":75,\"accepted_by\":\"$UserId\""))
            Endpoint.REJECT -> envelope(contractJson("cancelled", ",\"closed_reason\":\"rejected\""))
            Endpoint.EVIDENCE -> envelope("""{"id":"$OtherId","contract_id":"$ContractId","type":"link","moderation":{"status":"approved"}}""")
            Endpoint.PROFILE, Endpoint.PATCH_PROFILE -> envelope(profileJson)
            Endpoint.NICKNAME -> envelope("""{"nickname":"AvailableName","available":true}""")
            Endpoint.GROUPS -> page(groupJson)
            Endpoint.MEMBERS -> page("""{"user_id":"$OtherId","nickname":"Rival","avatar_id":"default","role":"member","points_balance":75,"joined_at":"2026-11-04T00:00:00Z"}""")
            Endpoint.CONTRACTS -> page(contractJson())
            Endpoint.TOURNAMENTS -> page("""{"id":"$OtherId","game":"Halo","format":"single_elimination","status":"open","scheduled_at":"2099-11-10T00:00:00Z","bracket":{"matches":[]}}""")
        }
        server.enqueue(json(body, endpoint.success))
    }
    override fun close() { server.shutdown() }
    companion object {
        fun json(body: String, status: Int = 200) = MockResponse().setResponseCode(status)
            .setHeader("Content-Type", "application/json").setBody(body)
        fun envelope(data: String) = """{"data":$data}"""
        fun page(item: String) = """{"data":[$item],"meta":{"has_more":false,"next_cursor":null}}"""
        val groupJson = """{"id":"$GroupId","name":"Equipo","invite_code":"ABCDEF","my_role":"member","my_points":100,"settings":{"bond_options":[25,50]}}"""
        fun contractJson(status: String = "open", extra: String = "") = """{"id":"$ContractId","group_id":"$GroupId","challenger_id":"$OtherId","target_type":"member","target_id":"$UserId","game":"Halo","condition":"Win a game","reward":"Choose next game","deadline":"2099-11-10T23:59:00Z","bond_required":25,"status":"$status","version":7,"evidence_count":0$extra}"""
        val profileJson = """{"id":"$UserId","email":"qa@example.com","nickname":"NewPlayer","avatar_id":"default","email_verified":false,"groups_count":1,"stats":{"completed":0,"cheated":0,"streak":0,"trophies":0,"word_index":50}}"""
        val sessionJson = """{"data":{"user":{"id":"$UserId","email":"qa@example.com","nickname":"Player"},"access_token":"new-synthetic-access","refresh_token":"new-synthetic-refresh","token_type":"Bearer","expires_in":900}}"""
    }
}
