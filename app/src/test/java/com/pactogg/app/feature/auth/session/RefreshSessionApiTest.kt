package com.pactogg.app.feature.auth.session
import com.pactogg.app.testing.shared.*

import kotlinx.coroutines.async
import okhttp3.ResponseBody.Companion.toResponseBody


import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.Endpoint
import com.pactogg.app.testing.shared.Refresh
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** AUTH-03: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(IntegrationChecks::class)
class RefreshSessionApiTest {
    @Test fun AUTH_03_sendsRefreshTokenAndPersistsRotatedToken() = runBlocking {
        ApiTestFixture().use { fixture ->
            fixture.prepare(true)
            fixture.enqueueSuccess(Endpoint.REFRESH)
            fixture.now = 200000; fixture.sessions.restore()
            val request = fixture.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/auth/refresh", request.path?.substringBefore('?'))
            assertNull(request.getHeader("Authorization"))
            val data = request.body.readUtf8().takeIf { it.isNotEmpty() }?.let { Json.parseToJsonElement(it).jsonObject }
            assertEquals(Refresh, data?.get("refresh_token")?.jsonPrimitive?.content)
            assertEquals("new-synthetic-refresh", fixture.store.saved?.refreshToken)
            assertEquals(1, fixture.server.requestCount)
        }
    }

    @Test fun AUTH_03_twoConcurrent401ResponsesShareOneRotationAndRetryReadsOnce() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.enqueueSuccess(Endpoint.REFRESH)
            val both = java.util.concurrent.atomic.AtomicInteger()
            val ready = kotlinx.coroutines.CompletableDeferred<Unit>()
            suspend fun read(): Int {
                var calls = 0
                val result = f.sessions.authorized(readOnly = true) { bearer ->
                    calls++
                    if (calls == 1) {
                        assertEquals("Bearer synthetic-access-fixture", bearer)
                        if (both.incrementAndGet() == 2) ready.complete(Unit)
                        ready.await()
                        retrofit2.Response.error<String>(401, "".toResponseBody())
                    } else {
                        assertEquals("Bearer new-synthetic-access", bearer)
                        retrofit2.Response.success("ok")
                    }
                }
                assertEquals("ok", result.body()); return calls
            }
            val first = async { read() }; val second = async { read() }
            assertEquals(2, first.await()); assertEquals(2, second.await())
            assertEquals(1, f.server.requestCount)
            assertEquals("/api/v1/auth/refresh", f.server.takeRequest().path)
            assertEquals("new-synthetic-refresh", f.store.saved?.refreshToken)
        }
    }
    @Test fun AUTH_03_logoutDuringPendingRefreshEndsWithNoRestoredSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            val arrived = java.util.concurrent.CountDownLatch(1)
            val release = java.util.concurrent.CountDownLatch(1)
            f.server.dispatcher = object : okhttp3.mockwebserver.Dispatcher() {
                override fun dispatch(request: okhttp3.mockwebserver.RecordedRequest): okhttp3.mockwebserver.MockResponse {
                    if (request.path == "/api/v1/auth/refresh") {
                        arrived.countDown(); check(release.await(5, java.util.concurrent.TimeUnit.SECONDS))
                        return ApiTestFixture.json(ApiTestFixture.sessionJson)
                    }
                    return okhttp3.mockwebserver.MockResponse().setResponseCode(204)
                }
            }
            f.now = 200000
            val refresh = async { runCatching { f.sessions.restore() } }
            assertTrue(kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) { arrived.await(5, java.util.concurrent.TimeUnit.SECONDS) })
            val closing = async(start = kotlinx.coroutines.CoroutineStart.UNDISPATCHED) { f.sessions.logout() }
            try { release.countDown(); refresh.await(); assertTrue(closing.await()) }
            finally { release.countDown() }
            assertNull(f.sessions.current.value); assertNull(f.store.saved)
            assertEquals(2, f.server.requestCount)
            f.sessions.restore(); assertNull(f.sessions.current.value)
        }
    }


    @Test fun AUTH_03_lostResponseIsNotReplayedOrReportedAsSuccess() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(true); val before = f.store.saved
            f.server.enqueue(okhttp3.mockwebserver.MockResponse().setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST))
            assertTrue(runCatching { f.now = 200000; f.sessions.restore() }.exceptionOrNull() is java.io.IOException)
            assertNull(f.store.saved); assertEquals(1, f.server.requestCount)
        }
    }

}
