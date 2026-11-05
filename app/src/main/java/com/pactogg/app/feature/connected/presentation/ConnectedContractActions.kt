package com.pactogg.app.feature.connected.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.prototype.Action

@Composable
fun ConnectedContractActions(state: ConnectedState, model: ConnectedViewModel, contract: Challenge) {
    val accept = canAccept(contract, state.account?.id, state.group, state.contractStateStale)
    val reject = canReject(contract, state.account?.id, state.contractStateStale)
    if (state.social.pendingAction != null) {
        val accepting = state.social.pendingAction == "accept"
        ActionConfirmation(if (accepting) "¿Aceptar este reto?" else "¿Rechazar este reto?",
            if (accepting) "Se retendrán ${contract.bond} puntos virtuales de tu saldo."
            else "El reto quedará rechazado. No se descontarán puntos.",
            confirmLabel = if (accepting) "Aceptar reto" else "Rechazar reto",
            enabled = !state.busy, confirmEnabled = !state.busy && if (accepting) accept else reject,
            onConfirm = model::confirmContractAction, onDismiss = model::cancelContractAction)
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
