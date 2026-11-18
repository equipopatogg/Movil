package com.pactogg.app.core.offline

import com.pactogg.app.testing.IntegrationChecks
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.ByteString.Companion.encodeUtf8
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-12: lectura sin conexión con la última respuesta guardada, separada por cuenta. */
@Category(IntegrationChecks::class)
class OfflineCacheTest {
    private class MapStore : SnapshotStore {
        val items = mutableMapOf<Pair<String, String>, String>()
        override fun save(key: String, owner: String, body: String) { items[key to owner] = body }
        override fun find(key: String, owner: String) = items[key to owner]
        override fun clear() = items.clear()
    }

    private val userA = "111111111111111111111111"
    private val userB = "444444444444444444444444"
    private fun bearer(sub: String) = "Bearer h." +
        """{"sub":"$sub"}""".encodeUtf8().base64Url().trimEnd('=') + ".s"

    private fun withServer(block: (MockWebServer, OkHttpClient, OfflineCache, MapStore) -> Unit) {
        val server = MockWebServer()
        server.start()
        val store = MapStore()
        val cache = OfflineCache(store)
        val client = OkHttpClient.Builder().addInterceptor(cache).retryOnConnectionFailure(false).build()
        try { block(server, client, cache, store) } finally { server.shutdown() }
    }

    private fun get(server: MockWebServer, client: OkHttpClient, path: String, sub: String) =
        client.newCall(Request.Builder().url(server.url(path)).header("Authorization", bearer(sub)).build()).execute()

    private val json = """{"data":[{"id":"g1"}]}"""
    private fun ok() = MockResponse().setHeader("Content-Type", "application/json").setBody(json)
    private fun down() = MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)

    @Test fun PL_12_withoutNetworkReturnsTheLastSavedReadAndMarksOffline() = withServer { server, client, cache, _ ->
        server.enqueue(ok())
        get(server, client, "/api/v1/groups?limit=20", userA).use { assertEquals(json, it.body?.string()) }
        assertFalse(cache.offline.value)
        server.enqueue(down())
        get(server, client, "/api/v1/groups?limit=20", userA).use {
            assertEquals(200, it.code); assertEquals("1", it.header("X-Pacto-Offline"))
            assertEquals(json, it.body?.string())
        }
        assertTrue(cache.offline.value)
        server.enqueue(ok())
        get(server, client, "/api/v1/groups?limit=20", userA).close()
        assertFalse(cache.offline.value)
    }

    @Test fun PL_12_anotherAccountNeverSeesSavedDataAndClearRemovesIt() = withServer { server, client, cache, store ->
        server.enqueue(ok())
        get(server, client, "/api/v1/groups", userA).close()
        server.enqueue(down())
        assertTrue(runCatching { get(server, client, "/api/v1/groups", userB) }.exceptionOrNull() is IOException)
        cache.clear()
        assertTrue(store.items.isEmpty())
    }

    @Test fun PL_12_writesAuthAndUnsignedCallsAreNotSaved() = withServer { server, client, _, store ->
        server.enqueue(ok())
        client.newCall(Request.Builder().url(server.url("/api/v1/contracts")).header("Authorization", bearer(userA))
            .post("{}".toRequestBody("application/json".toMediaType())).build()).execute().close()
        server.enqueue(ok())
        get(server, client, "/api/v1/auth/session", userA).close()
        server.enqueue(ok())
        client.newCall(Request.Builder().url(server.url("/api/v1/groups")).build()).execute().close()
        assertTrue(store.items.isEmpty())
        assertNull(ownerOf("Bearer not-a-jwt"))
    }
}
