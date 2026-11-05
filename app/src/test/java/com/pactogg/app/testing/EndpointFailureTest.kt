package com.pactogg.app.testing

import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
@Category(SecurityChecks::class)
class EndpointFailureTest(private val endpoint: Endpoint) {
    @Test fun temporaryFailureRetainsSessionAndDoesNotRetry() = runBlocking {
        EndpointFixture().use { fixture ->
            fixture.prepare(endpoint != Endpoint.REGISTER && endpoint != Endpoint.LOGIN)
            fixture.server.enqueue(EndpointFixture.json("""{"code":"STORAGE_UNAVAILABLE","message":"$Sensitive"}""", 503))
            val error = runCatching { fixture.call(endpoint) }.exceptionOrNull() as ApiFailure
            assertEquals(503, error.status)
            assertFalse(error.toString().contains(Sensitive))
            assertEquals(1, fixture.server.requestCount)
            if (endpoint in listOf(Endpoint.REGISTER, Endpoint.LOGIN, Endpoint.REFRESH, Endpoint.LOGOUT)) {
                assertNull(fixture.store.saved)
            } else assertNotNull(fixture.store.saved)
        }
    }
    @Test fun rateLimitNeverPollsOrRetriesAutomatically() = runBlocking {
        EndpointFixture().use { fixture ->
            fixture.prepare(endpoint != Endpoint.REGISTER && endpoint != Endpoint.LOGIN)
            fixture.server.enqueue(EndpointFixture.json("""{"code":"RATE_LIMITED","message":"$Sensitive"}""", 429))
            val error = runCatching { fixture.call(endpoint) }.exceptionOrNull() as ApiFailure
            assertEquals(429, error.status); assertEquals(1, fixture.server.requestCount)
            assertFalse(error.toString().contains(Sensitive))
        }
    }
    @Test fun malformedSuccessCannotBeReportedAsConfirmed() = runBlocking {
        EndpointFixture().use { fixture ->
            fixture.prepare(endpoint != Endpoint.REGISTER && endpoint != Endpoint.LOGIN)
            fixture.server.enqueue(EndpointFixture.json("""{"unexpected":"$Sensitive"}""", if (endpoint == Endpoint.LOGOUT) 200 else endpoint.success))
            assertNotNull(runCatching { fixture.call(endpoint) }.exceptionOrNull())
            assertEquals(1, fixture.server.requestCount)
            if (endpoint in listOf(Endpoint.REGISTER, Endpoint.LOGIN, Endpoint.REFRESH, Endpoint.LOGOUT)) assertNull(fixture.store.saved)
        }
    }
    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0}")
        fun endpoints(): List<Array<Endpoint>> = Endpoint.entries.map { arrayOf(it) }
    }
}
