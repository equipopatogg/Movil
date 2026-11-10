package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.pactogg.app.R

/** The five entries come from Make. Unavailable entries never open prototype data. */
@Composable
internal fun ConnectedNavigation(route: ConnectedRoute, busy: Boolean, nav: (ConnectedRoute) -> Unit) {
    var upcoming by rememberSaveable { mutableStateOf(false) }
    val routes = listOf(ConnectedRoute.HOME, null, ConnectedRoute.CREATE,
        ConnectedRoute.TOURNAMENTS, ConnectedRoute.GROUPS)
    val labels = listOf(R.string.nav_home, R.string.nav_explore, R.string.nav_create,
        R.string.nav_tournaments, R.string.nav_groups)
    val icons = listOf(R.drawable.ic_pacto_home, R.drawable.ic_pacto_bolt, R.drawable.ic_pacto_plus,
        R.drawable.ic_pacto_trophy, R.drawable.ic_pacto_users)
    NavigationBar(containerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets(0, 0, 0, 0)) {
        routes.forEachIndexed { index, target ->
            NavigationBarItem(selected = target != null && navigationSection(route) == target,
                enabled = !busy, onClick = { if (target == null) upcoming = true else nav(target) },
                icon = { Icon(painterResource(icons[index]), null) },
                label = { Text(stringResource(labels[index]), style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer))
        }
    }
    if (upcoming) AlertDialog(onDismissRequest = { upcoming = false },
        title = { Text(stringResource(R.string.nav_explore)) },
        text = { Text(stringResource(R.string.section_upcoming)) },
        confirmButton = { TextButton(onClick = { upcoming = false }) {
            Text(stringResource(R.string.understood))
        } })
}

internal fun navigationSection(route: ConnectedRoute): ConnectedRoute = when (route) {
    ConnectedRoute.MEMBERS -> ConnectedRoute.GROUPS
    ConnectedRoute.BOARD, ConnectedRoute.CONTRACT, ConnectedRoute.EVIDENCE -> ConnectedRoute.HOME
    ConnectedRoute.JURY, ConnectedRoute.VOTING, ConnectedRoute.NOTIFICATIONS -> ConnectedRoute.HOME
    ConnectedRoute.POINTS, ConnectedRoute.RANKING, ConnectedRoute.COMMITMENTS, ConnectedRoute.FAME ->
        ConnectedRoute.GROUPS
    else -> route
}

internal fun ConnectedViewModel.back() {
    navigate(when (state.value.route) {
        ConnectedRoute.EVIDENCE -> state.value.evidenceReturnRoute
        ConnectedRoute.VOTING -> ConnectedRoute.CONTRACT
        ConnectedRoute.CONTRACT -> ConnectedRoute.BOARD
        ConnectedRoute.BOARD, ConnectedRoute.MEMBERS -> ConnectedRoute.GROUPS
        else -> ConnectedRoute.HOME
    })
}
