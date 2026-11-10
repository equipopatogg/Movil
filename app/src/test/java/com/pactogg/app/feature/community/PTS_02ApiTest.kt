package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.IntegrationChecks::class)
class PTS_02ApiTest {
    @Test fun PTS_02_httpsContractAndAuthentication() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":[{"position":1,"user_id":"111111111111111111111111","nickname":"fixture","avatar_id":"111111111111111111111111","points_balance":1}],"meta":{"has_more":false,"next_cursor":null}}"""))
            val result = CommunityRepository(f.api, f.sessions).ranking(GroupId)
            assertNotNull(result)
            val request = f.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/groups/222222222222222222222222/points/ranking", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }

}
