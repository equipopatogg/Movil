package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.pactogg.app.feature.connected.domain.Evidence
import com.pactogg.app.feature.connected.domain.EvidenceRules
import com.pactogg.app.feature.connected.domain.InputRules
import androidx.compose.material3.MaterialTheme
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard

@Composable
internal fun EvidenceCard(evidence: Evidence) {
    MakeCard {
        Kicker(if (evidence.type == "photo") "Foto del reto" else "Enlace del reto")
        Text("Contenido aprobado por moderación", style = MaterialTheme.typography.bodySmall)
        if (evidence.type == "photo") EvidencePhoto(evidence)
        else {
            val handler = LocalUriHandler.current
            var unavailable by remember(evidence.url) { mutableStateOf(false) }
            val url = evidence.url?.takeIf(InputRules::evidenceLink)
            if (url == null) Text("El enlace no está disponible.")
            else MakeButton("Abrir enlace") { unavailable = runCatching { handler.openUri(url) }.isFailure }
            if (unavailable) Text("No encontramos una aplicación para abrir el enlace.")
        }
    }
}

@Composable
private fun EvidencePhoto(evidence: Evidence) {
    val url = EvidenceRules.imageUrl(evidence.url)
    when {
        EvidenceRules.expired(evidence.expiresAt) -> Text("Esta foto alcanzó su fecha de vencimiento.")
        url == null -> Text("La foto se recibió. Actualiza las evidencias para obtener su imagen.")
        else -> {
            EvidenceImage(url, "Foto enviada como evidencia del reto")
            evidence.expiresAt?.let { Text("Disponible hasta: $it") }
        }
    }
}

@Composable
internal fun EvidenceImage(data: Any, description: String) {
    var failed by remember(data) { mutableStateOf(false) }
    var loading by remember(data) { mutableStateOf(true) }
    val context = LocalContext.current
    val request = remember(context, data) { ImageRequest.Builder(context).data(data).size(1024)
        .memoryCachePolicy(CachePolicy.DISABLED).diskCachePolicy(CachePolicy.DISABLED).build() }
    Box(Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center) {
        AsyncImage(model = request, contentDescription = description,
            modifier = Modifier.fillMaxWidth().height(220.dp), contentScale = ContentScale.Fit,
            onLoading = { loading = true }, onSuccess = { loading = false; failed = false },
            onError = { loading = false; failed = true })
        if (loading) CircularProgressIndicator()
        if (failed) Text("No se pudo mostrar la imagen. Actualiza las evidencias para intentarlo de nuevo.")
    }
}
