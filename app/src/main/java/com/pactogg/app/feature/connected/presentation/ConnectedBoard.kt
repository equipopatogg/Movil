package com.pactogg.app.feature.connected.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.feature.connected.data.ContractStatuses
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.Choices
import com.pactogg.app.prototype.Field
import com.pactogg.app.prototype.PactCard

@Composable
fun ConnectedBoard(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Retos de\ntu equipo.", state.group?.name ?: "Selecciona un grupo") {
        if (state.group == null) {
            Action("Elegir grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
        } else {
            val filter = state.social.boardFilter
            Field("Buscar reto", filter.query) { model.boardFilter(filter.copy(query = it.take(60))) }
            Field("Juego del reto", filter.game) { model.boardFilter(filter.copy(game = it.take(60))) }
            Choices(listOf("Todos") + ContractStatuses, filter.status ?: "Todos") {
                model.boardFilter(filter.copy(status = it.takeUnless { value -> value == "Todos" }))
            }
            Choices(listOf("Del grupo", "Mis retos"), if (filter.mine) "Mis retos" else "Del grupo") {
                model.boardFilter(filter.copy(mine = it == "Mis retos"))
            }
            Action("Consultar retos", enabled = !state.busy) { model.loadBoard() }
            if (state.social.contracts.loaded && state.social.contracts.items.isEmpty()) {
                Text("No hay retos para estos filtros.")
            }
            state.social.contracts.items.forEach { contract ->
                PactCard(contract.game, contract.condition) {
                    Text("Estado: ${contract.status} · Fianza: ${contract.bond} puntos")
                    Action("Abrir reto ${contract.id.takeLast(6)}", enabled = !state.busy) {
                        model.openChallenge(contract)
                    }
                }
            }
            if (state.social.contracts.hasMore) Action("Más retos", enabled = !state.busy) { model.loadBoard(true) }
        }
    }
}
