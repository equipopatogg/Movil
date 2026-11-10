package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.IntegrationChecks::class)
class ACH_01ApiTest {
    @Test fun ACH_01_httpsContractAndAuthentication() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":[{"contract_id":"333333333333333333333333","executor":null,"game":"fixture","condition":"fixture","reward":"fixture","resolved_at":null,"photos":[],"links":[]}],"meta":{"has_more":false,"next_cursor":null}}"""))
            val result = CommunityRepository(f.api, f.sessions).fame(GroupId)
            assertNotNull(result)
            val request = f.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/groups/222222222222222222222222/fame", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }

}
