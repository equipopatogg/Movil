package com.pactogg.app.feature.play.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.core.designsystem.localDate
import com.pactogg.app.core.designsystem.statusLabel
import com.pactogg.app.core.designsystem.statusTone
import com.pactogg.app.feature.connected.presentation.ActionConfirmation
import com.pactogg.app.feature.connected.presentation.ConnectedRoute
import com.pactogg.app.feature.connected.presentation.ConnectedState
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.back
import com.pactogg.app.feature.play.domain.PlayMatch
import com.pactogg.app.feature.play.domain.TournamentDetail
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoOrange

/** Nuevo torneo o inscribirse con código (maqueta `torneo/nuevo`, spec PL-04 RF1 y RF2). */
@Composable
internal fun TournamentNewScreen(state: ConnectedState, model: ConnectedViewModel) {
    val play = state.play
    MakePage("Nuevo torneo", "Compite con otros squads", onBack = { model.back() }) {
        val group = state.group
        if (group == null) {
            EmptyState("Elige tu grupo primero.", "El torneo se crea o se juega a nombre de un grupo.",
                R.drawable.ic_pacto_users)
            MakeButton("Ir a grupos", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
            return@MakePage
        }
        Kicker("Tu grupo · ${group.name}", PactoCyan)
        MakeCard {
            Text("Unirme con código", style = MaterialTheme.typography.titleMedium)
            Text("Pídele el código al organizador. Se cobra la inscripción en puntos de tu grupo.",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            MakeField("Código del torneo", play.joinCode, { model.tournamentCode(it) })
            MakeButton("Inscribir a mi grupo", ButtonKind.Secondary, enabled = !state.busy) { model.joinTournament() }
        }
        MakeCard(accent = PactoOrange) {
            Text("Organizar un torneo", style = MaterialTheme.typography.titleMedium)
            if (group.role != "admin") {
                Text("Solo el administrador del grupo puede organizar torneos.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                TournamentFormFields(play.form) { model.tournamentForm(it) }
                MakeButton("Crear torneo", enabled = !state.busy) { model.createTournament() }
            }
        }
        SafetyNote("El organizador no juega: solo arbitra. Los puntos son virtuales y solo valen dentro de PactoGG.")
    }
}

@Composable
private fun TournamentFormFields(form: TournamentForm, onChange: (TournamentForm) -> Unit) {
    MakeField("Juego", form.game, { onChange(form.copy(game = it.take(40))) })
    Kicker("Formato: eliminación directa")
    MakeField("Máximo de grupos (2 a 16)", form.maxGroups, { onChange(form.copy(maxGroups = it.filter(Char::isDigit)
        .take(2))) }, number = true)
    MakeField("Inscripción en puntos (0 a 500)", form.entryPoints, {
        onChange(form.copy(entryPoints = it.filter(Char::isDigit).take(3)))
    }, number = true)
    MakeField("Fecha y hora", form.date, { onChange(form.copy(date = it.take(16))) }, hint = "AAAA-MM-DD HH:mm")
}

/** Detalle para jugar: grupos, bolsa, iniciar/salir y cuadro (maqueta `torneo/bracket`, PL-04 RF3). */
@Composable
internal fun TournamentPlayScreen(state: ConnectedState, model: ConnectedViewModel) {
    val tournament = state.play.tournament
    var leaving by rememberSaveable { mutableStateOf(false) }
    MakePage("Torneo", tournament?.let { "${it.game} · eliminación directa" }, onBack = { model.back() }) {
        if (tournament == null) {
            EmptyState("No hay torneo abierto.", "Ábrelo desde la lista de torneos.", R.drawable.ic_pacto_trophy)
            return@MakePage
        }
        val organizer = tournament.organizerId == state.account?.id
        val mine = state.group?.id?.let { id -> tournament.groups.any { it.groupId == id } } == true
        TournamentHeader(tournament, organizer)
        TournamentGroups(tournament)
        TournamentActions(state, model, tournament, organizer, mine) { leaving = true }
        Bracket(tournament) { if (!state.busy) model.openMatch(it.id) }
        SafetyNote("Cada resultado lo confirma el grupo rival antes de avanzar de ronda.")
        MakeButton("Actualizar", ButtonKind.Ghost, enabled = !state.busy) { model.openTournament(tournament.id) }
    }
    if (leaving) ActionConfirmation("¿Salir del torneo?", "Tu grupo deja su lugar y se devuelve la inscripción.",
        "Salir", !state.busy, { leaving = false; model.leaveTournament() }, { leaving = false })
}

@Composable
private fun TournamentGroups(tournament: TournamentDetail) {
    Kicker("Grupos inscritos (${tournament.groups.size}/${tournament.maxGroups})")
    tournament.groups.forEach { group ->
        MakeCard {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(group.name.ifBlank { "Grupo" }, style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f))
                if (group.isOrganizer) Pill("Organiza", Tone.Orange)
                if (group.groupId == tournament.winner) Pill("Campeón", Tone.Violet)
            }
        }
    }
}

@Composable
private fun TournamentActions(state: ConnectedState, model: ConnectedViewModel, tournament: TournamentDetail,
    organizer: Boolean, mine: Boolean, onLeave: () -> Unit) {
    if (organizer && tournament.status == "open") {
        MakeButton("Iniciar torneo", enabled = !state.busy && tournament.groups.count { !it.isOrganizer } >= 2) {
            model.startTournament()
        }
        Text("Se necesitan al menos 2 grupos además del tuyo.", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    if (!organizer && mine && tournament.status == "open") {
        MakeButton("Salir del torneo", ButtonKind.Ghost, enabled = !state.busy, onClick = onLeave)
    }
}

@Composable
private fun TournamentHeader(tournament: TournamentDetail, organizer: Boolean) {
    MakeCard(accent = PactoOrange) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GameMark(tournament.game, Tone.Orange)
            Column(Modifier.weight(1f)) {
                Text(tournament.game, style = MaterialTheme.typography.titleLarge)
                Text(localDate(tournament.scheduledAt), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Pill(statusLabel(tournament.status), statusTone(tournament.status))
        }
        InfoRow("Inscripción", "${tournament.entryPoints} pts por grupo")
        InfoRow("Bolsa", "${tournament.prizePool} pts")
        if (organizer && tournament.code != null) InfoRow("Código para invitar", tournament.code)
        tournament.cancelReason?.let { InfoRow("Cancelado", it) }
    }
}

@Composable
private fun Bracket(tournament: TournamentDetail, onOpen: (PlayMatch) -> Unit) {
    if (tournament.matches.isEmpty()) {
        EmptyState("Aún no hay cuadro.", "Aparece cuando el organizador inicia el torneo.", R.drawable.ic_pacto_trophy)
        return
    }
    Kicker("Cuadro del torneo · toca una partida", PactoCyan)
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        tournament.matches.groupBy { it.round }.toSortedMap().forEach { (round, matches) ->
            Column(Modifier.width(210.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(roundName(round, tournament.rounds), color = PactoCyan,
                    style = MaterialTheme.typography.labelLarge)
                matches.sortedBy { it.position }.forEach { match ->
                    MakeCard(onClick = { onOpen(match) }) {
                        Text("Partida ${match.position}", style = MaterialTheme.typography.titleSmall)
                        Pill(statusLabel(match.status), statusTone(match.status))
                        Text("${tournament.groupName(match.p1)}: ${match.p1Score ?: "—"}")
                        Text("${tournament.groupName(match.p2)}: ${match.p2Score ?: "—"}")
                    }
                }
            }
        }
    }
}

private fun roundName(round: Int, rounds: Int): String = when (rounds - round) {
    0 -> "Final"
    1 -> "Semifinales"
    2 -> "Cuartos de final"
    else -> "Ronda $round"
}
