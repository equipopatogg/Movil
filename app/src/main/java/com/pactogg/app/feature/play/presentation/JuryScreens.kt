package com.pactogg.app.feature.play.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.EmptyState
import com.pactogg.app.core.designsystem.GameMark
import com.pactogg.app.core.designsystem.InfoRow
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.SafetyNote
import com.pactogg.app.core.designsystem.Segmented
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.core.designsystem.localDate
import com.pactogg.app.core.designsystem.statusLabel
import com.pactogg.app.core.designsystem.statusTone
import com.pactogg.app.feature.connected.presentation.ActionConfirmation
import com.pactogg.app.feature.connected.presentation.ConnectedState
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.EvidenceCard
import com.pactogg.app.feature.connected.presentation.back
import com.pactogg.app.feature.play.domain.VoteResult
import com.pactogg.app.feature.play.domain.VoteSummary
import com.pactogg.app.ui.theme.PactoCyan

/** Lista de retos que me toca votar (maqueta `jurado`, spec PL-01). */
@Composable
internal fun JuryScreen(state: ConnectedState, model: ConnectedViewModel) {
    MakePage("Jurado", "Retos de otros · tu tarea es revisar, no participar", onBack = { model.back() }) {
        val votes = state.play.pendingVotes
        if (state.play.votesLoaded && votes.isEmpty()) {
            EmptyState("Nada por revisar.", "Cuando te elijan como jurado de un reto, aparecerá aquí.",
                R.drawable.ic_pacto_vote)
        }
        votes.forEach { pending ->
            MakeCard(onClick = { if (!state.busy) model.openVote(pending.contract) }) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Pill("Jurado", Tone.Cyan)
                    pending.votingEndsAt?.let { Pill("Cierra ${localDate(it)}", Tone.Muted) }
                }
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GameMark(pending.contract.game)
                    Column(Modifier.weight(1f)) {
                        Kicker(pending.contract.game)
                        Text(pending.contract.condition, style = MaterialTheme.typography.titleMedium)
                    }
                }
                Text("Revisar el pacto", color = PactoCyan, style = MaterialTheme.typography.labelLarge)
            }
        }
        MakeButton("Actualizar", ButtonKind.Ghost, enabled = !state.busy) { model.loadPendingVotes() }
        SafetyNote("Votas Legítimo o Hizo trampa según la evidencia. Tu voto es secreto hasta que cierre la votación.")
    }
}

/** Revisión de un reto: evidencia, avance de la votación, voto y resultado (spec PL-01). */
@Composable
internal fun VoteScreen(state: ConnectedState, model: ConnectedViewModel) {
    val play = state.play
    val contract = play.voteContract
    MakePage("Revisar pacto", "¿La evidencia respalda el reto?", onBack = { model.back() }) {
        if (contract == null) {
            EmptyState("No hay reto abierto.", "Elige uno desde Jurado.", R.drawable.ic_pacto_vote)
            return@MakePage
        }
        MakeCard {
            Pill(statusLabel(contract.status), statusTone(contract.status))
            Kicker(contract.game)
            Text(contract.condition, style = MaterialTheme.typography.titleLarge)
            InfoRow("Recompensa", contract.reward)
            InfoRow("Fianza", "${contract.bond} pts virtuales")
        }
        Kicker("Evidencia recibida")
        if (play.voteEvidence.isEmpty()) Text("No pudimos mostrar la evidencia de este reto.",
            style = MaterialTheme.typography.bodySmall)
        play.voteEvidence.forEach { EvidenceCard(it) }
        play.voteSummary?.let { VoteProgress(it) }
        play.voteResult?.let { ResultCard(it) }
        val summary = play.voteSummary
        val canVote = summary != null && summary.iAmJuror && !summary.iVoted && contract.status == "voting"
        if (canVote) VoteForm(state, model)
        if (summary?.adminCanResolve == true && play.voteResult == null) AdminResolve(state, model)
    }
    if (play.confirmVote) ActionConfirmation("¿Confirmar tu voto?",
        if (play.voteChoice == "legit") "Votarás que el reto se cumplió de forma legítima." else
            "Votarás que hizo trampa. Esto afecta su índice de palabra.",
        "Confirmar voto", !state.busy, onConfirm = { model.castVote() }, onDismiss = { model.requestVote(false) })
}

@Composable
private fun VoteProgress(summary: VoteSummary) {
    MakeCard {
        Kicker("Avance de la votación")
        InfoRow("Votos", "${summary.votesCast} de ${summary.jurorsAssigned}")
        summary.votingEndsAt?.let { InfoRow("Cierra", localDate(it)) }
        if (summary.votingExtended) Text("Se extendió 24 h porque faltaban votos.",
            style = MaterialTheme.typography.bodySmall)
        if (summary.iVoted) Pill("Ya votaste", Tone.Violet)
    }
}

@Composable
private fun VoteForm(state: ConnectedState, model: ConnectedViewModel) {
    val choice = when (state.play.voteChoice) { "legit" -> "Legítimo"; "cheat" -> "Hizo trampa"; else -> "" }
    MakeCard {
        Kicker("Tu voto")
        Segmented(listOf("Legítimo", "Hizo trampa"),
            choice) { model.chooseVote(if (it == "Legítimo") "legit" else "cheat") }
        MakeField("Comentario (opcional)", state.play.voteComment, model::voteComment, hint = "Hasta 140 caracteres.")
        MakeButton("Confirmar mi voto",
            enabled = !state.busy && state.play.voteChoice != null) { model.requestVote(true) }
    }
}

@Composable
private fun AdminResolve(state: ConnectedState, model: ConnectedViewModel) {
    MakeCard {
        Kicker("Decisión del administrador")
        Text("El jurado no llegó a una decisión. Como administrador neutral, decide tú.",
            style = MaterialTheme.typography.bodySmall)
        MakeButton("Se cumplió", enabled = !state.busy) { model.resolveVoting("fulfilled") }
        MakeButton("Hizo trampa", ButtonKind.Ghost, enabled = !state.busy) { model.resolveVoting("cheating") }
    }
}

@Composable
private fun ResultCard(result: VoteResult) {
    MakeCard {
        Kicker("Resultado")
        Pill(statusLabel(result.outcome), statusTone(result.outcome))
        InfoRow("Legítimo", result.legitVotes.toString())
        InfoRow("Hizo trampa", result.cheatVotes.toString())
        InfoRow("Fianza", "${result.bond} pts")
        result.votes.mapNotNull { it.second }.forEach { Text("“$it”", style = MaterialTheme.typography.bodySmall) }
    }
}
