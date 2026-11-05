package com.pactogg.app.testing

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.data.*
import com.pactogg.app.feature.connected.data.*
import com.pactogg.app.feature.connected.domain.*
import com.pactogg.app.feature.connected.presentation.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.Assert.*
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.lang.reflect.Proxy

@RunWith(Parameterized::class)
@Category(UnitChecks::class)
class EndpointUnitTest(private val endpoint: Endpoint) {
    @Test fun enforcesConsumerBoundaryWithoutNetwork() = runBlocking {
        val api = Proxy.newProxyInstance(PactoApi::class.java.classLoader, arrayOf(PactoApi::class.java)) { _, _, _ ->
            throw AssertionError("Unexpected network boundary access in a unit test")
        } as PactoApi
        val sessions = SessionManager(api, FixtureStore())
        val social = ApiSocialRepository(api, sessions)
        val products = ApiProductRepository(api, sessions)
        when (endpoint) {
            Endpoint.REGISTER -> { assertFalse(InputRules.access("qa@example.com", "a".repeat(11), "Player", true))
                assertFalse(InputRules.access("qa@example.com", "a".repeat(12), "Player", false))
                assertFalse(InputRules.access("qa@example.com", "a".repeat(12), "a".repeat(25), true)) }
            Endpoint.LOGIN -> { assertFalse(InputRules.access("not-an-email", "pass", null, false))
                assertFalse(InputRules.access("qa@example.com", "a".repeat(129), null, false)) }
            Endpoint.REFRESH -> {
                val bad = SessionDto(UserDto(UserId,"qa@example.com","Player"),"access","refresh","Bearer",0)
                assertTrue(runCatching { bad.toDomain(1000) }.exceptionOrNull() is IllegalArgumentException)
            }
            Endpoint.LOGOUT -> {
                val request = Json.encodeToString(LogoutRequest.serializer(), LogoutRequest("synthetic"))
                assertTrue(request.contains("refresh_token")); assertFalse(request.contains("password"))
            }
            Endpoint.CREATE_GROUP -> invalid { products.createGroup("a".repeat(41)) }
            Endpoint.CREATE_CONTRACT -> {
                assertFalse(InputRules.challenge(GroupFixture, ChallengeDraft("Halo", "Win", "Choose game",
                    "2000-01-01T00:00:00Z",25),1000L*1000*1000*1000))
                assertFalse(InputRules.challenge(GroupFixture, ChallengeDraft("Halo","Win","100 USD",
                    "2099-11-10T23:59:00Z",25),1000))
            }
            Endpoint.EVIDENCE -> {
                assertFalse(InputRules.evidenceLink("https://youtube.com.attacker.example/a"))
                invalid { products.sendPhoto(ContractFixture,byteArrayOf(),"image/png") }
            }
            Endpoint.TOURNAMENTS -> invalid { products.tournaments("not-a-status",null,null) }
            Endpoint.PROFILE -> invalid { ProfileDto(UserId,"qa@example.com","Player","default",false,
                ProfileStatsDto(0,0,0,0,101),0).toDomain() }
            Endpoint.PATCH_PROFILE -> invalid { social.updateProfile("a".repeat(25)) }
            Endpoint.NICKNAME -> invalid { social.nickname("bad nickname") }
            Endpoint.GROUPS -> invalid { social.groups("q".repeat(41),null) }
            Endpoint.GROUP -> invalid { social.group("invalid-id") }
            Endpoint.JOIN -> invalid { social.join("ABCDE") }
            Endpoint.MEMBERS -> invalid { social.members(GroupId,"q".repeat(25),null) }
            Endpoint.CONTRACTS -> invalid { social.contracts(GroupId,BoardFilter(status="invalid"),null) }
            Endpoint.CONTRACT -> invalid { ContractDto("invalid-id",GroupId,"Halo","Win","Choose",
                "2099-11-10T00:00:00Z",25,"open").toDomain() }
            Endpoint.ACCEPT -> {
                assertFalse(canAccept(ContractFixture,OtherId,GroupFixture,false))
                assertFalse(canAccept(ContractFixture,UserId,GroupFixture.copy(points=0),false))
            }
            Endpoint.REJECT -> {
                assertFalse(canReject(ContractFixture.copy(targetType="group"),UserId,false))
                assertFalse(canReject(ContractFixture,OtherId,false))
            }
        }
    }
    private suspend fun invalid(block: suspend () -> Unit) {
        assertTrue(runCatching { block() }.exceptionOrNull() is IllegalArgumentException)
    }
    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0}")
        fun endpoints(): List<Array<Endpoint>> = Endpoint.entries.map { arrayOf(it) }
    }
}
