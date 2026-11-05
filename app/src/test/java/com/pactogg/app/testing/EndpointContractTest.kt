package com.pactogg.app.testing

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.junit.*
import org.junit.Assert.*
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
@Category(IntegrationChecks::class)
class EndpointContractTest(private val endpoint: Endpoint) {
    @Test fun consumesConfirmedWireContract() = runBlocking {
        EndpointFixture().use { fixture ->
            fixture.prepare(endpoint != Endpoint.REGISTER && endpoint != Endpoint.LOGIN)
            fixture.success(endpoint)
            val result = fixture.call(endpoint)
            val request = fixture.server.takeRequest()
            assertEquals(endpoint.verb, request.method)
            assertEquals("/api/v1/" + endpoint.route, request.path?.substringBefore('?'))
            assertEquals(if (endpoint.protected) "Bearer $Access" else null, request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            when (endpoint) {
                Endpoint.REGISTER -> { assertEquals(true, data?.get("accept_privacy")?.jsonPrimitive?.boolean)
                    assertEquals("2026-10-01", data?.get("privacy_version")?.jsonPrimitive?.content) }
                Endpoint.LOGIN -> assertEquals("qa@example.com", data?.get("email")?.jsonPrimitive?.content)
                Endpoint.REFRESH -> { assertEquals(Refresh, data?.get("refresh_token")?.jsonPrimitive?.content)
                    assertEquals("new-synthetic-refresh", fixture.store.saved?.refreshToken) }
                Endpoint.LOGOUT -> { assertNull(fixture.store.saved); assertEquals(Refresh,
                    data?.get("refresh_token")?.jsonPrimitive?.content) }
                Endpoint.CREATE_GROUP -> assertEquals("Equipo", data?.get("name")?.jsonPrimitive?.content)
                Endpoint.CREATE_CONTRACT -> { assertEquals(OtherId, data?.get("target_id")?.jsonPrimitive?.content)
                    assertEquals(GroupId, data?.get("group_id")?.jsonPrimitive?.content) }
                Endpoint.EVIDENCE -> assertEquals("https://youtu.be/example", data?.get("url")?.jsonPrimitive?.content)
                Endpoint.ACCEPT, Endpoint.REJECT -> assertEquals(7, data?.get("version")?.jsonPrimitive?.int)
                Endpoint.PATCH_PROFILE -> { assertEquals(setOf("nickname"), data?.keys)
                    assertEquals("NewPlayer", fixture.store.saved?.account?.nickname) }
                Endpoint.JOIN -> assertEquals("ABCDEF", data?.get("code")?.jsonPrimitive?.content)
                Endpoint.NICKNAME -> assertEquals("AvailableName", request.requestUrl?.queryParameter("nickname"))
                Endpoint.GROUPS -> assertEquals("Equipo", request.requestUrl?.queryParameter("q"))
                Endpoint.MEMBERS -> assertEquals("Ri", request.requestUrl?.queryParameter("q"))
                Endpoint.CONTRACTS -> { assertEquals("true", request.requestUrl?.queryParameter("mine"))
                    assertEquals("open", request.requestUrl?.queryParameter("status")) }
                Endpoint.TOURNAMENTS -> assertEquals("Halo", request.requestUrl?.queryParameter("game"))
                Endpoint.PROFILE -> assertEquals(UserId, (result as com.pactogg.app.feature.connected.domain.Profile).account.id)
                Endpoint.GROUP -> assertEquals(GroupId, (result as com.pactogg.app.feature.connected.domain.Group).id)
                Endpoint.CONTRACT -> assertEquals(7, (result as com.pactogg.app.feature.connected.domain.Challenge).version)
            }
            assertEquals(1, fixture.server.requestCount)
        }
    }
    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0}")
        fun endpoints(): List<Array<Endpoint>> = Endpoint.entries.map { arrayOf(it) }
    }
}
