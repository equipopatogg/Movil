package com.pactogg.app.feature.groups.join
import com.pactogg.app.testing.shared.*
import com.pactogg.app.core.network.ApiFailure



import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** MEM-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class JoinGroupApiTest {
    @Test fun MEM_01_normalizesInviteCodeBeforeJoining() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.JOIN)
            fixture.social.join("abcdef")
            val request = fixture.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/groups/join", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertEquals("ABCDEF", data?.get("code")?.jsonPrimitive?.content)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun MEM_01_existingMembershipConflictDoesNotRetryOrChangeSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); val before = f.store.saved
            f.server.enqueue(ApiTestFixture.json("""{"code":"ALREADY_MEMBER"}""", 409))
            val failure = runCatching { f.social.join("abcdef") }.exceptionOrNull() as ApiFailure
            assertEquals(409, failure.status); assertEquals("ALREADY_MEMBER", failure.code)
            assertEquals(before, f.store.saved); assertEquals(1, f.server.requestCount)
            assertEquals("/api/v1/groups/join", f.server.takeRequest().path)
        }
    }


    @Test fun MEM_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(404 to "NOT_FOUND", 422 to "VALIDATION_ERROR", 409 to "GROUP_FULL", 409 to "GROUP_LIMIT_REACHED")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.social.join("ABCDEF") }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }

}
