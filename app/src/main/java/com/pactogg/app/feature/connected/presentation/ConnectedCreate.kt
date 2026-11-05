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
            PactCard("Necesitas un grupo", "Selecciona o crea un grupo para usar su configuración real.") {
                Action("Crear grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
            }
        } else {
            Text(group.name, color = androidx.compose.material3.MaterialTheme.colorScheme.secondary)
            val draft = state.draft
            val rival = state.social.selectedRival
            Text(if (draft.targetId == null) "Destinatario: todo el grupo" else "Rival: ${rival?.nickname.orEmpty()}")
            Action("Elegir rival del grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.MEMBERS) }
            if (draft.targetId != null) Action("Retar a todo el grupo", enabled = !state.busy) {
                model.targetMember(null)
            }
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
                    Text(if (draft.targetId == null) "Destinatario: grupo"
                        else "Destinatario: ${rival?.nickname.orEmpty()}")
                    Action("Confirmar y crear reto", enabled = !state.busy) { model.createChallenge() }
                }
            } else Action("Revisar reto", enabled = !state.busy) { model.review() }
        }
    }
}

@Composable
fun ConnectedContract(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Tu reto.\nTu palabra.", "Detalle confirmado por el servidor") {
        state.contract?.let { contract ->
            PactCard(contract.game, contract.condition, R.drawable.ic_pacto_shield) {
                Text("Recompensa: ${contract.reward}")
                Text(if (state.contractStateStale) "Estado actual pendiente de consulta."
                    else "Estado del servidor: ${contract.status}")
                Text("Fecha UTC: ${contract.deadline}")
                Text("Fianza: ${contract.bond} puntos virtuales")
                contract.evidenceCount?.let { Text("Evidencias registradas: $it") }
                contract.closedReason?.let { Text("Motivo de cierre: $it") }
                ConnectedContractMetadata(contract)
                Action("Actualizar reto", enabled = !state.busy) { model.openChallenge(contract) }
                ConnectedContractActions(state, model, contract)
                val evidenceTarget = contract.targetType != "member" || contract.targetId == state.account?.id
                if (contract.status in listOf("open", "in_progress", "accepted", "evidence_pending", "voting") &&
                    (contract.evidenceCount ?: 0) < 3 && evidenceTarget) {
                    Action("Agregar evidencia", enabled = !state.busy) { model.navigate(ConnectedRoute.EVIDENCE) }
                }
            }
        } ?: Text("No hay un reto confirmado en esta sesión.")
        Text("Los puntos son virtuales. La votación y resolución del reto siguen pendientes en backend.")
    }
}
