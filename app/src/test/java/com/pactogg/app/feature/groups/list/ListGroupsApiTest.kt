package com.pactogg.app.feature.groups.list

import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** GRP-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class ListGroupsApiTest {
    @Test fun GRP_02_sendsGroupSearchQuery() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.GROUPS)
            fixture.social.groups("Equipo", null)
            val request = fixture.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/groups", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals("Equipo", request.requestUrl?.queryParameter("q"))
            assertEquals(1, fixture.server.requestCount)
        }
    }
}
