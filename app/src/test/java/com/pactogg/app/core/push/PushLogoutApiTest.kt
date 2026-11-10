package com.pactogg.app.core.push

import com.pactogg.app.feature.account.data.DeviceRepository
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(IntegrationChecks::class)
class PushLogoutApiTest {
    @Test fun DEV_02_removesSpecificTokenBeforeRevokingSession() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare()
            fixture.server.enqueue(ApiTestFixture.json("", 204))
            fixture.enqueueSuccess(Endpoint.LOGOUT)
            val token = "synthetic-fcm-token-1234567890"
            fixture.sessions.beforeLogout = { DeviceRepository(fixture.api, fixture.sessions).remove(token); true }
            assertTrue(fixture.sessions.logout())
            val remove = fixture.server.takeRequest()
            assertEquals("DELETE", remove.method)
            assertEquals("/api/v1/devices/me", remove.requestUrl?.encodedPath)
            assertEquals(token, remove.requestUrl?.queryParameter("fcm_token"))
            assertEquals("/api/v1/auth/logout", fixture.server.takeRequest().requestUrl?.encodedPath)
            assertNull(fixture.sessions.current.value)
        }
    }

    @Test fun DEV_02_unconfirmedRemovalStillClosesSessionAndReportsFalse() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare()
            fixture.enqueueSuccess(Endpoint.LOGOUT)
            fixture.sessions.beforeLogout = { false }
            assertFalse(fixture.sessions.logout())
            assertNull(fixture.sessions.current.value)
            assertNull(fixture.store.saved)
            assertEquals("/api/v1/auth/logout", fixture.server.takeRequest().requestUrl?.encodedPath)
        }
    }
}
