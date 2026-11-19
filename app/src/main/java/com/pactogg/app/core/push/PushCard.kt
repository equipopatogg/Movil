package com.pactogg.app.core.push

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pactogg.app.PactoGGApplication
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.Tone

private val StateText = mapOf(
    PushConnectionState.NOT_CONFIGURED to "Esta instalación no tiene Firebase configurado.",
    PushConnectionState.SIGN_IN_REQUIRED to "Inicia sesión para registrar este teléfono.",
    PushConnectionState.PERMISSION_REQUIRED to "Las notificaciones están bloqueadas en este teléfono.",
    PushConnectionState.CONNECTING to "Conectando con Firebase…",
    PushConnectionState.REGISTERING to "Registrando este teléfono en PactoGG…",
    PushConnectionState.REGISTERED to "Este teléfono recibe avisos de retos, votos y torneos.",
    PushConnectionState.TOKEN_ERROR to "No se pudo conectar con Firebase. Revisa internet y Google Play.",
    PushConnectionState.SERVER_ERROR to "No se pudo registrar este teléfono. Revisa la conexión.",
)

/** Estado de los avisos y acceso al permiso desde el perfil (PL-10 RF4). */
@Composable
fun PushCard() {
    val context = LocalContext.current
    val push = (context.applicationContext as? PactoGGApplication)?.push ?: return
    val state by push.connection.collectAsStateWithLifecycle()
    var allowed by remember { mutableStateOf(pushAllowed(context)) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                allowed = pushAllowed(context)
                push.refresh()
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
    MakeCard {
        Kicker("Notificaciones push")
        Pill(if (state == PushConnectionState.REGISTERED) "Activas" else "Inactivas",
            if (state == PushConnectionState.REGISTERED) Tone.Cyan else Tone.Muted)
        Text(StateText.getValue(state), style = MaterialTheme.typography.bodySmall)
        if (!allowed) MakeButton("Abrir ajustes de notificaciones", ButtonKind.Secondary) {
            context.startActivity(settingsIntent(context.packageName))
        }
        if (allowed && (state == PushConnectionState.TOKEN_ERROR || state == PushConnectionState.SERVER_ERROR)) {
            MakeButton("Reintentar", ButtonKind.Ghost) { push.refresh() }
        }
    }
}

private fun settingsIntent(pkg: String): Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, pkg)
} else {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", pkg, null))
}
