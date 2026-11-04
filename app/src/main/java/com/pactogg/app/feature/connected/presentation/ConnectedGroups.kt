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
    AppPage("Tu grupo.\nTu equipo.", "Crea un grupo privado") {
        PactCard("Nuevo grupo", "Nombre de 2 a 40 caracteres.", R.drawable.ic_pacto_users) {
            Field("Nombre del grupo", state.groupName, model::groupName)
            Action("Crear grupo", enabled = !state.busy) { model.createGroup() }
        }
        state.group?.let { group ->
            PactCard(group.name, "Grupo confirmado por el servidor", R.drawable.ic_pacto_users) {
                Text("Código: ${group.inviteCode}")
                Text("Rol: ${group.role} · Puntos virtuales: ${group.points}")
            }
        }
        Text("Solo se muestra el último grupo creado aquí. El servicio todavía no permite recuperar tu lista.")
    }
}
