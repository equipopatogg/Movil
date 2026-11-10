package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.IntegrationChecks::class)
class NOT_02ApiTest {
    @Test fun NOT_02_httpsContractAndAuthentication() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":{"id":"333333333333333333333333","type":"fixture","title":"fixture","body":"fixture","group_id":null,"contract_id":null,"commitment_id":null,"read":true,"read_at":null,"created_at":"fixture"},"meta":{"has_more":false,"next_cursor":null}}"""))
            val result = CommunityRepository(f.api, f.sessions).notices.read(ContractId)
            assertNotNull(result)
            val request = f.server.takeRequest()
            assertEquals("PATCH", request.method)
            assertEquals("/api/v1/notifications/333333333333333333333333/read", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }

}
