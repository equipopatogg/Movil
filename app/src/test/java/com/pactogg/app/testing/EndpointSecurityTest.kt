package com.pactogg.app.testing

import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
@Category(SecurityChecks::class)
class EndpointSecurityTest(private val endpoint: Endpoint) {
    @Test fun deniedResponseIsSanitizedAndNeverReplayed() = runBlocking {
        EndpointFixture().use { fixture ->
            fixture.prepare(endpoint != Endpoint.REGISTER && endpoint != Endpoint.LOGIN)
            val status = if (endpoint == Endpoint.NICKNAME) 429 else if (endpoint.protected) 403 else 401
            val code = if (status == 429) "RATE_LIMITED" else if (status == 403) "NOT_ALLOWED" else "UNAUTHORIZED"
            fixture.server.enqueue(EndpointFixture.json("""{"code":"$code","message":"$Sensitive","request_id":"safe-request","details":{"token":"$Sensitive"}}""", status))
            val failure = runCatching { fixture.call(endpoint) }.exceptionOrNull()
            assertTrue(failure is ApiFailure)
            failure as ApiFailure
            assertEquals(status, failure.status)
            assertEquals(code, failure.code)
            assertEquals("safe-request", failure.requestId)
            assertFalse(failure.toString().contains(Sensitive))
            assertFalse(failure.toString().contains(Access))
            assertNull(failure.cause)
            assertEquals(1, fixture.server.requestCount)
            val request = fixture.server.takeRequest()
            assertEquals("/api/v1/" + endpoint.route, request.path?.substringBefore('?'))
            if (!endpoint.protected) assertNull(request.getHeader("Authorization"))
            when (endpoint) {
                Endpoint.REGISTER, Endpoint.LOGIN, Endpoint.REFRESH, Endpoint.LOGOUT -> assertNull(fixture.store.saved)
                else -> assertNotNull(fixture.store.saved) // 403 or public rate limit does not erase another valid session.
            }
        }
    }
    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0}")
        fun endpoints(): List<Array<Endpoint>> = Endpoint.entries.map { arrayOf(it) }
    }
}
@RunWith(Parameterized::class)
@Category(SecurityChecks::class)
class ProtectedEndpointSessionTest(private val endpoint: Endpoint) {
    @Test fun noProtectedRequestLeavesDeviceWithoutSession() = runBlocking {
        EndpointFixture().use { fixture ->
            fixture.prepare(false)
            val result = runCatching { fixture.call(endpoint) }
            if (endpoint == Endpoint.LOGOUT) assertTrue(result.getOrThrow() as Boolean)
            else assertEquals(401, (result.exceptionOrNull() as ApiFailure).status)
            assertEquals(0, fixture.server.requestCount)
            assertNull(fixture.store.saved)
        }
    }
    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0}")
        fun endpoints(): List<Array<Endpoint>> = Endpoint.entries.filter { it.protected }.map { arrayOf(it) }
    }
}
