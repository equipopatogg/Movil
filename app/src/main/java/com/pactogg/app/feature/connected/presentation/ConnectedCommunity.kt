package com.pactogg.app.feature.connected.presentation

import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage

@Composable
internal fun CommunityHomeActions(state: ConnectedState, model: ConnectedViewModel) {
    Action(stringResource(R.string.community_jurado_votaciones_pendientes), enabled = !state.busy) {
        model.navigate(ConnectedRoute.JURY) }
    Action(stringResource(R.string.community_notificaciones), enabled = !state.busy) { model.navigate(
        ConnectedRoute.NOTIFICATIONS) }
    if (state.group != null) {
        Action(stringResource(R.string.community_mis_puntos), enabled = !state.busy) { model.navigate(
            ConnectedRoute.POINTS) }
        Action(stringResource(R.string.community_ranking_del_grupo), enabled = !state.busy) { model.navigate(
            ConnectedRoute.RANKING) }
        Action(stringResource(R.string.community_compromisos), enabled = !state.busy) { model.navigate(
            ConnectedRoute.COMMITMENTS) }
        Action(stringResource(R.string.community_muro_de_la_fama), enabled = !state.busy) { model.navigate(
            ConnectedRoute.FAME) }
    }
}

@Composable
internal fun ConnectedCommunity(state: ConnectedState, model: ConnectedViewModel) {
    val title = communityTitle(state.route)
    AppPage(title, state.group?.name.orEmpty()) {
        Action(stringResource(R.string.community_actualizar), enabled = !state.busy) { model.loadCommunity() }
        if (!state.community.loaded && !state.busy) Text(stringResource(
            R.string.community_actualiza_para_consultar_los_datos_del_servicio))
        CommunityContent(state, model)
        if (state.community.more) Action(stringResource(R.string.community_cargar_mas), enabled =
            !state.busy) { model.loadCommunity(true) }
    }
    state.community.confirmation?.let { action ->
        ActionConfirmation("¿Confirmar esta acción?",
            if (action.kind == "vote") "Tu voto será definitivo y no se podrá cambiar."
            else "Se enviará la acción al servicio. Espera la confirmación antes de repetirla.",
            confirmLabel = "Confirmar", enabled = !state.busy,
            confirmEnabled = !state.busy && communityActionAllowed(state, action),
            onConfirm = model::confirmCommunityAction, onDismiss = { model.requestCommunityAction(null) })
    }
}

private fun communityTitle(route: ConnectedRoute): String = when (route) {
    ConnectedRoute.JURY -> "Jurado"
    ConnectedRoute.VOTING -> "Votación y resultado"
    ConnectedRoute.POINTS -> "Mis puntos"
    ConnectedRoute.RANKING -> "Ranking"
    ConnectedRoute.COMMITMENTS -> "Compromisos"
    ConnectedRoute.NOTIFICATIONS -> "Notificaciones"
    else -> "Muro de la fama"
}

@Composable
private fun CommunityContent(state: ConnectedState, model: ConnectedViewModel) {
    when (state.route) {
        ConnectedRoute.JURY -> CommunityJury(state, model)
        ConnectedRoute.VOTING -> CommunityVoting(state, model)
        ConnectedRoute.POINTS -> CommunityPoints(state)
        ConnectedRoute.RANKING -> CommunityRanking(state)
        ConnectedRoute.COMMITMENTS -> CommunityCommitments(state, model)
        ConnectedRoute.NOTIFICATIONS -> CommunityNotifications(state, model)
        ConnectedRoute.FAME -> CommunityFame(state)
        else -> Unit
    }
}
