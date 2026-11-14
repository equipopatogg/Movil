package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.pactogg.app.core.designsystem.Segmented
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.core.designsystem.statusLabel
import com.pactogg.app.feature.connected.domain.Group
import com.pactogg.app.ui.theme.PactoCyan

private val GroupTabs = listOf("Mis grupos", "Unirme", "Crear grupo")

/**
 * Grupos según la maqueta (`Groups`). Flujo simple:
 * 1) Mis grupos se carga sola al entrar; tocar un grupo lo deja como "tu espacio".
 * 2) El grupo elegido muestra solo lo que se hace con él: ver retos, crear reto y ver miembros.
 * 3) Unirme y Crear grupo van en su propia pestaña.
 */
@Composable
fun ConnectedGroups(state: ConnectedState, model: ConnectedViewModel) {
    var tab by rememberSaveable { mutableStateOf(GroupTabs.first()) }
    MakePage("Grupos", "Un espacio privado. Tu gente. Tus pactos.") {
        Segmented(GroupTabs, tab) { tab = it }
        when (tab) {
            "Unirme" -> MakeCard {
                Kicker("Código de invitación", PactoCyan)
                Text("Pide a un amigo los seis caracteres del código de su grupo.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                MakeField("Código", state.social.joinCode, model::joinCode)
                MakeButton("Unirme al grupo", enabled = !state.busy) { model.joinGroup() }
            }
            "Crear grupo" -> MakeCard {
                Kicker("Nuevo grupo", PactoCyan)
                Text("Serás administrador y podrás invitar a tu squad con un código.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                MakeField("Nombre del grupo", state.groupName, model::groupName, hint = "De 2 a 40 caracteres.")
                MakeButton("Crear grupo", enabled = !state.busy) { model.createGroup() }
            }
            else -> {
                state.group?.let { SelectedGroup(it, state, model) }
                GroupList(state, model)
            }
        }
        SafetyNote("Los grupos son privados: solo sus miembros ven sus retos y torneos.")
    }
}

@Composable
private fun GroupList(state: ConnectedState, model: ConnectedViewModel) {
    val others = state.social.groups.items.filter { it.id != state.group?.id }
    if (others.isNotEmpty() || state.group == null) {
        Kicker(if (state.group == null) "Elige tu squad" else "Tus otros grupos")
    }
    if (state.social.groups.loaded && state.social.groups.items.isEmpty()) {
        EmptyState("Aún no tienes grupo.", "Únete con un código o crea el tuyo en las pestañas de arriba.",
            R.drawable.ic_pacto_users)
    }
    others.forEach { group ->
        MakeCard(onClick = { if (!state.busy) model.selectGroup(group.id) }) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GameMark(group.name, Tone.Violet)
                Column(Modifier.weight(1f)) {
                    Text(group.name, style = MaterialTheme.typography.titleMedium)
                    Text("${statusLabel(group.role)} · ${group.points} puntos",
                        color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                Pill("Privado", Tone.Cyan)
            }
        }
    }
    if (state.social.groups.hasMore) MakeButton("Más grupos", ButtonKind.Ghost, enabled = !state.busy) {
        model.loadGroups(true)
    }
}

@Composable
private fun SelectedGroup(group: Group, state: ConnectedState, model: ConnectedViewModel) {
    MakeCard(accent = PactoCyan) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GameMark(group.name, Tone.Violet)
            Column(Modifier.weight(1f)) {
                Kicker("Tu espacio", PactoCyan)
                Text(group.name, style = MaterialTheme.typography.titleLarge)
            }
            Pill(statusLabel(group.role), Tone.Orange)
        }
        InfoRow("Código para invitar", group.inviteCode)
        InfoRow("Tus puntos virtuales", group.points.toString())
        MakeButton("Ver retos del grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.BOARD) }
        MakeButton("Ver miembros", ButtonKind.Ghost, enabled = !state.busy) { model.navigate(ConnectedRoute.MEMBERS) }
    }
}
