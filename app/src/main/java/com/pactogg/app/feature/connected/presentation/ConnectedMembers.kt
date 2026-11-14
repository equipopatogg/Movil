package com.pactogg.app.feature.connected.presentation

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
import com.pactogg.app.core.designsystem.Initial
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.core.designsystem.statusLabel

/** Miembros del grupo con el estilo de la maqueta; "Retar" lleva al formulario con el rival elegido. */
@Composable
fun ConnectedMembers(state: ConnectedState, model: ConnectedViewModel) {
    MakePage("Tu squad", state.group?.name ?: "Selecciona un grupo", onBack = { model.back() }) {
        if (state.group != null) {
            MakeField("Buscar por apodo", state.social.memberQuery, model::memberSearch)
            MakeButton("Buscar miembros", ButtonKind.Ghost, enabled = !state.busy) { model.loadMembers() }
            if (state.social.members.loaded && state.social.members.items.isEmpty()) {
                EmptyState("Nadie con ese apodo.", "Prueba con otras letras.", R.drawable.ic_pacto_users)
            }
            state.social.members.items.forEach { member ->
                MakeCard {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Initial(member.nickname, if (member.id == state.account?.id) Tone.Orange else Tone.Violet, 36)
                        Column(Modifier.weight(1f)) {
                            Text(member.nickname, style = MaterialTheme.typography.titleSmall)
                            Text("${member.points} puntos virtuales", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Pill(statusLabel(member.role), if (member.role == "admin") Tone.Orange else Tone.Muted)
                    }
                    if (member.id != state.account?.id) {
                        MakeButton("Retar a ${member.nickname}", ButtonKind.Secondary, enabled = !state.busy) {
                            model.targetMember(member.id)
                            model.navigate(ConnectedRoute.CREATE)
                        }
                    }
                }
            }
            if (state.social.members.hasMore) MakeButton("Más miembros", ButtonKind.Ghost, enabled = !state.busy) {
                model.loadMembers(true)
            }
        } else {
            MakeButton("Elegir grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
        }
    }
}
