package com.pactogg.app.feature.account

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.account.data.AccountAccessRepository
import com.pactogg.app.feature.account.data.DeviceRepository
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(SecurityChecks::class)
class AccountSecurityTest {
    @Test fun anonymousResendDoesNotReachTransport() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(false)
            assertTrue(runCatching { AccountAccessRepository(f.api, f.sessions).resend() }.isFailure)
            assertEquals(0, f.server.requestCount)
        }
    }
    @Test fun invalidRecoveryAndEmptyDeviceTokenNeverReachTransport() = runBlocking {
        ApiTestFixture().use { f ->
            val access = AccountAccessRepository(f.api, f.sessions)
            assertTrue(runCatching { access.forgot("invalid") }.isFailure)
            assertTrue(runCatching { access.reset("short", "weak") }.isFailure)
            assertTrue(runCatching { DeviceRepository(f.api, f.sessions).remove("") }.isFailure)
            assertEquals(0, f.server.requestCount)
        }
    }
    @Test fun rejectedResetPreservesSessionAndDoesNotRetry() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            val original = f.store.saved
            f.server.enqueue(ApiTestFixture.json("""{"code":"INVALID_OR_EXPIRED_TOKEN"}""", 400))
            val error = runCatching {
                AccountAccessRepository(f.api, f.sessions).reset("synthetic-token-123456", "SyntheticPassword123!")
            }.exceptionOrNull() as ApiFailure
            assertEquals("INVALID_OR_EXPIRED_TOKEN", error.code)
            assertSame(original, f.store.saved)
            assertEquals(1, f.server.requestCount)
        }
    }
    @Test fun passwordResponseCannotSwitchToAnotherAccount() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            val original = f.store.saved
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.sessionJson.replace(
                "111111111111111111111111", "222222222222222222222222")))
            val error = runCatching {
                AccountAccessRepository(f.api, f.sessions).password("OldSynthetic123!", "NewSyntheticPassword123!")
            }.exceptionOrNull() as ApiFailure
            assertEquals("INVALID_RESPONSE", error.code)
            assertSame(original, f.store.saved)
        }
    }
}
