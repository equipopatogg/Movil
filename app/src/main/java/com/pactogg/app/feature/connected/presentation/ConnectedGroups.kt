package com.pactogg.app.feature.connected.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.R
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.Field
import com.pactogg.app.prototype.PactCard

@Composable
fun ConnectedGroups(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Tu grupo.\nTu equipo.", "Grupos privados") {
        PactCard("Mis grupos", "Selecciona el grupo con el que quieres jugar.", R.drawable.ic_pacto_users) {
            Field("Buscar grupo", state.social.groupQuery, model::groupSearch)
            Action("Consultar mis grupos", enabled = !state.busy) { model.loadGroups() }
            if (state.social.groups.loaded && state.social.groups.items.isEmpty()) {
                Text("No hay grupos para esta búsqueda.")
            }
            state.social.groups.items.forEach { group ->
                Text("${group.name} · ${group.role} · ${group.points} puntos")
                Action("Abrir ${group.name}", enabled = !state.busy) { model.selectGroup(group.id) }
            }
            if (state.social.groups.hasMore) Action("Más grupos", enabled = !state.busy) { model.loadGroups(true) }
        }
        state.group?.let { group ->
            PactCard(group.name, "Grupo seleccionado", R.drawable.ic_pacto_users) {
                Text("Código: ${group.inviteCode}")
                Text("Rol: ${group.role} · Puntos virtuales: ${group.points}")
                Action("Actualizar grupo", enabled = !state.busy) { model.selectGroup(group.id) }
                Action("Ver miembros", enabled = !state.busy) { model.navigate(ConnectedRoute.MEMBERS) }
                Action("Ver retos del grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.BOARD) }
                Action("Crear reto en este grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.CREATE) }
            }
        }
        PactCard("Únete a tu equipo", "Pide a un compañero el código de seis caracteres.") {
            Field("Código de invitación", state.social.joinCode, model::joinCode)
            Action("Unirme al grupo", enabled = !state.busy) { model.joinGroup() }
        }
        PactCard("Nuevo grupo", "Nombre de 2 a 40 caracteres.", R.drawable.ic_pacto_users) {
            Field("Nombre del grupo", state.groupName, model::groupName)
            Action("Crear grupo", enabled = !state.busy) { model.createGroup() }
        }
    }
}
