package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.feature.connected.domain.Tournament
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.Choices
import com.pactogg.app.prototype.Field
import com.pactogg.app.prototype.PactCard
import com.pactogg.app.prototype.TournamentArtwork

@Composable
fun ConnectedTournaments(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Compite\ncon tu grupo.", "Torneos disponibles · solo consulta") {
        if (state.selectedTournament != null) {
            TournamentDetails(state.selectedTournament)
            Action("Volver a torneos", enabled = !state.busy) { model.tournament(null) }
        } else {
            val labels = listOf("Todos", "Abiertos", "En curso", "Finalizados", "Cancelados")
            val statuses = listOf(null, "open", "in_progress", "finished", "cancelled")
            Choices(labels, labels[statuses.indexOf(state.status).coerceAtLeast(0)]) {
                model.filter(statuses[labels.indexOf(it)], state.gameFilter)
            }
            Field("Filtrar por juego", state.gameFilter) { model.filter(state.status, it) }
            Action("Consultar torneos", enabled = !state.busy) { model.loadTournaments() }
            if (state.tournamentsLoaded && state.tournaments.isEmpty()) Text("No hay torneos para estos filtros.")
            state.tournaments.forEach { tournament ->
                PactCard(tournament.game, "${tournament.format} · ${tournament.status}", R.drawable.ic_pacto_trophy) {
                    TournamentArtwork(R.drawable.ic_pacto_trophy)
                    Text(tournament.scheduledAt)
                    Action("Ver cuadro del torneo", enabled = !state.busy) { model.tournament(tournament) }
                }
            }
            if (state.hasMore) Action("Cargar más", enabled = !state.busy) { model.loadTournaments(more = true) }
        }
    }
}

@Composable
internal fun TournamentDetails(tournament: Tournament) {
    Text(tournament.game, style = MaterialTheme.typography.titleLarge)
    Text("${tournament.format} · ${tournament.status}")
    TournamentArtwork(R.drawable.ic_pacto_trophy)
    if (tournament.matches.isEmpty()) Text("El servidor aún no informa partidas para este torneo.")
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        tournament.matches.groupBy { it.round }.toSortedMap().forEach { (round, matches) ->
            Column(Modifier.width(240.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Ronda $round", color = MaterialTheme.colorScheme.secondary)
                matches.sortedBy { it.position }.forEach { match ->
                    PactCard("Partida ${match.position}", match.status, R.drawable.ic_pacto_trophy) {
                        Text("${match.first ?: "Por definir"}: ${match.firstScore ?: "—"}")
                        Text("${match.second ?: "Por definir"}: ${match.secondScore ?: "—"}")
                    }
                }
            }
        }
    }
}
