package com.pactogg.app.core.offline

import com.pactogg.app.core.database.SnapshotDao
import com.pactogg.app.core.database.SnapshotEntity
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.ByteString.Companion.decodeBase64

/** Dónde se guardan las respuestas. En la app es Room; en pruebas, un mapa. */
interface SnapshotStore {
    fun save(key: String, owner: String, body: String)
    fun find(key: String, owner: String): String?
    fun clear()
}

class RoomSnapshotStore(private val dao: SnapshotDao, private val now: () -> Long = System::currentTimeMillis) :
    SnapshotStore {
    override fun save(key: String, owner: String, body: String) = dao.save(SnapshotEntity(key, owner, body, now()))
    override fun find(key: String, owner: String): String? = dao.find(key, owner)?.body
    override fun clear() = dao.clear()
}

private const val MaxBody = 512L * 1024
private val OwnerId = Regex("[a-fA-F0-9]{24}")
private val Excluded = listOf("/api/v1/auth/", "/api/v1/devices/", "/api/v1/users/check-nickname")
private val JsonType = "application/json".toMediaType()

/**
 * Modo sin conexión (PL-12): guarda la última respuesta 200 de cada consulta GET de la API por cuenta y,
 * si no hay red, la devuelve para leer. Las acciones que cambian datos no se guardan ni se repiten.
 */
class OfflineCache(private val store: SnapshotStore) : Interceptor {
    private val mutableOffline = MutableStateFlow(false)
    val offline = mutableOffline.asStateFlow()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        val owner = ownerOf(request.header("Authorization"))
        val cacheable = request.method == "GET" && path.startsWith("/api/v1/") &&
            Excluded.none { path.startsWith(it) }
        return if (owner != null && cacheable) {
            remembered(chain, path + request.url.encodedQuery?.let { "?$it" }.orEmpty(), owner)
        } else {
            chain.proceed(request)
        }
    }

    private fun remembered(chain: Interceptor.Chain, key: String, owner: String): Response {
        val request = chain.request()
        val response = try {
            chain.proceed(request)
        } catch (error: IOException) {
            val saved = runCatching { store.find(key, owner) }.getOrNull() ?: throw error
            mutableOffline.value = true
            Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(200)
                .message("OK").header("X-Pacto-Offline", "1").body(saved.toResponseBody(JsonType)).build()
        }
        if (response.header("X-Pacto-Offline") == null) {
            mutableOffline.value = false
            if (response.code == 200 && response.body?.contentType()?.subtype == "json") {
                val body = response.peekBody(MaxBody + 1).string()
                if (body.length <= MaxBody) runCatching { store.save(key, owner, body) }
            }
        }
        return response
    }

    /** Al cerrar sesión no queda nada de la cuenta en el teléfono. */
    fun clear() {
        runCatching { store.clear() }
        mutableOffline.value = false
    }
}

/** El `sub` del token de acceso (id de la cuenta). Sin `sub` válido no se guarda nada. */
internal fun ownerOf(header: String?): String? = header?.removePrefix("Bearer ")?.split('.')?.getOrNull(1)
    ?.decodeBase64()?.utf8()
    ?.let { json -> runCatching { Json.parseToJsonElement(json).jsonObject["sub"]?.jsonPrimitive?.content } }
    ?.getOrNull()
    ?.takeIf { it.matches(OwnerId) }
