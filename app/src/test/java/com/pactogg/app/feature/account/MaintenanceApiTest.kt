package com.pactogg.app.feature.account

import com.pactogg.app.feature.account.data.DeviceRepository
import com.pactogg.app.feature.account.data.MaintenanceRepository
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.ContractFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(IntegrationChecks::class)
class MaintenanceApiTest {
    @Test fun cancelUsesObservedVersionAndRequiresNoContent() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("", 204))
            MaintenanceRepository(f.api, f.sessions).cancel(ContractFixture)
            val request = f.server.takeRequest()
            assertEquals("DELETE", request.method)
            assertEquals("7", request.requestUrl?.queryParameter("version"))
            assertNotNull(request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }
    @Test fun deviceRemovalTargetsOnlyTheCurrentToken() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("", 204))
            val token = "synthetic-fcm-token-1234567890"
            DeviceRepository(f.api, f.sessions).remove(token)
            val request = f.server.takeRequest()
            assertEquals("DELETE", request.method)
            assertEquals(token, request.requestUrl?.queryParameter("fcm_token"))
        }
    }
}
