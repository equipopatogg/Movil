package com.pactogg.app

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.pactogg.app.core.network.EvidenceImageClient
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PactoGGApplication : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: Context): ImageLoader = ImageLoader.Builder(context)
        .components { add(OkHttpNetworkFetcherFactory(callFactory = { EvidenceImageClient.create() })) }
        .diskCache(null)
        .memoryCache(null)
        .build()
}
