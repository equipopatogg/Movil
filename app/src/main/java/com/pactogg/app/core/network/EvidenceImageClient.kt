package com.pactogg.app.core.network

import okhttp3.OkHttpClient

/** CDN requests have no session interceptor, Authorization header, cookie jar or redirects. */
object EvidenceImageClient {
    fun create(): OkHttpClient = ApiClientFactory.defaultClient().newBuilder()
        .addInterceptor { chain ->
            check(chain.request().url.isHttps)
            chain.proceed(chain.request().newBuilder().removeHeader("Authorization").build())
        }.build()
}
