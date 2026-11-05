package com.pactogg.app.feature.connected.presentation

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.pactogg.app.feature.connected.data.ApiProductRepository
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.Field
import com.pactogg.app.prototype.PactCard
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ConnectedEvidence(state: ConnectedState, model: ConnectedViewModel) {
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) model.sendPhoto { readSelectedPhoto(context, uri) }
    }
    AppPage("Demuestra\ntu reto.", "Evidencia por enlace o foto") {
        if (state.contract == null) Text("No hay un reto confirmado para enviar evidencia.")
        else {
            PactCard("Enlace", "YouTube, Twitch, Medal o Streamable mediante HTTPS.") {
                Field("Enlace HTTPS", state.link, model::link)
                Action("Enviar enlace", enabled = !state.busy) { model.sendLink() }
            }
            PactCard("Foto", "Selecciona JPEG, PNG o WebP de hasta 5 MiB. Se enviará para moderación.") {
                Action("Seleccionar y enviar foto", enabled = !state.busy) {
                    if (!state.busy) picker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
            state.evidence?.let {
                Text("Evidencia confirmada: ${it.type} · Moderación: ${it.moderation}")
            }
            if (state.contractStateStale) Text("Evidencia enviada; estado actual del reto pendiente de consulta. " +
                "No repitas el envío para actualizarlo.")
            Text("La validación y autorización definitivas corresponden al servidor. No se cargan videos.")
        }
    }
}

private suspend fun readSelectedPhoto(context: Context, uri: Uri): Pair<ByteArray, String> =
    withContext(Dispatchers.IO) {
        val mime = context.contentResolver.getType(uri).orEmpty()
        require(mime in ApiProductRepository.AllowedPhotoTypes)
        val bytes = context.contentResolver.openInputStream(uri)?.use { input ->
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(8192)
            var count = input.read(buffer)
            while (count != -1) {
                require(result.size() + count <= ApiProductRepository.MaxPhotoBytes)
                result.write(buffer, 0, count)
                count = input.read(buffer)
            }
            result.toByteArray()
        } ?: error("IMAGE_UNAVAILABLE")
        require(bytes.isNotEmpty())
        bytes to mime
    }
