package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.IntegrationChecks::class)
class VOT_04ApiTest {
    @Test fun VOT_04_httpsContractAndAuthentication() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":{"contract_id":"333333333333333333333333","outcome":"fulfilled","resolved_by":null,"resolved_at":null,"legit_votes":1,"cheat_votes":1,"bond_required":1,"bond_movement":"fixture","challenger_id":"111111111111111111111111","executor_id":null,"reward":"fixture","votes":[]},"meta":{"has_more":false,"next_cursor":null}}"""))
            val result = CommunityRepository(f.api, f.sessions).resolve(ContractId, "fulfilled")
            assertNotNull(result)
            val request = f.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/contracts/333333333333333333333333/votes/resolve", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val key = requireNotNull(request.getHeader("Idempotency-Key"))
            assertEquals(key, java.util.UUID.fromString(key).toString())
            assertEquals(1, f.server.requestCount)
        }
    }

}
