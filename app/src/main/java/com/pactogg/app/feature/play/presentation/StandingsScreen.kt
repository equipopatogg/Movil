package com.pactogg.app.feature.play.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.EmptyState
import com.pactogg.app.core.designsystem.Initial
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.SafetyNote
import com.pactogg.app.core.designsystem.Segmented
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.core.designsystem.statusLabel
import com.pactogg.app.feature.connected.presentation.ConnectedRoute
import com.pactogg.app.feature.connected.presentation.ConnectedState
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.back
import com.pactogg.app.feature.connected.presentation.chooseGroupThen
import com.pactogg.app.feature.play.domain.Commitment
import com.pactogg.app.feature.play.domain.RankingEntry
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoOrange

private val Tabs = listOf("Ranking" to "ranking", "Libro" to "libro", "Fama" to "fama", "Tramposos" to "ban")

/** Palabra y ranking (maqueta `compromisos`, spec PL-03): Ranking, Libro, Muro de la Fama y Los Tramposos. */
@Composable
internal fun StandingsScreen(state: ConnectedState, model: ConnectedViewModel) {
    var info by rememberSaveable { mutableStateOf(false) }
    val play = state.play
    MakePage("Palabra y ranking", "Solo entre amigos del mismo grupo", onBack = { model.back() }) {
        val group = state.group
        if (group == null) {
            EmptyState("Elige un grupo.", "El ranking y los compromisos son de cada grupo.", R.drawable.ic_pacto_users)
            MakeButton("Ir a grupos", enabled = !state.busy) { model.chooseGroupThen(ConnectedRoute.STANDINGS) }
            return@MakePage
        }
        Kicker("Grupo privado · ${group.name}", PactoCyan)
        val current = Tabs.firstOrNull { it.second == play.standingsTab }?.first ?: "Ranking"
        Segmented(Tabs.map { it.first }, current) { label ->
            model.standingsTab(Tabs.first { it.first == label }.second)
        }
        when (play.standingsTab) {
            "libro" -> CommitmentsTab(state, model)
            "fama" -> FameTab(state)
            "ban" -> ShameTab(state)
            else -> RankingTab(state)
        }
        MakeCard(onClick = { info = !info }) {
            Text("¿Cómo se calcula?", style = MaterialTheme.typography.titleSmall)
            if (info) {
                Text("Ranking: puntos virtuales del grupo; el primero es el Rey del Grupo.",
                    style = MaterialTheme.typography.bodySmall)
                Text("Índice de palabra: sube al cumplir retos y compromisos a tiempo; baja con trampas y vencidos.",
                    style = MaterialTheme.typography.bodySmall)
                Text("Los Tramposos: solo retos que el jurado resolvió como trampa.",
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun RankingTab(state: ConnectedState) {
    val ranking = state.play.ranking
    if (ranking == null || ranking.items.isEmpty()) {
        EmptyState("El grupo necesita contratos resueltos.", "Cuando haya puntos en juego aparecerá el podio.",
            R.drawable.ic_pacto_trophy)
        return
    }
    ranking.king?.let { king ->
        MakeCard(accent = PactoOrange) {
            Kicker("Rey del grupo", PactoOrange)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Initial(king.nickname, Tone.Orange, 44)
                Text(king.nickname, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                Pill("${king.points} pts", Tone.Orange)
            }
        }
    }
    Podium(ranking.items.take(3))
    ranking.items.drop(3).forEach { entry ->
        MakeCard {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("${entry.position}", style = MaterialTheme.typography.titleLarge)
                Initial(entry.nickname, if (entry.userId == state.account?.id) Tone.Cyan else Tone.Violet)
                Text(entry.nickname, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                Text("${entry.points} pts", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun Podium(top: List<RankingEntry>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        top.forEachIndexed { index, entry ->
            val tone = listOf(Tone.Orange, Tone.Cyan, Tone.Violet)[index]
            MakeCard(Modifier.weight(1f)) {
                Kicker("#${entry.position}")
                Initial(entry.nickname, tone, 36)
                Text(entry.nickname, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                Text("${entry.points} pts", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun CommitmentsTab(state: ConnectedState, model: ConnectedViewModel) {
    val play = state.play
    if (play.commitmentsLoaded && play.iOwe.isEmpty() && play.owedToMe.isEmpty()) {
        EmptyState("Tu palabra está al día.",
            "Los compromisos aparecen cuando un reto legítimo genera un acuerdo por cumplir.",
            R.drawable.ic_pacto_vote)
        return
    }
    if (play.iOwe.isNotEmpty()) Kicker("Debo (${play.iOwe.size})")
    play.iOwe.forEach { CommitmentCard(it, "A ${it.creditor}", state.busy,
        "Ya cumplí") { model.completeCommitment(it, false) } }
    if (play.owedToMe.isNotEmpty()) Kicker("Me deben (${play.owedToMe.size})")
    play.owedToMe.forEach { CommitmentCard(it, "${it.debtor} te debe", state.busy, "Marcar cumplido") {
        model.completeCommitment(it, true)
    } }
}

@Composable
private fun CommitmentCard(item: Commitment, who: String, busy: Boolean, action: String, onAction: () -> Unit) {
    MakeCard {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Pill(statusLabel(item.status), if (item.status == "completed") Tone.Violet else Tone.Orange)
            if (item.game.isNotBlank()) Pill(item.game, Tone.Muted)
        }
        Text(item.description, style = MaterialTheme.typography.titleSmall)
        Text(who, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (item.status != "completed") MakeButton(action, ButtonKind.Secondary, enabled = !busy, onClick = onAction)
    }
}

@Composable
private fun FameTab(state: ConnectedState) {
    val fame = state.play.fame.orEmpty()
    if (fame.isEmpty()) {
        EmptyState("El muro espera buenas historias.", "Solo se muestran logros respaldados por contratos cerrados.",
            R.drawable.ic_pacto_trophy)
    }
    fame.forEach { entry ->
        MakeCard(accent = PactoCyan) {
            Kicker(entry.game, PactoCyan)
            Text(entry.condition, style = MaterialTheme.typography.titleSmall)
            entry.executor?.let { Text("Lo cumplió $it", style = MaterialTheme.typography.bodySmall) }
            if (entry.reward.isNotBlank()) Text("Recompensa: ${entry.reward}",
                style = MaterialTheme.typography.bodySmall)
            entry.photos.firstOrNull()?.let { url ->
                AsyncImage(url, "Evidencia del logro", Modifier.fillMaxWidth().height(160.dp),
                    contentScale = ContentScale.Crop)
            }
            entry.links.forEach { link -> Text(link, color = PactoCyan, style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Composable
private fun ShameTab(state: ConnectedState) {
    val shame = state.play.shame.orEmpty()
    Kicker("Los Tramposos · retos que el jurado declaró trampa")
    if (shame.isEmpty()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Pill("Sin casos confirmados", Tone.Orange)
            EmptyState("No señalamos a nadie sin consenso.",
                "Una evidencia rechazada no convierte a nadie en tramposo.",
                R.drawable.ic_pacto_shield)
        }
    }
    shame.forEach { entry ->
        MakeCard {
            Pill(entry.label.ifBlank { "Hizo trampa" }, Tone.Orange)
            Text(entry.condition, style = MaterialTheme.typography.titleSmall)
            Text("${entry.executor ?: "Alguien"} · ${entry.game}", style = MaterialTheme.typography.bodySmall)
        }
    }
    SafetyNote("Solo aparecen retos que el jurado resolvió como trampa.")
}
