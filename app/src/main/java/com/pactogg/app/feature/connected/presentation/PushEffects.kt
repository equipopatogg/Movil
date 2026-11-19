package com.pactogg.app.feature.connected.presentation

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pactogg.app.PactoGGApplication
import com.pactogg.app.core.push.PushEvents
import com.pactogg.app.core.push.pushAllowed

private const val PushPrefs = "pactogg_push"
private const val AskedKey = "permission_asked"

/** Con sesión: pide el permiso una sola vez (PL-10 RF4) y abre Notificaciones al tocar un aviso (RF6). */
@Composable
internal fun PushEffects(model: ConnectedViewModel) {
    val context = LocalContext.current
    val open by PushEvents.openNotices.collectAsStateWithLifecycle()
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        (context.applicationContext as? PactoGGApplication)?.push?.refresh()
    }
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences(PushPrefs, Context.MODE_PRIVATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !pushAllowed(context) &&
            !prefs.getBoolean(AskedKey, false)) {
            prefs.edit { putBoolean(AskedKey, true) }
            permission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    LaunchedEffect(open) {
        if (open) {
            PushEvents.consume()
            model.navigate(ConnectedRoute.NOTIFICATIONS)
        }
    }
}
