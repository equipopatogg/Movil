package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
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
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.Segmented
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.core.designsystem.statusLabel
import com.pactogg.app.core.designsystem.statusTone
import com.pactogg.app.feature.connected.data.ContractStatuses
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.ui.theme.PactoCyan

/** Tablero de retos del grupo con las tarjetas de reto de la maqueta (`ChallengeCard`). */
@Composable
fun ConnectedBoard(state: ConnectedState, model: ConnectedViewModel) {
    MakePage("Retos", state.group?.name ?: "Selecciona un grupo", onBack = { model.back() }) {
        if (state.group == null) {
            MakeButton("Elegir grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
        } else {
            val filter = state.social.boardFilter
            Segmented(listOf("Del grupo", "Mis retos"), if (filter.mine) "Mis retos" else "Del grupo") {
                model.boardFilter(filter.copy(mine = it == "Mis retos"))
            }
            val labels = listOf("Todos") + ContractStatuses.map(::statusLabel)
            val selected = filter.status?.let(::statusLabel) ?: "Todos"
            Segmented(labels, selected) { label ->
                model.boardFilter(filter.copy(status = ContractStatuses.getOrNull(labels.indexOf(label) - 1)))
            }
            MakeField("Buscar reto", filter.query, { model.boardFilter(filter.copy(query = it.take(60))) })
            MakeField("Juego", filter.game, { model.boardFilter(filter.copy(game = it.take(60))) })
            MakeButton("Consultar retos", ButtonKind.Ghost, enabled = !state.busy) { model.loadBoard() }
            if (state.social.contracts.loaded && state.social.contracts.items.isEmpty()) {
                EmptyState("Todavía no hay pactos aquí.", "Crea el primero o cambia los filtros.",
                    R.drawable.ic_pacto_bolt)
            }
            state.social.contracts.items.forEach { contract ->
                ChallengeCard(contract, state.account?.id) { if (!state.busy) model.openChallenge(contract) }
            }
            if (state.social.contracts.hasMore) MakeButton("Más retos", ButtonKind.Ghost, enabled = !state.busy) {
                model.loadBoard(true)
            }
        }
    }
}

/** Tarjeta de reto de la maqueta: estado, juego, condición, fianza y "Ver reto". */
@Composable
internal fun ChallengeCard(contract: Challenge, accountId: String?, onOpen: () -> Unit) {
    MakeCard(onClick = onOpen) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Pill(statusLabel(contract.status), statusTone(contract.status))
            if (contract.challengerId == accountId) Pill("Tu reto", Tone.Violet)
            else if (contract.targetId == accountId) Pill("Para ti", Tone.Orange)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GameMark(contract.game)
            Column(Modifier.weight(1f)) {
                Kicker(contract.game)
                Text(contract.condition, style = MaterialTheme.typography.titleMedium)
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Fianza: ${contract.bond} pts", style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Ver reto", color = PactoCyan, style = MaterialTheme.typography.labelLarge)
        }
    }
}
