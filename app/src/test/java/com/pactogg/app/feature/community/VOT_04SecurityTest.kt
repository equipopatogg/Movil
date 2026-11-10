package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.SecurityChecks::class)
class VOT_04SecurityTest {
    @Test fun VOT_04_anonymousCannotSend() = runTest {
        ApiTestFixture().use { f ->
            f.prepare(false)
            val error = runCatching { CommunityRepository(f.api, f.sessions).resolve(ContractId, "fulfilled") }.exceptionOrNull()
            assertEquals(401, (error as ApiFailure).status)
            assertEquals(0, f.server.requestCount)
        }
    }
    @Test fun VOT_04_serverErrorsNeverSucceedOrReplay() = runTest {
        for (status in listOf(403, 404, 409, 422, 429, 503)) {
            ApiTestFixture().use { f ->
                f.prepare()
                f.server.enqueue(ApiTestFixture.json("""{"code":"REJECTED"}""", status))
                val error = runCatching { CommunityRepository(f.api, f.sessions).resolve(ContractId, "fulfilled") }.exceptionOrNull()
                assertEquals(status, (error as ApiFailure).status)
                assertEquals(1, f.server.requestCount)
                assertNotNull(f.store.saved)
            }
        }
    }
}
