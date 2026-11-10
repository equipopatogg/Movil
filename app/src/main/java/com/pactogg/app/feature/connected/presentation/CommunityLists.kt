package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.account.presentation.LocalExtras
import com.pactogg.app.feature.account.presentation.ExtrasPage

import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.PactCard

@Composable
internal fun CommunityPoints(state: ConnectedState) {
    state.community.points?.let { points ->
        Text("Saldo: ${points.balance} puntos virtuales, sin valor monetario")
        if (points.movements.isEmpty()) Text(stringResource(R.string.community_sin_movimientos))
        points.movements.forEach { item ->
            PactCard(item.createdAt, "${item.amount} puntos") {
                Text("Saldo posterior: ${item.balanceAfter}")
                Text(when (item.type) {
                    "bond_hold" -> "Retención de fianza"
                    "bond_release" -> "Devolución de fianza"
                    "bond_forfeit" -> "Transferencia de fianza"
                    else -> "Movimiento de puntos"
                })
            }
        }
    }
}

@Composable
internal fun CommunityRanking(state: ConnectedState) {
    if (state.community.loaded && state.community.ranking.isEmpty()) Text(stringResource(
        R.string.community_sin_posiciones_disponibles))
    state.community.ranking.forEach {
        PactCard("${it.position}. ${it.nickname}", "${it.pointsBalance} puntos virtuales") { }
    }
}

@Composable
internal fun CommunityCommitments(state: ConnectedState, model: ConnectedViewModel) {
    CommunityCommitmentsContent(state, model::requestCommunityAction)
}

@Composable
internal fun CommunityCommitmentsContent(state: ConnectedState, onAction: (CommunityAction) -> Unit) {
    if (state.community.loaded && state.community.commitments.isEmpty()) Text(stringResource(
        R.string.community_no_hay_compromisos))
    state.community.commitments.forEach { item ->
        PactCard(item.game, item.description) {
            Text("Debe cumplir: ${item.debtor.nickname}")
            Text("A favor de: ${item.creditor.nickname}")
            Text(if (item.status == "completed") "Completado" else if (item.requestedAt != null)
                "Cumplimiento solicitado; espera confirmación" else "Pendiente")
            val complete = CommunityAction("complete", item.id)
            val request = CommunityAction("request", item.id)
            if (communityActionAllowed(state, complete)) Action(stringResource(
                R.string.community_confirmar_que_cumplio), enabled = !state.busy) {
                onAction(complete)
            }
            if (communityActionAllowed(state, request)) Action(stringResource(
                R.string.community_avisar_que_ya_cumpli), enabled = !state.busy) {
                onAction(request)
            }
        }
    }
}

@Composable
internal fun CommunityNotifications(state: ConnectedState, model: ConnectedViewModel) {
    val extras = LocalExtras.current
    if (state.community.loaded && state.community.notifications.isEmpty()) Text(stringResource(
        R.string.community_no_tienes_notificaciones))
    if (state.community.notifications.any { !it.read }) Action(stringResource(
        R.string.community_marcar_todas_como_leidas), enabled = !state.busy) {
        model.requestCommunityAction(CommunityAction("readAll"))
    }
    state.community.notifications.forEach { item ->
        PactCard(item.title, item.body) {
            Text(item.createdAt)
            Action("Eliminar aviso", enabled = !state.busy) { extras?.open(ExtrasPage.DELETE_NOTICE, item.id) }
            if (item.read) Text(stringResource(R.string.community_leida)) else Action(stringResource(
                R.string.community_marcar_como_leida), enabled = !state.busy) {
                model.requestCommunityAction(CommunityAction("read", item.id))
            }
        }
    }
}
