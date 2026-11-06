package com.pactogg.app.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import retrofit2.Response

@Serializable
class Envelope<T>(val data: T, val meta: PageMeta? = null)

@Serializable
data class PageMeta(
    @SerialName("has_more") val hasMore: Boolean,
    @SerialName("next_cursor") val nextCursor: String? = null,
)

@Serializable
private class WireError(val code: String = "", val details: JsonElement? = null, val message: String? = null,
    @SerialName("request_id") val requestId: String? = null)

private val ErrorJson = Json { ignoreUnknownKeys = true }

/** No raw body, URL, token or cause. Only bounded evidence conflicts may carry public display text. */
class ApiFailure(val status: Int, val code: String, val requestId: String? = null,
    val displayMessage: String? = null) : Exception(code)

fun <T> Response<T>.checked(expected: Int): T {
    if (code() != expected) throw failure()
    return body() ?: throw ApiFailure(0, "INVALID_RESPONSE")
}

fun Response<*>.failure(): ApiFailure {
    val error = errorBody()?.use { body ->
        // Bound error processing even when the proxy sends an unexpected response.
        val source = body.source()
        if (source.request(16385)) null else source.readUtf8()
    }
    val parsed = error?.let { text ->
        runCatching { ErrorJson.decodeFromString<WireError>(text) }.getOrNull()
    }
    val safe = parsed?.code?.takeIf { it.matches(Regex("[A-Z_]{1,64}")) } ?: "HTTP_ERROR"
    val requestId = parsed?.requestId?.takeIf { it.matches(Regex("[A-Za-z0-9_-]{1,64}")) }
    val display = parsed?.message?.takeIf {
        code() == 409 && raw().request.url.encodedPath.endsWith("/evidence") &&
            it.length in 1..240 && it.none(Char::isISOControl) &&
            !Regex("(?i)(bearer|token|password|secret|https?://|[A-Za-z0-9_-]{40,})").containsMatchIn(it)
    }
    return ApiFailure(code(), safe, requestId, display)
}
