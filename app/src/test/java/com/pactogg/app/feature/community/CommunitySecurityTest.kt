package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.ContractId
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.SecurityChecks::class)
class CommunitySecurityTest {
    @Test fun VOT_02_invalidVoteNeverReachesNetwork() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            val result = runCatching { CommunityRepository(f.api, f.sessions).vote(ContractId, "invalid", null) }
            assertTrue(result.exceptionOrNull() is IllegalArgumentException)
            assertEquals(0, f.server.requestCount)
        }
    }
    @Test fun VOT_02_forbiddenDoesNotRetryOrSucceed() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"code":"NOT_JUROR"}""", 403))
            val result = runCatching { CommunityRepository(f.api, f.sessions).vote(ContractId, "legit", null) }
            assertEquals(403, (result.exceptionOrNull() as ApiFailure).status)
            assertEquals(1, f.server.requestCount)
            val request = f.server.takeRequest()
            assertEquals("PUT", request.method)
            assertEquals("/api/v1/contracts/$ContractId/votes", request.path)
        }
    }
    @Test fun VOT_03_rejectsDifferentContractResponse() = runTest {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":{"contract_id":"444444444444444444444444","status":"voting","jurors_assigned":3,"votes_cast":1,"votes_remaining":2,"i_am_juror":true,"i_voted":false,"admin_can_resolve":false}}"""))
            val result = runCatching { CommunityRepository(f.api, f.sessions).summary(ContractId) }
            assertEquals("INVALID_RESPONSE", (result.exceptionOrNull() as ApiFailure).code)
        }
    }
    @Test fun NOT_03_anonymousNeverSendsRequest() = runTest {
        ApiTestFixture().use { f ->
            f.prepare(false)
            val result = runCatching { CommunityRepository(f.api, f.sessions).notices.readAll() }
            assertTrue(result.isFailure)
            assertEquals(0, f.server.requestCount)
        }
    }
}
