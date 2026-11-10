package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.IntegrationChecks::class)
class VOT_02ApiTest {
    @Test fun VOT_02_httpsContractAndAuthentication() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":{"contract_id":"333333333333333333333333","vote":"legit","status":"voting","votes_cast":1,"jurors_assigned":1,"needs_admin":true},"meta":{"has_more":false,"next_cursor":null}}"""))
            val result = CommunityRepository(f.api, f.sessions).vote(ContractId, "legit", null)
            assertNotNull(result)
            val request = f.server.takeRequest()
            assertEquals("PUT", request.method)
            assertEquals("/api/v1/contracts/333333333333333333333333/votes", request.requestUrl?.encodedPath)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val key = requireNotNull(request.getHeader("Idempotency-Key"))
            assertEquals(key, java.util.UUID.fromString(key).toString())
            assertEquals(1, f.server.requestCount)
        }
    }

}
