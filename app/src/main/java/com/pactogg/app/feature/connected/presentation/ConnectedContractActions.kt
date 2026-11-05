package com.pactogg.app.feature.connected.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.PactCard

@Composable
fun ConnectedContractActions(state: ConnectedState, model: ConnectedViewModel, contract: Challenge) {
    val accept = canAccept(contract, state.account?.id, state.group, state.contractStateStale)
    val reject = canReject(contract, state.account?.id, state.contractStateStale)
    if (state.social.pendingAction != null) {
        val accepting = state.social.pendingAction == "accept"
        PactCard(if (accepting) "¿Aceptas el reto?" else "¿Rechazas el reto?",
            if (accepting) "Se retendrán ${contract.bond} puntos virtuales de tu saldo."
            else "El reto dirigido a ti quedará cerrado como rechazado; no se descuentan puntos.") {
            Action(if (accepting) "Confirmar aceptación" else "Confirmar rechazo",
                enabled = !state.busy && if (accepting) accept else reject) { model.confirmContractAction() }
            Action("Volver sin confirmar", enabled = !state.busy) { model.cancelContractAction() }
        }
    } else {
        if (accept) Action("Aceptar reto", enabled = !state.busy) { model.requestContractAction("accept") }
        if (reject) Action("Rechazar reto", enabled = !state.busy) { model.requestContractAction("reject") }
        val insufficient = state.group?.points?.let { it < contract.bond } == true
        if (contract.status == "open" && contract.challengerId != state.account?.id && insufficient) {
            Text("No tienes puntos suficientes para esta fianza.")
        }
    }
}

@Composable
fun ConnectedContractMetadata(contract: Challenge) {
    contract.acceptedBy?.let { Text("Aceptado por: $it") }
    contract.acceptedAt?.let { Text("Aceptado: $it") }
    contract.updatedAt?.let { Text("Actualizado: $it") }
    contract.createdAt?.let { Text("Creado: $it") }
}
