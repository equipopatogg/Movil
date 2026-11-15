package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.pactogg.app.R

/** Las cinco entradas vienen de la maqueta. Explorar abre ideas locales (PL-06), nunca datos de otras personas. */
@Composable
internal fun ConnectedNavigation(route: ConnectedRoute, busy: Boolean, nav: (ConnectedRoute) -> Unit) {
    val routes = listOf(ConnectedRoute.HOME, ConnectedRoute.EXPLORE, ConnectedRoute.CREATE,
        ConnectedRoute.TOURNAMENTS, ConnectedRoute.GROUPS)
    val labels = listOf(R.string.nav_home, R.string.nav_explore, R.string.nav_create,
        R.string.nav_tournaments, R.string.nav_groups)
    val icons = listOf(R.drawable.ic_pacto_home, R.drawable.ic_pacto_bolt, R.drawable.ic_pacto_plus,
        R.drawable.ic_pacto_trophy, R.drawable.ic_pacto_users)
    NavigationBar(containerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets(0, 0, 0, 0)) {
        routes.forEachIndexed { index, target ->
            NavigationBarItem(selected = navigationSection(route) == target,
                enabled = !busy, onClick = { nav(target) },
                icon = { Icon(painterResource(icons[index]), null) },
                label = { Text(stringResource(labels[index]), style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer))
        }
    }
}

internal fun navigationSection(route: ConnectedRoute): ConnectedRoute = when (route) {
    ConnectedRoute.MEMBERS -> ConnectedRoute.GROUPS
    ConnectedRoute.BOARD, ConnectedRoute.CONTRACT, ConnectedRoute.EVIDENCE, ConnectedRoute.JURY, ConnectedRoute.VOTE,
    ConnectedRoute.NOTIFICATIONS, ConnectedRoute.STANDINGS -> ConnectedRoute.HOME
    ConnectedRoute.TOURNAMENT_NEW, ConnectedRoute.TOURNAMENT_PLAY, ConnectedRoute.MATCH -> ConnectedRoute.TOURNAMENTS
    ConnectedRoute.VOICE -> ConnectedRoute.CREATE
    else -> route
}

internal fun ConnectedViewModel.back() {
    navigate(when (state.value.route) {
        ConnectedRoute.EVIDENCE -> ConnectedRoute.CONTRACT
        ConnectedRoute.CONTRACT -> ConnectedRoute.BOARD
        ConnectedRoute.BOARD, ConnectedRoute.MEMBERS -> ConnectedRoute.GROUPS
        ConnectedRoute.VOTE -> if (state.value.play.pendingVotes.isEmpty()) ConnectedRoute.CONTRACT
            else ConnectedRoute.JURY
        ConnectedRoute.MATCH -> ConnectedRoute.TOURNAMENT_PLAY
        ConnectedRoute.TOURNAMENT_PLAY, ConnectedRoute.TOURNAMENT_NEW -> ConnectedRoute.TOURNAMENTS
        ConnectedRoute.VOICE -> ConnectedRoute.CREATE
        else -> ConnectedRoute.HOME
    })
}

/** Datos que se cargan al entrar a cada pantalla (separado de `navigate` por complejidad). */
internal fun ConnectedViewModel.loadFor(route: ConnectedRoute) {
    when (route) {
        ConnectedRoute.TOURNAMENTS -> if (!state.value.tournamentsLoaded) loadTournaments()
        ConnectedRoute.PROFILE -> loadProfile()
        ConnectedRoute.GROUPS -> loadGroups()
        ConnectedRoute.MEMBERS -> loadMembers()
        ConnectedRoute.BOARD -> loadBoard()
        ConnectedRoute.EVIDENCE -> loadEvidence()
        else -> com.pactogg.app.feature.play.presentation.onPlayRoute(this, route)
    }
}
