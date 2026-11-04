package com.pactogg.app.feature.connected.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.R
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.Choices
import com.pactogg.app.prototype.Field
import com.pactogg.app.prototype.PactCard

@Composable
fun ConnectedCreate(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Crea tu\npróximo reto.", "Reto manual para el grupo") {
        val group = state.group
        if (group == null) {
            PactCard("Necesitas un grupo", "Crea un grupo para disponer de su configuración real.") {
                Action("Crear grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
            }
        } else {
            Text(group.name, color = androidx.compose.material3.MaterialTheme.colorScheme.secondary)
            val draft = state.draft
            Field("Juego", draft.game) { model.draft(draft.copy(game = it)) }
            Field("Condición del reto", draft.condition) { model.draft(draft.copy(condition = it)) }
            Field("Recompensa sin dinero", draft.reward) { model.draft(draft.copy(reward = it)) }
            Field("Fecha local: AAAA-MM-DD HH:mm", state.deadlineLocal, model::deadline)
            Text("Usamos la zona horaria del dispositivo y enviamos la fecha en UTC.")
            Text("Fianza en puntos virtuales, según el grupo")
            Choices(group.bonds.map(Int::toString), draft.bond?.toString().orEmpty()) {
                model.draft(draft.copy(bond = it.toInt()))
            }
            if (state.reviewing) {
                PactCard(draft.game, draft.condition, R.drawable.ic_pacto_bolt) {
                    Text("Recompensa: ${draft.reward}")
                    Text("Fecha UTC: ${draft.deadline}")
                    Text("Fianza: ${draft.bond} puntos virtuales")
                    Text("Destinatario: grupo")
                    Action("Confirmar y crear reto", enabled = !state.busy) { model.createChallenge() }
                }
            } else Action("Revisar reto", enabled = !state.busy) { model.review() }
        }
    }
}

@Composable
fun ConnectedContract(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Tu reto.\nTu palabra.", "Resultado de la creación") {
        state.contract?.let { contract ->
            PactCard(contract.game, contract.condition, R.drawable.ic_pacto_shield) {
                Text("Recompensa: ${contract.reward}")
                Text("Estado del servidor: ${contract.status}")
                Text("Fecha UTC: ${contract.deadline}")
                Text("Fianza: ${contract.bond} puntos virtuales")
                Action("Agregar evidencia", enabled = !state.busy) { model.navigate(ConnectedRoute.EVIDENCE) }
            }
        } ?: Text("No hay un reto confirmado en esta sesión.")
        Text("El servidor aún no ofrece consulta del historial ni aceptación o votación del reto.")
    }
}
