package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.IntegrationChecks::class)
class VOT_01ApiTest {
    @Test fun VOT_01_httpsContractAndAuthentication() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":[{"contract":{"id":"333333333333333333333333","group_id":"222222222222222222222222","game":"Halo","condition":"Win","reward":"Choose game","deadline":"2099-01-01T00:00:00Z","bond_required":25,"status":"voting"},"voting_ends_at":null}],"meta":{"has_more":false,"next_cursor":null}}"""))
            val result = CommunityRepository(f.api, f.sessions).pending()
            assertNotNull(result)
            val request = f.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/me/votes/pending", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }

}
