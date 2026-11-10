package com.pactogg.app.feature.account

import com.pactogg.app.feature.account.data.AccountAccessRepository
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(IntegrationChecks::class)
class AccountAccessApiTest {
    @Test fun forgotUsesPublic202WithoutCreatingSessionOrAssumingDelivery() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(false)
            f.server.enqueue(ApiTestFixture.json("", 202))
            AccountAccessRepository(f.api, f.sessions).forgot("qa@example.com")
            val request = f.server.takeRequest()
            assertEquals("/api/v1/auth/forgot-password", request.path)
            assertNull(request.getHeader("Authorization"))
            assertEquals("""{"email":"qa@example.com"}""", request.body.readUtf8())
            assertNull(f.store.saved)
        }
    }
    @Test fun verifyConsumesExplicitConfirmationWithoutSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(false)
            f.server.enqueue(ApiTestFixture.json("""{"data":{"email_verified":true}}"""))
            AccountAccessRepository(f.api, f.sessions).verify("synthetic-token-123456")
            val request = f.server.takeRequest()
            assertEquals("/api/v1/auth/verify-email", request.path)
            assertNull(request.getHeader("Authorization"))
        }
    }
    @Test fun successfulResetClosesObservedSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":{"password_reset":true}}"""))
            AccountAccessRepository(f.api, f.sessions).reset("synthetic-token-123456", "SyntheticPassword123!")
            assertNull(f.sessions.current.value)
            assertNull(f.store.saved)
            assertNull(f.server.takeRequest().getHeader("Authorization"))
        }
    }
    @Test fun passwordChangeSavesReturnedRotatedSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.enqueueSuccess(Endpoint.LOGIN)
            AccountAccessRepository(f.api, f.sessions).password("OldSynthetic123!", "NewSyntheticPassword123!")
            assertEquals("new-synthetic-access", f.store.saved?.accessToken)
            assertEquals("new-synthetic-refresh", f.store.saved?.refreshToken)
            val request = f.server.takeRequest()
            assertEquals("/api/v1/users/me/password", request.path)
            assertNotNull(request.getHeader("Authorization"))
            assertEquals(1, f.server.requestCount)
        }
    }
    @Test fun googleHandlesCreatedSessionWithoutSendingBearer() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(false)
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.sessionJson, 201))
            AccountAccessRepository(f.api, f.sessions).google(
                "synthetic-google-token-123456", "Player", true, "2026-10-01")
            assertNotNull(f.store.saved)
            val request = f.server.takeRequest()
            assertEquals("/api/v1/auth/google", request.path)
            assertNull(request.getHeader("Authorization"))
        }
    }
}
