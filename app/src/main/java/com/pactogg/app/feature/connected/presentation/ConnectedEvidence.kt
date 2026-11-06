package com.pactogg.app.feature.connected.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.pactogg.app.feature.connected.data.PhotoPreparer
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.Field
import com.pactogg.app.prototype.PactCard

@Composable
fun ConnectedEvidence(state: ConnectedState, model: ConnectedViewModel) {
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) model.preparePhoto { PhotoPreparer(context).prepare(uri) }
    }
    EvidenceContent(state,
        onPick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        onUpload = model::uploadSelectedPhoto, onLink = model::sendLink, onEdit = model::link,
        onRefresh = model::loadEvidence, onDiscard = model::discardPhoto)
}

@Composable
internal fun EvidenceContent(state: ConnectedState, onPick: () -> Unit, onUpload: () -> Unit,
    onLink: () -> Unit, onEdit: (String) -> Unit, onRefresh: () -> Unit, onDiscard: () -> Unit) {
    var confirmLink by rememberSaveable(state.contract?.id) { mutableStateOf(false) }
    AppPage("Demuestra\ntu reto.", "Comparte una captura o un enlace") {
        if (state.contract == null) Text("Abre un reto para consultar o enviar sus evidencias.")
        else {
            if (state.canUploadEvidence()) {
                PactCard("Foto", "Elige una captura JPEG, PNG o WebP. La prepararemos antes de enviarla.") {
                    PhotoSelection(state, onPick, onUpload, onDiscard)
                }
                PactCard("Enlace", "YouTube, Twitch, Medal o Streamable. No se suben archivos de video.") {
                    Field("Enlace HTTPS", state.link, onEdit)
                    Action("Revisar enlace", enabled = !state.busy && state.link.isNotBlank()) { confirmLink = true }
                }
            } else Text(if (state.contractStateStale)
                "Actualiza el reto antes de enviar otra evidencia. Si ya enviaste una, no la repitas."
                else "Puedes ver las evidencias. Solo quien cumple el reto puede enviarlas " +
                    "mientras el plazo siga abierto.")
            Action("Actualizar evidencias", enabled = !state.busy, onClick = onRefresh)
            if (state.evidenceUnavailable) Text("Por ahora no podemos mostrar las evidencias guardadas. " +
                "Si ya enviaste una, no la repitas.")
            if (state.evidenceLoaded && state.evidenceItems.isEmpty()) Text("Este reto todavía no tiene evidencias.")
            state.evidenceItems.forEach { EvidenceCard(it) }
            state.evidence?.let { Text("Tu última evidencia fue recibida por el servidor.") }
            if (state.contractStateStale) Text("Vuelve al detalle del reto y pulsa Actualizar reto " +
                "para consultar su estado.")
        }
    }
    if (confirmLink) ActionConfirmation("¿Enviar este enlace?",
        "Se revisará el contenido antes de guardarlo. Contará como una de las tres evidencias del reto.",
        "Enviar enlace", !state.busy, { confirmLink = false; onLink() }, { confirmLink = false },
        confirmEnabled = !state.busy && state.canUploadEvidence())
}

@Composable
private fun PhotoSelection(state: ConnectedState, onPick: () -> Unit, onUpload: () -> Unit, onDiscard: () -> Unit) {
    val photo = state.selectedPhoto
    if (photo == null) {
        Text("Hasta 8000 píxeles por lado en el original. El archivo enviado no superará 5 MB.")
        Action("Elegir foto", enabled = !state.busy, onClick = onPick)
    } else {
        EvidenceImage(photo.bytes, "Vista previa de la foto elegida")
        Text("Lista para enviar · ${photo.width} × ${photo.height} px · ${(photo.bytes.size + 1023) / 1024} KB")
        Text("La foto se revisará antes de guardarse. Una vez enviada, contará como evidencia del reto.")
        Action("Enviar foto", enabled = !state.busy, onClick = onUpload)
        Action("Elegir otra foto", enabled = !state.busy, onClick = onPick)
        Action("Quitar foto", enabled = !state.busy, onClick = onDiscard)
    }
}
