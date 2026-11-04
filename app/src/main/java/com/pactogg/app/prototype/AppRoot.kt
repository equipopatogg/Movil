package com.pactogg.app.prototype

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.pactogg.app.R
import com.pactogg.app.feature.auth.presentation.AccessState
import com.pactogg.app.feature.auth.presentation.AccessEvent
import com.pactogg.app.feature.auth.presentation.AccessScreen
import com.pactogg.app.feature.auth.presentation.reduce
import com.pactogg.app.feature.explore.presentation.IdeaPresentation
import com.pactogg.app.feature.home.presentation.ContentStatus

@Composable
fun AppRoot() {
    var history by remember { mutableStateOf(listOf("access")) }
    var access by remember { mutableStateOf(AccessState()) }
    var idea by remember { mutableStateOf<IdeaPresentation?>(null) }
    val route = history.last()
    val navigate: (String) -> Unit = { target ->
        if (target == "access") { access = AccessState(); history = listOf(target) }
        else if (target != route) history = history + target
    }
    BackHandler(history.size > 1) { history = history.dropLast(1) }
    Scaffold(topBar = {
        if (route !in listOf("access", "recovery", "welcome")) Column {
            Row {
                TextButton(onClick = { navigate("home") }) {
                    Text("P", color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium)
                    Text("  PACTOGG", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { navigate("notifications") }) {
                    Icon(painterResource(R.drawable.ic_pacto_bell), "Notificaciones")
                }
                IconButton(onClick = { navigate("profile") }) {
                    Icon(painterResource(R.drawable.ic_pacto_user), "Perfil")
                }
            }
            Text("Sin conexión al servidor · datos locales de prueba",
                modifier = Modifier.padding(horizontal = androidx.compose.ui.unit.Dp(18f)),
                color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
        }
    }, bottomBar = {
        if (route !in listOf("access", "recovery", "welcome")) BottomRoutes(route) { target ->
            if (target == "create") idea = null
            history = listOf("home", target).distinct()
        }
    }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            PrimaryRoute(route, access, { event ->
                if (event == AccessEvent.Submit) {
                    access = AccessState(); history = listOf("home")
                } else access = access.reduce(event)
            }, idea, { idea = it; navigate("create") }, navigate)
        }
    }
}

@Composable
private fun BottomRoutes(route: String, onNavigate: (String) -> Unit) {
    val routes = listOf("home", "explore", "create", "tournaments", "groups")
    val labels = listOf("Inicio", "Explorar", "Crear", "Torneos", "Grupos")
    val icons = listOf(R.drawable.ic_pacto_home, R.drawable.ic_pacto_search, R.drawable.ic_pacto_plus,
        R.drawable.ic_pacto_trophy, R.drawable.ic_pacto_users)
    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
        routes.forEachIndexed { index, target ->
            NavigationBarItem(selected = route == target, onClick = { onNavigate(target) },
                icon = { Icon(painterResource(icons[index]), null) }, label = { Text(labels[index]) },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant))
        }
    }
}

@Composable
private fun PrimaryRoute(route: String, access: AccessState, onAccess: (AccessEvent) -> Unit,
    idea: IdeaPresentation?, onIdea: (IdeaPresentation) -> Unit, nav: (String) -> Unit) {
    when (route) {
        "access" -> AccessScreen(access, onAccess, tools = {
            TextButton(onClick = { nav("recovery") }) { Text("Recuperar acceso") }
            TextButton(onClick = { nav("welcome") }) { Text("Conoce PactoGG") }
        })
        "recovery" -> RecoveryPage { nav("access") }
        "welcome" -> WelcomePage { nav("access") }
        "home" -> HomePage(nav)
        "explore" -> ExploreRoute(ContentStatus.EMPTY, onIdea)
        "create" -> ManualChallengeScreen(idea) { nav("home") }
        "groups" -> GroupsPage()
        "profile" -> ProfilePage(nav)
        else -> SecondaryRoute(route, nav)
    }
}

@Composable
private fun SecondaryRoute(route: String, nav: (String) -> Unit) {
    when (route) {
        "tournaments", "new-tournament", "bracket", "match" -> TournamentPage(route, nav)
        "contract", "evidence", "jury" -> ReviewPage(route, nav)
        "voice" -> VoicePage(nav)
        "ranking" -> RankingPage(nav)
        "notifications" -> NotificationsPage(nav)
    }
}
