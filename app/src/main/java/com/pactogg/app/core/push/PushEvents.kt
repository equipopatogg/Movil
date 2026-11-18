package com.pactogg.app.core.push

import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Tocar un aviso abre Notificaciones (PL-10 RF6). Solo guarda una bandera, nunca el contenido. */
object PushEvents {
    private val pending = MutableStateFlow(false)
    val openNotices = pending.asStateFlow()

    fun handle(intent: Intent?) {
        val fromPush = intent?.action == PushOpen || intent?.extras?.containsKey("google.message_id") == true
        if (fromPush) pending.value = true
    }

    fun consume() { pending.value = false }
}
