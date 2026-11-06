package com.pactogg.app.feature.community

import com.pactogg.app.feature.community.data.CommunityRepository
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

@org.junit.experimental.categories.Category(com.pactogg.app.testing.SecurityChecks::class)
class COM_01SecurityTest {
    @Test fun COM_01_anonymousCannotSend() = runTest {
        ApiTestFixture().use { f ->
            f.prepare(false)
            val error = runCatching { CommunityRepository(f.api, f.sessions).commitments(GroupId) }.exceptionOrNull()
            assertEquals(401, (error as ApiFailure).status)
            assertEquals(0, f.server.requestCount)
        }
    }
    @Test fun COM_01_serverErrorsNeverSucceedOrReplay() = runTest {
        for (status in listOf(403, 404, 409, 422, 429, 503)) {
            ApiTestFixture().use { f ->
                f.prepare()
                f.server.enqueue(ApiTestFixture.json("""{"code":"REJECTED"}""", status))
                val error = runCatching { CommunityRepository(f.api, f.sessions).commitments(GroupId) }.exceptionOrNull()
                assertEquals(status, (error as ApiFailure).status)
                assertEquals(1, f.server.requestCount)
                assertNotNull(f.store.saved)
            }
        }
    }
}
