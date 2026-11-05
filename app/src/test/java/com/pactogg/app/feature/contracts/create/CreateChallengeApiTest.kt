package com.pactogg.app.feature.contracts.create
import com.pactogg.app.testing.shared.*

import com.pactogg.app.core.network.ApiFailure


import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import com.pactogg.app.testing.shared.GroupFixture
import com.pactogg.app.testing.shared.GroupId
import com.pactogg.app.testing.shared.OtherId
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class CreateChallengeApiTest {
    @Test fun CON_01_sendsGroupAndTargetForChallengeCreation() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.CREATE_CONTRACT)
            fixture.products.createChallenge(GroupFixture, ChallengeDraft("Halo", "Win a game", "Choose next game", "2099-11-10T23:59:00Z", 25, OtherId))
            val request = fixture.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/contracts", request.path?.substringBefore('?'))
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertEquals(OtherId, data?.get("target_id")?.jsonPrimitive?.content)
            assertEquals(GroupId, data?.get("group_id")?.jsonPrimitive?.content)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun CON_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity() = runBlocking {
        for ((status, code) in listOf(422 to "CONTENT_REJECTED", 503 to "MODERATION_UNAVAILABLE")) {
            ApiTestFixture().use { f ->
                f.prepare(true); val before = f.store.saved
                f.server.enqueue(ApiTestFixture.json("""{"code":"$code","message":"private-server-detail"}""", status))
                val error = runCatching { f.products.createChallenge(GroupFixture, com.pactogg.app.feature.connected.domain.ChallengeDraft("Halo", "Win a game", "Choose next game", "2099-11-10T23:59:00Z", 25)) }.exceptionOrNull() as ApiFailure
                assertEquals(status, error.status); assertEquals(code, error.code)
                assertFalse(error.toString().contains("private-server-detail"))
                assertEquals(before, f.store.saved)
                assertEquals(1, f.server.requestCount)
            }
        }
    }


    @Test fun CON_01_lostResponseIsNotReplayedOrReportedAsSuccess() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(true); val before = f.store.saved
            f.server.enqueue(okhttp3.mockwebserver.MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST))
            assertTrue(runCatching { f.products.createChallenge(GroupFixture, com.pactogg.app.feature.connected.domain.ChallengeDraft("Halo", "Win a game", "Choose next game", "2099-11-10T23:59:00Z", 25)) }.exceptionOrNull() is java.io.IOException)
            assertEquals(before, f.store.saved); assertEquals(1, f.server.requestCount)
        }
    }

}
