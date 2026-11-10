package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.IntegrationChecks::class)
class NOT_03ApiTest {
    @Test fun NOT_03_httpsContractAndAuthentication() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":{"updated":1},"meta":{"has_more":false,"next_cursor":null}}"""))
            val result = CommunityRepository(f.api, f.sessions).notices.readAll()
            assertNotNull(result)
            val request = f.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/notifications/read-all", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }

}
