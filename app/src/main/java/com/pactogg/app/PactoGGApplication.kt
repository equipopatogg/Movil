package com.pactogg.app

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.pactogg.app.core.network.EvidenceImageClient
import com.pactogg.app.core.offline.OfflineCache
import com.pactogg.app.core.push.PushRuntime
import com.pactogg.app.core.session.SessionManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltAndroidApp
class PactoGGApplication : Application(), SingletonImageLoader.Factory {
    @Inject lateinit var push: PushRuntime
    @Inject lateinit var offline: OfflineCache
    @Inject lateinit var sessions: SessionManager

    override fun onCreate() {
        super.onCreate()
        push.start()
        // PL-12: al cerrar sesión se borra lo guardado para leer sin conexión.
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            var last: String? = null
            sessions.current.map { it?.account?.id }.distinctUntilChanged().collect { id ->
                if (id == null && last != null) offline.clear()
                last = id
            }
        }
    }

    override fun newImageLoader(context: Context): ImageLoader = ImageLoader.Builder(context)
        .components { add(OkHttpNetworkFetcherFactory(callFactory = { EvidenceImageClient.create() })) }
        .diskCache(null)
        .memoryCache(null)
        .build()
}
