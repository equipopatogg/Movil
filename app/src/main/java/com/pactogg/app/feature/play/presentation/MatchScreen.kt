package com.pactogg.app.feature.play.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.EmptyState
import com.pactogg.app.core.designsystem.InfoRow
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.SafetyNote
import com.pactogg.app.core.designsystem.localDate
import com.pactogg.app.core.designsystem.statusLabel
import com.pactogg.app.core.designsystem.statusTone
import com.pactogg.app.feature.assist.data.MlKitScoreReader
import com.pactogg.app.feature.connected.data.PhotoPreparer
import com.pactogg.app.feature.connected.presentation.ConnectedState
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.back
import com.pactogg.app.feature.play.domain.PlayMatch
import com.pactogg.app.feature.play.domain.TournamentDetail
import com.pactogg.app.ui.theme.PactoCyan

/** Partida: reportar, confirmar, disputar o resolver (maqueta `torneo/reportar` y `torneo/confirmar`, PL-04 RF4). */
@Composable
internal fun MatchScreen(state: ConnectedState, model: ConnectedViewModel) {
    val match = state.play.match
    val tournament = state.play.tournament
    MakePage("Partida", tournament?.game, onBack = { model.back() }) {
        if (match == null || tournament == null) {
            EmptyState("No hay partida abierta.", "Ábrela desde el cuadro del torneo.", R.drawable.ic_pacto_trophy)
            return@MakePage
        }
        MatchSummary(match, tournament)
        MatchActions(state, model, tournament, match)
        SafetyNote("No hay empates: si quedan iguales, jueguen el desempate y reporten al ganador.")
    }
}

@Composable
private fun MatchActions(state: ConnectedState, model: ConnectedViewModel, tournament: TournamentDetail,
    match: PlayMatch) {
    val myGroup = state.group?.id
    val playing = myGroup != null && (myGroup == match.p1 || myGroup == match.p2)
    val ready = match.p1 != null && match.p2 != null
    when {
        match.status == "pending" && playing && ready ->
            ReportForm(state, model, tournament, match)
        match.status == "reported" && playing && match.reportedBy != myGroup -> RivalAnswer(state, model)
        match.status == "reported" && playing -> Kicker("Esperando que el otro grupo confirme (24 h).", PactoCyan)
        else -> Unit
    }
    if (ready) OrganizerResolve(state, model, tournament, match)
}

@Composable
private fun MatchSummary(match: PlayMatch, tournament: TournamentDetail) {
    MakeCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Kicker("Ronda ${match.round} · Partida ${match.position}")
            Pill(statusLabel(match.status), statusTone(match.status))
        }
        Text("${tournament.groupName(match.p1)}  ${match.p1Score ?: "—"}", style = MaterialTheme.typography.titleMedium)
        Text("${tournament.groupName(match.p2)}  ${match.p2Score ?: "—"}", style = MaterialTheme.typography.titleMedium)
        match.winner?.let { InfoRow("Ganador", tournament.groupName(it)) }
        match.reportedBy?.let { InfoRow("Reportó", tournament.groupName(it)) }
        match.reportedAt?.let { InfoRow("Reportado", localDate(it)) }
        match.disputeReason?.let { InfoRow("Motivo de disputa", it) }
        match.screenshotUrl?.let { url ->
            AsyncImage(url, "Foto del resultado", Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop)
        }
    }
}

@Composable
private fun ReportForm(state: ConnectedState, model: ConnectedViewModel, tournament: TournamentDetail,
    match: PlayMatch) {
    val context = LocalContext.current
    val play = state.play
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) model.prepareReportPhoto { PhotoPreparer(context).prepare(uri) }
    }
    // PL-14 (ML): ML Kit lee el marcador de la captura en el teléfono y lo propone si los campos están vacíos.
    val reader = remember { MlKitScoreReader() }
    var detected by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(play.reportPhoto) {
        val photo = play.reportPhoto ?: return@LaunchedEffect
        detected = null
        val score = runCatching { reader.read(photo.bytes) }.getOrNull() ?: return@LaunchedEffect
        detected = "${score.first} – ${score.second}"
        val current = model.state.value.play
        if (current.scoreFirst.isBlank() && current.scoreSecond.isBlank()) {
            model.scores(score.first.toString(), score.second.toString())
        }
    }
    MakeCard {
        Text("Registrar resultado", style = MaterialTheme.typography.titleMedium)
        MakeField(tournament.groupName(match.p1), play.scoreFirst, { model.scores(it, play.scoreSecond) },
            number = true)
        MakeField(tournament.groupName(match.p2), play.scoreSecond, { model.scores(play.scoreFirst, it) },
            number = true)
        val photo = play.reportPhoto
        val photoText = if (photo == null) "Captura del resultado: JPEG, PNG o WebP."
            else "Foto lista (${photo.width}×${photo.height})."
        Text(photoText,
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        MakeButton(if (photo == null) "Elegir foto del resultado" else "Cambiar foto", ButtonKind.Secondary,
            enabled = !state.busy) {
            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        detected?.let {
            Text("Marcador leído de la foto con ML Kit: $it. Revísalo antes de enviar.",
                style = MaterialTheme.typography.bodySmall, color = PactoCyan)
        }
        MakeButton("Enviar reporte", enabled = !state.busy && photo != null) { model.reportMatch() }
    }
}

@Composable
private fun RivalAnswer(state: ConnectedState, model: ConnectedViewModel) {
    MakeCard {
        Text("¿El resultado es correcto?", style = MaterialTheme.typography.titleMedium)
        MakeButton("Confirmar resultado", enabled = !state.busy) { model.confirmMatch() }
        MakeField("Motivo para disputar", state.play.disputeReason, { model.disputeReason(it) }, multiline = true,
            hint = "De 5 a 140 caracteres")
        MakeButton("Disputar", ButtonKind.Ghost, enabled = !state.busy) { model.disputeMatch() }
    }
}

@Composable
private fun OrganizerResolve(state: ConnectedState, model: ConnectedViewModel, tournament: TournamentDetail,
    match: PlayMatch) {
    val organizer = tournament.organizerId == state.account?.id
    if (!organizer || match.status !in listOf("pending", "reported", "disputed")) return
    MakeCard(accent = PactoCyan) {
        Text("Resolver como organizador", style = MaterialTheme.typography.titleMedium)
        Text("Úsalo si hay disputa, si el rival no respondió en 24 h o si un grupo no se presentó.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        listOfNotNull(match.p1, match.p2).forEach { group ->
            MakeButton("Gana ${tournament.groupName(group)}", ButtonKind.Secondary, enabled = !state.busy) {
                model.resolveMatch(group)
            }
        }
    }
}
