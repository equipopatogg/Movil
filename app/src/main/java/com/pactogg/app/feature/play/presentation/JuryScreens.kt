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
import com.pactogg.app.feature.connected.domain.Challenge
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
        VoteActions(state, model, contract)
    }
    if (play.confirmVote) ActionConfirmation("¿Confirmar tu voto?",
        if (play.voteChoice == "legit") "Votarás que el reto se cumplió de forma legítima." else
            "Votarás que hizo trampa. Esto afecta su índice de palabra.",
        "Confirmar voto", !state.busy, onConfirm = { model.castVote() }, onDismiss = { model.requestVote(false) })
}

/** Votar, o el motivo por el que no se puede, y la decisión del administrador (PL-01 RF6, RF7). */
@Composable
private fun VoteActions(state: ConnectedState, model: ConnectedViewModel, contract: Challenge) {
    val summary = state.play.voteSummary
    val canVote = summary != null && summary.iAmJuror && !summary.iVoted && contract.status == "voting"
    if (canVote) VoteForm(state, model)
    else voteBlockedReason(contract, summary, state.account?.id)?.let { SafetyNote(it) }
    if (summary?.adminCanResolve == true && state.play.voteResult == null) AdminResolve(state, model)
    MakeButton("Actualizar", ButtonKind.Ghost, enabled = !state.busy) { model.openVote(contract) }
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
        when (result.outcome) {
            "fulfilled" -> Text("¡Reto cumplido! 🏆", style = MaterialTheme.typography.headlineSmall, color = PactoCyan)
            "cheating" -> Text("El jurado decidió: hizo trampa.", style = MaterialTheme.typography.titleMedium)
            else -> Unit
        }
        Pill(statusLabel(result.outcome), statusTone(result.outcome))
        if (result.reward.isNotBlank()) InfoRow("Recompensa", result.reward)
        InfoRow("Legítimo", result.legitVotes.toString())
        InfoRow("Hizo trampa", result.cheatVotes.toString())
        InfoRow("Fianza", "${result.bond} pts")
        result.votes.mapNotNull { it.second }.forEach { Text("“$it”", style = MaterialTheme.typography.bodySmall) }
    }
}

/** Por qué no aparece el formulario de voto, en vez de dejar la pantalla sin botón (PL-01 RF7). */
internal fun voteBlockedReason(contract: Challenge, summary: VoteSummary?, accountId: String?): String? {
    val party = accountId != null && accountId in setOf(contract.challengerId, contract.acceptedBy,
        contract.executorId, contract.targetId.takeIf { contract.targetType == "member" })
    return when {
        summary == null -> "No pudimos consultar la votación. Pulsa Actualizar."
        contract.status == "in_progress" -> "La votación empieza cuando llegue la primera evidencia."
        contract.status != "voting" -> null
        summary.iVoted -> null
        party -> "Eres parte de este reto, así que no votas: lo decide el jurado (hasta 3 miembros del grupo " +
            "que no participan). Te avisaremos del resultado."
        summary.jurorsAssigned == 0 -> "No hay jurado disponible en el grupo; lo resolverá un administrador."
        else -> "No te tocó ser jurado de este reto: el servidor eligió a otros miembros al azar."
    }
}
