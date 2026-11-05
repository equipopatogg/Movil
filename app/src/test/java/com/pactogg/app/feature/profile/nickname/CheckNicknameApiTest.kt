package com.pactogg.app.feature.profile.nickname
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure


import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** USR-06: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class CheckNicknameApiTest {
    @Test fun USR_06_sendsNicknameAvailabilityQuery() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.NICKNAME)
            fixture.social.nickname("AvailableName")
            val request = fixture.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/users/check-nickname", request.path?.substringBefore('?'))
            assertNull(request.getHeader("Authorization"))
            assertEquals("AvailableName", request.requestUrl?.queryParameter("nickname"))
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun USR_06_existingNameWithDifferentCaseIsShownOccupiedWithoutAuthentication() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(false)
            f.server.enqueue(ApiTestFixture.json("""{"data":{"nickname":"pLaYeR","available":false}}"""))
            assertFalse(f.social.nickname("pLaYeR").available)
            val request = f.server.takeRequest()
            assertEquals("/api/v1/users/check-nickname?nickname=pLaYeR", request.path)
            assertNull(request.getHeader("Authorization")); assertNull(f.store.saved)
        }
    }

}
