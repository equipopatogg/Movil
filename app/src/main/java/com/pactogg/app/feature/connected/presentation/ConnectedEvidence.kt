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
import androidx.compose.material3.MaterialTheme
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.EmptyState
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.SafetyNote
import com.pactogg.app.core.designsystem.SectionTitle
import com.pactogg.app.ui.theme.PactoCyan

@Composable
fun ConnectedEvidence(state: ConnectedState, model: ConnectedViewModel) {
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) model.preparePhoto { PhotoPreparer(context).prepare(uri) }
    }
    EvidenceContent(state,
        onPick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        onUpload = model::uploadSelectedPhoto, onLink = model::sendLink, onEdit = model::link,
        onRefresh = model::loadEvidence, onDiscard = model::discardPhoto, onBack = { model.back() })
}

/** Evidencias con el estilo de la maqueta (`Subir evidencia`): foto o enlace y lo ya recibido. */
@Composable
internal fun EvidenceContent(state: ConnectedState, onPick: () -> Unit, onUpload: () -> Unit,
    onLink: () -> Unit, onEdit: (String) -> Unit, onRefresh: () -> Unit, onDiscard: () -> Unit,
    onBack: (() -> Unit)? = null) {
    var confirmLink by rememberSaveable(state.contract?.id) { mutableStateOf(false) }
    MakePage("Subir evidencia", "Lo que ocurrió, respaldado por pruebas", onBack = onBack) {
        if (state.contract == null) {
            EmptyState("No hay reto abierto.", "Abre un reto para consultar o enviar sus evidencias.")
        } else {
            if (state.canUploadEvidence()) {
                MakeCard {
                    Kicker("Foto", PactoCyan)
                    Text("Elige una captura JPEG, PNG o WebP. La prepararemos antes de enviarla.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    PhotoSelection(state, onPick, onUpload, onDiscard)
                }
                MakeCard {
                    Kicker("Enlace", PactoCyan)
                    Text("YouTube, Twitch, Medal o Streamable. No se suben archivos de video.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    MakeField("Enlace HTTPS", state.link, onEdit)
                    MakeButton("Revisar enlace", ButtonKind.Secondary,
                        enabled = !state.busy && state.link.isNotBlank()) { confirmLink = true }
                }
            } else SafetyNote(if (state.contractStateStale)
                "Actualiza el reto antes de enviar otra evidencia. Si ya enviaste una, no la repitas."
                else "Puedes ver las evidencias. Solo quien cumple el reto puede enviarlas " +
                    "mientras el plazo siga abierto.")
            SectionTitle("¿Qué puede revisar el jurado?", "Evidencia recibida")
            MakeButton("Actualizar evidencias", ButtonKind.Ghost, enabled = !state.busy, onClick = onRefresh)
            if (state.evidenceUnavailable) SafetyNote("Por ahora no podemos mostrar las evidencias guardadas. " +
                "Si ya enviaste una, no la repitas.")
            if (state.evidenceLoaded && state.evidenceItems.isEmpty()) {
                EmptyState("Todavía no hay evidencias.", "Cuando alguien suba una foto o enlace aparecerá aquí.")
            }
            state.evidenceItems.forEach { EvidenceCard(it) }
            state.evidence?.let { SafetyNote("Tu última evidencia fue recibida por el servidor.") }
            if (state.contractStateStale) SafetyNote("Vuelve al detalle del reto y pulsa Actualizar reto " +
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
        Text("Hasta 8000 píxeles por lado en el original. El archivo enviado no superará 5 MB.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        MakeButton("Elegir foto", enabled = !state.busy, onClick = onPick)
    } else {
        EvidenceImage(photo.bytes, "Vista previa de la foto elegida")
        Text("Lista para enviar · ${photo.width} × ${photo.height} px · ${(photo.bytes.size + 1023) / 1024} KB",
            style = MaterialTheme.typography.bodySmall)
        Text("La foto se revisará antes de guardarse. Una vez enviada, contará como evidencia del reto.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        MakeButton("Enviar foto", enabled = !state.busy, onClick = onUpload)
        MakeButton("Elegir otra foto", ButtonKind.Ghost, enabled = !state.busy, onClick = onPick)
        MakeButton("Quitar foto", ButtonKind.Ghost, enabled = !state.busy, onClick = onDiscard)
    }
}
