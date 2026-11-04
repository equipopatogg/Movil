package com.pactogg.app.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pactogg.app.feature.commitments.presentation.CommitmentsScreen
import com.pactogg.app.feature.contracts.presentation.ContractsScreen
import com.pactogg.app.feature.groups.presentation.GroupsScreen
import com.pactogg.app.feature.profile.presentation.ProfileScreen
import com.pactogg.app.feature.tournaments.presentation.TournamentsScreen

@Composable
fun PactoGGApp(shellViewModel: ShellViewModel = viewModel()) {
    val controller = rememberNavController()
    val backStack by controller.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val environment by shellViewModel.environment.collectAsStateWithLifecycle()
    Scaffold(
        bottomBar = {
            if (route != GROUPS_ROUTE) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.background, tonalElevation = 0.dp) {
                    Destination.entries.forEach { destination ->
                        NavigationBarItem(
                            selected = route == destination.route,
                            modifier = Modifier.testTag("nav_${destination.route}"),
                            onClick = {
                                controller.navigate(destination.route) {
                                    popUpTo(controller.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Text(destination.mark) },
                            label = { Text(stringResource(destination.label)) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(controller, startDestination = Destination.CONTRACTS.route,
            modifier = Modifier.fillMaxSize().padding(padding)) {
            composable(Destination.CONTRACTS.route) { ContractsScreen() }
            composable(Destination.TOURNAMENTS.route) { TournamentsScreen() }
            composable(Destination.COMMITMENTS.route) { CommitmentsScreen() }
            composable(Destination.PROFILE.route) {
                ProfileScreen(environment) { controller.navigate(GROUPS_ROUTE) }
            }
            composable(GROUPS_ROUTE) { GroupsScreen { controller.popBackStack() } }
        }
    }
}
