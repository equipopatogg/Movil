package com.pactogg.app.feature.connected.presentation

import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.PactCard

@Composable
internal fun CommunityJury(state: ConnectedState, model: ConnectedViewModel) {
    if (state.community.loaded && state.community.pending.isEmpty()) Text(stringResource(
        R.string.community_no_tienes_votaciones_pendientes))
    state.community.pending.forEach { item ->
        PactCard(item.contract.game, item.contract.condition) {
            item.votingEndsAt?.let { Text("Vence: $it") }
            Action(stringResource(R.string.community_revisar_reto), enabled = !state.busy) {
                model.openJuryContract(item.contract.id) }
        }
    }
}

@Composable
internal fun CommunityVoting(state: ConnectedState, model: ConnectedViewModel) {
    CommunityVotingContent(state, model::communityComment, model::requestCommunityAction) {
        model.navigate(ConnectedRoute.EVIDENCE)
    }
}

@Composable
internal fun CommunityVotingContent(state: ConnectedState, onComment: (String) -> Unit,
    onAction: (CommunityAction) -> Unit, onEvidence: () -> Unit) {
    if (state.contract != null) Action(stringResource(R.string.community_revisar_evidencias), enabled = !state.busy) {
        onEvidence()
    }
    state.community.summary?.let { summary ->
        Text("Votos recibidos: ${summary.votesCast}/${summary.jurorsAssigned}")
        summary.votingEndsAt?.let { Text("Vence: $it") }
        if (summary.iVoted) Text(stringResource(R.string.community_ya_emitiste_tu_voto))
        if (state.community.loaded && canCastVote(summary, CommunityAction("vote", summary.contractId, "legit"))) {
            OutlinedTextField(value = state.community.comment, onValueChange = onComment,
                enabled = !state.busy, label = { Text(stringResource(
                    R.string.community_comentario_opcional_maximo_140)) })
            Action(stringResource(R.string.community_votar_legitimo), enabled = !state.busy) {
                onAction(CommunityAction("vote", summary.contractId, "legit"))
            }
            Action(stringResource(R.string.community_votar_trampa), enabled = !state.busy) {
                onAction(CommunityAction("vote", summary.contractId, "cheat"))
            }
        }
        if (state.community.loaded && canResolveVote(summary,
            CommunityAction("resolve", summary.contractId, "fulfilled"))) {
            Action(stringResource(R.string.community_resolver_como_cumplido), enabled = !state.busy) {
                onAction(CommunityAction("resolve", summary.contractId, "fulfilled"))
            }
            Action(stringResource(R.string.community_resolver_como_trampa), enabled = !state.busy) {
                onAction(CommunityAction("resolve", summary.contractId, "cheating"))
            }
        }
    }
    CommunityResult(state)
}

@Composable
private fun CommunityResult(state: ConnectedState) {
    state.community.result?.let { result ->
        PactCard("Resultado", if (result.outcome == "fulfilled") "Cumplido" else "Trampa") {
            Text("Legítimo: ${result.legitVotes} · Trampa: ${result.cheatVotes}")
            Text("Fianza: ${result.bondRequired} puntos virtuales")
            Text(when (result.bondMovement) {
                "bond_release", "bond_release_and_reward" -> "La fianza fue devuelta."
                "bond_forfeit" -> "La fianza fue transferida al retador."
                else -> "Sin movimiento de fianza."
            })
            result.votes.forEach { Text("Voto anónimo: ${if (it.vote == "legit") "Legítimo" else "Trampa"}") }
        }
    }
}
