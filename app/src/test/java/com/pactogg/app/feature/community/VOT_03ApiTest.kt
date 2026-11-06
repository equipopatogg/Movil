package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.IntegrationChecks::class)
class VOT_03ApiTest {
    @Test fun VOT_03_httpsContractAndAuthentication() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":{"contract_id":"333333333333333333333333","status":"voting","jurors_assigned":1,"votes_cast":1,"votes_remaining":1,"voting_ends_at":null,"i_am_juror":true,"i_voted":true,"admin_can_resolve":true,"legit_votes":null,"cheat_votes":null},"meta":{"has_more":false,"next_cursor":null}}"""))
            val result = CommunityRepository(f.api, f.sessions).summary(ContractId)
            assertNotNull(result)
            val request = f.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/contracts/333333333333333333333333/votes/summary", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }

}
