package com.pactogg.app.feature.profile.detail

import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import com.pactogg.app.testing.shared.UserId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** USR-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class ProfileDetailsApiTest {
    @Test fun USR_02_mapsOwnProfileIdentity() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.PROFILE)
            val result = fixture.social.profile()
            val request = fixture.server.takeRequest()
            assertEquals("GET", request.method)
            assertEquals("/api/v1/users/me", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            assertEquals(UserId, result.account.id)
            assertEquals(1, fixture.server.requestCount)
        }
    }
}
