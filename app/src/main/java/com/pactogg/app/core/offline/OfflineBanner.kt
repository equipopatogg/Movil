package com.pactogg.app.core.offline

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pactogg.app.PactoGGApplication

/** Aviso fijo mientras se muestran datos guardados (PL-12 RF3). */
@Composable
fun OfflineBanner() {
    val app = LocalContext.current.applicationContext as? PactoGGApplication ?: return
    val offline by app.offline.offline.collectAsStateWithLifecycle()
    if (!offline) return
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondaryContainer) {
        Text("Sin conexión: ves lo último guardado. Para crear, aceptar o votar necesitas internet.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
    }
}
