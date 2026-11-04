package com.pactogg.app.core.network

import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/** No request is sent until an approved API interface is explicitly invoked. */
class ApiClientFactory @Inject constructor() {
    fun create(baseUrl: String, client: OkHttpClient = defaultClient()): Retrofit {
        require(isValidBaseUrl(baseUrl)) { "La API requiere HTTPS, sin credenciales y con / final." }
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
        return Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
    }

    companion object {
        fun isValidBaseUrl(value: String): Boolean {
            val url = value.toHttpUrlOrNull() ?: return false
            return url.isHttps && url.username.isEmpty() && url.password.isEmpty() &&
                url.query == null && url.fragment == null && url.encodedPath.endsWith("/")
        }

        fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(45, TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
    }
}
