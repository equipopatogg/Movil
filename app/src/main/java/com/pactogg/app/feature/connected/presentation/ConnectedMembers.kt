package com.pactogg.app.feature.connected.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.Field
import com.pactogg.app.prototype.PactCard

@Composable
fun ConnectedMembers(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Tu equipo.", state.group?.name ?: "Selecciona un grupo") {
        if (state.group != null) {
            Field("Prefijo del apodo", state.social.memberQuery, model::memberSearch)
            Action("Buscar miembros", enabled = !state.busy) { model.loadMembers() }
            if (state.social.members.loaded && state.social.members.items.isEmpty()) {
                Text("No hay miembros coincidentes.")
            }
            state.social.members.items.forEach { member ->
                PactCard(member.nickname, "${member.role} · ${member.points} puntos virtuales") {
                    if (member.id != state.account?.id) Action("Retar a ${member.nickname}", enabled = !state.busy) {
                        model.targetMember(member.id)
                        model.navigate(ConnectedRoute.CREATE)
                    }
                }
            }
            if (state.social.members.hasMore) Action("Más miembros", enabled = !state.busy) { model.loadMembers(true) }
        }
        Action("Volver a grupos", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
    }
}
