package com.pactogg.app.feature.groups.members
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

/** MEM-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class ListMembersApiTest {
    @Test fun MEM_02_sendsMemberSearchWithinSelectedGroup() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.MEMBERS)
            fixture.social.members(GroupId, "Ri", null)
            val request = fixture.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/groups/$GroupId/members", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals("Ri", request.requestUrl?.queryParameter("q"))
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun MEM_02_followsNextCursorAndSupportsLimitOneAtWireBoundary() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            val first = """{"user_id":"$OtherId","nickname":"Rival","avatar_id":"default","role":"member","points_balance":75,"joined_at":"2026-11-04T00:00:00Z"}"""
            f.server.enqueue(ApiTestFixture.json("""{"data":[$first],"meta":{"has_more":true,"next_cursor":"$OtherId"}}"""))
            val page = f.social.members(GroupId, "Ri", null)
            assertTrue(page.hasMore); assertEquals(OtherId, page.cursor)
            f.server.enqueue(ApiTestFixture.json("""{"data":[],"meta":{"has_more":false,"next_cursor":null}}"""))
            assertFalse(f.social.members(GroupId, "Ri", page.cursor).hasMore)
            assertEquals("/api/v1/groups/$GroupId/members?q=Ri&limit=20", f.server.takeRequest().path)
            assertEquals("/api/v1/groups/$GroupId/members?q=Ri&cursor=$OtherId&limit=20", f.server.takeRequest().path)
            f.server.enqueue(ApiTestFixture.json("""{"data":[$first],"meta":{"has_more":true,"next_cursor":"$OtherId"}}"""))
            val response = f.api.members("Bearer $Access", GroupId, null, null, 1)
            assertEquals(1, response.body()?.data?.size)
            assertEquals("/api/v1/groups/$GroupId/members?limit=1", f.server.takeRequest().path)
            val cursor = response.body()?.meta?.nextCursor
            assertEquals(OtherId, cursor)
            f.server.enqueue(ApiTestFixture.json("""{"data":[],"meta":{"has_more":false,"next_cursor":null}}"""))
            val last = f.api.members("Bearer $Access", GroupId, null, cursor, 1)
            assertFalse(requireNotNull(last.body()?.meta).hasMore)
            assertEquals("/api/v1/groups/$GroupId/members?cursor=$OtherId&limit=1", f.server.takeRequest().path)
            assertEquals(4, f.server.requestCount)
        }
    }

}
