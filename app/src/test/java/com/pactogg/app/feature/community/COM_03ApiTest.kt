package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.IntegrationChecks::class)
class COM_03ApiTest {
    @Test fun COM_03_httpsContractAndAuthentication() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":{"id":"333333333333333333333333","group_id":"222222222222222222222222","contract_id":"333333333333333333333333","debtor":{"user_id":"111111111111111111111111","nickname":"fixture"},"creditor":{"user_id":"111111111111111111111111","nickname":"fixture"},"description":"fixture","game":"fixture","status":"completed","requested_at":null,"completed_at":null,"created_at":"fixture"},"meta":{"has_more":false,"next_cursor":null}}"""))
            val result = CommunityRepository(f.api, f.sessions).complete(ContractId, false)
            assertNotNull(result)
            val request = f.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/commitments/333333333333333333333333/complete", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }

}
