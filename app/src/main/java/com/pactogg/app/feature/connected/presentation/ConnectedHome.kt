package com.pactogg.app.feature.connected.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.feature.play.presentation.openStandings
import com.pactogg.app.core.designsystem.HeroTitle
import com.pactogg.app.core.designsystem.Initial
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.LinkCard
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.PageLayout
import com.pactogg.app.core.designsystem.SectionTitle
import com.pactogg.app.core.designsystem.statusLabel
import com.pactogg.app.feature.play.presentation.openMatch
import com.pactogg.app.ui.theme.PactoBackground
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoOrange
import com.pactogg.app.ui.theme.PactoRaised

/** Encabezado de la maqueta (`AppHeader`): marca PactoGG e inicial del perfil. */
@Composable
internal fun AppHeader(state: ConnectedState, model: ConnectedViewModel) {
    Row(Modifier.fillMaxWidth().background(PactoBackground).padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Row(Modifier.weight(1f).clickable(enabled = !state.busy, role = Role.Button) {
            model.navigate(ConnectedRoute.HOME)
        }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            Box(Modifier.size(30.dp).background(PactoOrange, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center) {
                Text("P", color = PactoBackground, style = MaterialTheme.typography.titleSmall)
            }
            Column {
                Text("PACTOGG", style = MaterialTheme.typography.titleMedium)
                Kicker("Tu palabra está en juego")
            }
        }
        Box(Modifier.padding(end = 10.dp).size(38.dp).clickable(enabled = !state.busy, role = Role.Button) {
            model.navigate(ConnectedRoute.NOTIFICATIONS)
        }, contentAlignment = Alignment.Center) {
            Icon(painterResource(R.drawable.ic_pacto_bell), "Notificaciones", tint = PactoCyan,
                modifier = Modifier.size(22.dp))
            val unread = state.play.unread
            if (unread > 0) Box(Modifier.align(Alignment.TopEnd).size(17.dp).background(PactoOrange,
                RoundedCornerShape(9.dp)),
                contentAlignment = Alignment.Center) {
                Text(if (unread > 9) "9+" else "$unread", color = PactoBackground,
                    style = MaterialTheme.typography.labelSmall)
            }
        }
        Box(Modifier.clickable(enabled = !state.busy, role = Role.Button) { model.navigate(ConnectedRoute.PROFILE) }) {
            Initial(state.account?.nickname.orEmpty(), size = 38)
        }
    }
}

/** Inicio según la maqueta (`Home`). Solo muestra datos reales o accesos a pantallas conectadas. */
@Composable
internal fun ConnectedHome(state: ConnectedState, model: ConnectedViewModel) {
    var guide by rememberSaveable { mutableStateOf(false) }
    val go = { route: ConnectedRoute -> if (!state.busy) model.navigate(route) }
    PageLayout {
        LinkCard(state.group?.name ?: "Elige tu squad", if (state.group != null) "TU ESPACIO · TOCA PARA CAMBIAR"
            else "Abre o crea un grupo para jugar", R.drawable.ic_pacto_users) { go(ConnectedRoute.GROUPS) }
        HeroTitle("Bienvenido a tu tablero", "Juega con ganas.\nCumple tu palabra.",
            "Todo lo que acuerdas con tu squad, en un mismo lugar.")
        HomeShortcuts(go) { tab -> if (!state.busy) model.openStandings(tab) }
        SectionTitle("Cada pacto, un siguiente paso", "Lo que sigue")
        state.contract?.let { contract ->
            MakeCard(onClick = { go(ConnectedRoute.CONTRACT) }) {
                Kicker(contract.game)
                Text(contract.condition, style = MaterialTheme.typography.titleMedium)
                Text("Ver contrato y evidencia", color = PactoCyan, style = MaterialTheme.typography.labelLarge)
            }
        }
        if (state.group != null) {
            LinkCard("Tablero de retos", "Los pactos de ${state.group.name}", R.drawable.ic_pacto_bolt) {
                go(ConnectedRoute.BOARD)
            }
            LinkCard("Fama y Los Tramposos", "Los mejores retos y los que el jurado declaró trampa",
                R.drawable.ic_pacto_shield) { if (!state.busy) model.openStandings("fama") }
        } else {
            MakeButton("Elegir grupo", enabled = !state.busy) { go(ConnectedRoute.GROUPS) }
        }
        state.play.nextMatch?.let { next ->
            MakeCard(accent = PactoCyan, onClick = { if (!state.busy) model.openMatch(next.match.id) }) {
                Kicker("Tu siguiente partida", PactoCyan)
                Text(next.game.ifBlank { "Torneo" }, style = MaterialTheme.typography.titleMedium)
                Text("Ronda ${next.match.round} · ${statusLabel(next.match.status)}",
                    style = MaterialTheme.typography.bodySmall)
                Text("Ver partida", color = PactoCyan, style = MaterialTheme.typography.labelLarge)
            }
        }
        val pending = state.play.pendingVotes.size
        LinkCard("Jurado", if (pending > 0) "Te toca revisar $pending reto(s)" else "Retos de otros para revisar",
            R.drawable.ic_pacto_vote, badge = pending.takeIf { it > 0 }?.toString()) { go(ConnectedRoute.JURY) }
        TournamentLink { go(ConnectedRoute.TOURNAMENTS) }
        HowItWorks(guide) { guide = !guide }
    }
}

/** Atajos del inicio en 2 x 2: crear, crear por voz, ranking y torneos. */
@Composable
private fun HomeShortcuts(go: (ConnectedRoute) -> Unit, standings: (String) -> Unit) {
    val items = listOf(
        Shortcut(R.drawable.ic_pacto_plus, "Nuevo pacto", "Crea un reto") { go(ConnectedRoute.CREATE) },
        Shortcut(R.drawable.ic_pacto_mic, "Por voz", "Díctalo y listo") { go(ConnectedRoute.VOICE) },
        Shortcut(R.drawable.ic_pacto_trophy, "Ranking", "Rey del grupo") { standings("ranking") },
        Shortcut(R.drawable.ic_pacto_users, "Torneos", "Compite") { go(ConnectedRoute.TOURNAMENTS) },
    )
    items.chunked(2).forEach { row ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            row.forEach { item ->
                MakeCard(Modifier.weight(1f), onClick = item.onClick) {
                    Box(Modifier.size(34.dp).background(PactoRaised, RoundedCornerShape(9.dp)),
                        contentAlignment = Alignment.Center) {
                        Icon(painterResource(item.icon), null, tint = PactoCyan, modifier = Modifier.size(19.dp))
                    }
                    Text(item.title, style = MaterialTheme.typography.titleSmall)
                    Text(item.copy, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

private class Shortcut(@DrawableRes val icon: Int, val title: String, val copy: String, val onClick: () -> Unit)

@Composable
private fun TournamentLink(onClick: () -> Unit) {
    MakeCard(accent = PactoOrange, onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(Modifier.size(48.dp).background(PactoOrange, RoundedCornerShape(12.dp))
                .border(2.dp, PactoBackground, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.ic_pacto_trophy), null, tint = PactoBackground,
                    modifier = Modifier.size(26.dp))
            }
            Column(Modifier.weight(1f)) {
                Kicker("El reto se juega en equipo")
                Text("Hay lugar para tu squad.", style = MaterialTheme.typography.titleMedium)
                Text("Explora los torneos amistosos", color = PactoCyan, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun HowItWorks(open: Boolean, toggle: () -> Unit) {
    MakeCard(onClick = toggle) {
        Text("¿Cómo funciona un pacto?", style = MaterialTheme.typography.titleSmall)
        if (open) {
            listOf("Acuerden las reglas." to "Juego, condición, plazo y puntos virtuales.",
                "Juega y demuestra." to "Comparte una captura o un enlace.",
                "Deja que el jurado revise." to "Tus amigos deciden si se cumplió.").forEachIndexed { i, (t, c) ->
                Text("${i + 1}. $t", style = MaterialTheme.typography.labelLarge)
                Text(c, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Text("Competir entre amigos. Cumplir por convicción.", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

