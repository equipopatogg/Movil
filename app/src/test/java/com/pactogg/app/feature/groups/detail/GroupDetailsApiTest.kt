package com.pactogg.app.feature.groups.detail
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure


import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import com.pactogg.app.testing.shared.GroupId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** GRP-03: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class GroupDetailsApiTest {
    @Test fun GRP_03_mapsRequestedGroupIdentity() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.GROUP)
            val result = fixture.social.group(GroupId)
            val request = fixture.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/groups/$GroupId", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(GroupId, result.id)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun GRP_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(404 to "NOT_FOUND")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.social.group(GroupId) }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }

}
