package com.pactogg.app.feature.profile.edit
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

/** USR-03: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class EditProfileApiTest {
    @Test fun USR_03_sendsOnlyNicknameAndUpdatesVisibleIdentity() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.PATCH_PROFILE)
            fixture.social.updateProfile("NewPlayer")
            val request = fixture.server.takeRequest()
            assertEquals("PATCH", request.method)
            assertEquals("/api/v1/users/me", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertEquals(setOf("nickname"), data?.keys)
            assertEquals("NewPlayer", fixture.store.saved?.account?.nickname)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun USR_03_occupiedNicknamePreservesIdentityAndCanBeCorrected() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); val before = f.store.saved
            f.server.enqueue(ApiTestFixture.json("""{"code":"NICKNAME_ALREADY_EXISTS"}""", 409))
            assertEquals(409, (runCatching { f.social.updateProfile("Occupied") }.exceptionOrNull() as ApiFailure).status)
            assertEquals(before, f.store.saved)
            f.enqueueSuccess(Endpoint.PATCH_PROFILE)
            val updated = f.social.updateProfile("NewPlayer")
            assertEquals("NewPlayer", updated.account.nickname)
            assertEquals(before?.accessToken, f.store.saved?.accessToken)
            assertEquals(before?.refreshToken, f.store.saved?.refreshToken)
            assertEquals("PATCH", f.server.takeRequest().method)
            assertEquals("PATCH", f.server.takeRequest().method)
            f.enqueueSuccess(Endpoint.PROFILE)
            assertEquals("NewPlayer", f.social.profile().account.nickname)
            assertEquals("GET", f.server.takeRequest().method)
            assertEquals(3, f.server.requestCount)
        }
    }


    @Test fun USR_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(400 to "INVALID_INPUT", 422 to "VALIDATION_ERROR", 422 to "CONTENT_REJECTED", 503 to "MODERATION_UNAVAILABLE")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.social.updateProfile("ValidName") }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }

}
