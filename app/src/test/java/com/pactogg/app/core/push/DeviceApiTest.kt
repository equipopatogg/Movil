package com.pactogg.app.core.push

import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-10 (DEV-01, DEV-02) contra un servidor simulado. */
@Category(IntegrationChecks::class)
class DeviceApiTest {
    private val token = "f".repeat(60)

    @Test fun PL_10_registersTokenWithPutAndBearer() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope(
                """{"id":"dev-1","platform":"android","updated_at":"2026-11-16T00:00:00Z"}""")))
            val device = f.sessions.authorized { f.api.registerDevice(it, DeviceRequest(token, version = "0.1.0")) }
                .body()?.data
            val request = f.server.takeRequest()
            assertEquals("PUT", request.method)
            assertEquals("/api/v1/devices/me", request.path)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val body = request.body.readUtf8()
            listOf("\"fcm_token\":\"$token\"", "\"platform\":\"android\"", "\"app_version\":\"0.1.0\"")
                .forEach { assertTrue(it, body.contains(it)) }
            assertEquals("android", device?.platform)
        }
    }

    @Test fun PL_10_removesTokenWithDelete() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("", 204))
            val response = f.sessions.authorized { f.api.removeDevice(it, token) }
            val request = f.server.takeRequest()
            assertEquals("DELETE", request.method)
            assertEquals("/api/v1/devices/me?fcm_token=$token", request.path)
            assertEquals(204, response.code())
        }
    }

    @Test fun PL_10_logoutRunsDeviceRemovalBeforeClearingAndSurvivesItsFailure() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            var sawSession = false
            f.sessions.beforeLogout = { sawSession = f.sessions.current.value != null; error("offline") }
            f.server.enqueue(ApiTestFixture.json("", 204))
            f.sessions.logout()
            assertTrue(sawSession)
            assertEquals(null, f.store.saved)
        }
    }
}
