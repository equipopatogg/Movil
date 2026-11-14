package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import com.pactogg.app.core.designsystem.localDate
import com.pactogg.app.core.designsystem.statusLabel
import com.pactogg.app.core.designsystem.statusTone
import com.pactogg.app.feature.connected.domain.Tournament
import com.pactogg.app.ui.theme.PactoCyan

private val TournamentLabels = listOf("Todos", "Abiertos", "En curso", "Finalizados", "Cancelados")
private val TournamentStatuses = listOf(null, "open", "in_progress", "finished", "cancelled")

/** Torneos con el estilo de la maqueta (`Tournaments`): pestañas por estado y tarjetas. */
@Composable
fun ConnectedTournaments(state: ConnectedState, model: ConnectedViewModel) {
    val selected = state.selectedTournament
    if (selected != null) {
        MakePage("Torneo", "${selected.game} · eliminación directa", onBack = { model.tournament(null) }) {
            TournamentDetails(selected)
        }
        return
    }
    MakePage("Torneos", "Compite con tu squad") {
        Segmented(TournamentLabels, TournamentLabels[TournamentStatuses.indexOf(state.status).coerceAtLeast(0)]) {
            model.filter(TournamentStatuses[TournamentLabels.indexOf(it)], state.gameFilter)
        }
        MakeField("Filtrar por juego", state.gameFilter, { model.filter(state.status, it) })
        MakeButton("Consultar torneos", ButtonKind.Ghost, enabled = !state.busy) { model.loadTournaments() }
        if (state.tournamentsLoaded && state.tournaments.isEmpty()) {
            EmptyState("No hay torneos para estos filtros.", "Cuando tu grupo se inscriba a uno aparecerá aquí.",
                R.drawable.ic_pacto_trophy)
        }
        state.tournaments.forEach { tournament -> TournamentCard(tournament) { model.tournament(tournament) } }
        if (state.hasMore) MakeButton("Cargar más", ButtonKind.Ghost, enabled = !state.busy) {
            model.loadTournaments(more = true)
        }
    }
}

@Composable
private fun TournamentCard(tournament: Tournament, onOpen: () -> Unit) {
    MakeCard(onClick = onOpen) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Pill(statusLabel(tournament.status), statusTone(tournament.status))
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GameMark(tournament.game, Tone.Orange)
            Column(Modifier.weight(1f)) {
                Kicker("Eliminación directa")
                Text(tournament.game, style = MaterialTheme.typography.titleMedium)
                Text(localDate(tournament.scheduledAt), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Text("Ver cuadro del torneo", color = PactoCyan, style = MaterialTheme.typography.labelLarge)
    }
}

/** Cuadro por rondas (`bracket` de la maqueta). Los textos los usa TournamentBracketTest. */
@Composable
internal fun TournamentDetails(tournament: Tournament) {
    MakeCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GameMark(tournament.game, Tone.Orange)
            Column(Modifier.weight(1f)) {
                Text(tournament.game, style = MaterialTheme.typography.titleLarge)
                Text("${tournament.format} · ${statusLabel(tournament.status)}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    if (tournament.matches.isEmpty()) {
        EmptyState("Aún no hay cuadro.", "El servidor todavía no informa partidas para este torneo.",
            R.drawable.ic_pacto_trophy)
    }
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        tournament.matches.groupBy { it.round }.toSortedMap().forEach { (round, matches) ->
            Column(Modifier.width(210.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Ronda $round", color = PactoCyan, style = MaterialTheme.typography.labelLarge)
                matches.sortedBy { it.position }.forEach { match ->
                    MakeCard {
                        Text("Partida ${match.position}", style = MaterialTheme.typography.titleSmall)
                        Pill(statusLabel(match.status), statusTone(match.status))
                        Text("${match.first ?: "Por definir"}: ${match.firstScore ?: "—"}")
                        Text("${match.second ?: "Por definir"}: ${match.secondScore ?: "—"}")
                    }
                }
            }
        }
    }
}
