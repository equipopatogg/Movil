package com.pactogg.app.feature.connected.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pactogg.app.R
import com.pactogg.app.feature.auth.presentation.AccessScreen
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.HeroPanel
import com.pactogg.app.prototype.PactCard
import com.pactogg.app.prototype.Action

@Composable
fun ConnectedApp(model: ConnectedViewModel) {
    val state by model.state.collectAsStateWithLifecycle()
    if (state.restoring) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    if (state.account == null) {
        AccessScreen(state.access.copy(loading = state.busy), model::access) {
            Feedback(state.error, state.notice)
        }
        return
    }
    BackHandler(state.route != ConnectedRoute.HOME || state.selectedTournament != null) {
        if (state.selectedTournament != null) model.tournament(null) else model.navigate(ConnectedRoute.HOME)
    }
    Scaffold(topBar = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { model.navigate(ConnectedRoute.HOME) }, enabled = !state.busy) {
                Text("PACTOGG", color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { model.navigate(ConnectedRoute.PROFILE) }, enabled = !state.busy) {
                Text(requireNotNull(state.account).nickname)
            }
        }
    }, bottomBar = { ConnectedNavigation(state.route, state.busy, model::navigate) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Feedback(state.error, state.notice)
            if (state.busy) CircularProgressIndicator(Modifier.padding(12.dp))
            Box(Modifier.weight(1f)) { ConnectedContent(state, model) }
        }
    }
}

@Composable
private fun ConnectedNavigation(route: ConnectedRoute, busy: Boolean, nav: (ConnectedRoute) -> Unit) {
    val routes = listOf(ConnectedRoute.HOME, ConnectedRoute.GROUPS, ConnectedRoute.TOURNAMENTS)
    val labels = listOf("Inicio", "Grupos", "Torneos")
    val icons = listOf(R.drawable.ic_pacto_home, R.drawable.ic_pacto_users, R.drawable.ic_pacto_trophy)
    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
        routes.forEachIndexed { index, target ->
            NavigationBarItem(selected = route == target, enabled = !busy, onClick = { nav(target) },
                icon = { Icon(painterResource(icons[index]), null) }, label = { Text(labels[index]) })
        }
    }
}

@Composable
private fun ConnectedContent(state: ConnectedState, model: ConnectedViewModel) {
    when (state.route) {
        ConnectedRoute.HOME -> ConnectedHome(state, model)
        ConnectedRoute.PROFILE -> ConnectedProfile(state, model)
        ConnectedRoute.GROUPS -> ConnectedGroups(state, model)
        ConnectedRoute.TOURNAMENTS -> ConnectedTournaments(state, model)
        else -> ConnectedHome(state, model)
    }
}

@Composable
private fun ConnectedHome(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Tu palabra.\nTu juego.", "Bienvenido, ${state.account?.nickname.orEmpty()}") {
        HeroPanel("PACTOGG", "Tu palabra cuenta.",
            "Tu cuenta está conectada al servicio.", R.drawable.ic_pacto_bolt)
        PactCard("Sesión activa", "Accede a tu perfil para cerrar sesión.", R.drawable.ic_pacto_shield) {
            Action("Ver mi perfil", enabled = !state.busy) { model.navigate(ConnectedRoute.PROFILE) }
        }
        PactCard(state.group?.name ?: "Crea tu grupo", "Grupo creado durante esta sesión.",
            R.drawable.ic_pacto_users) {
            Action("Crear o ver grupo", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
        }
        Action("Consultar torneos", enabled = !state.busy) { model.navigate(ConnectedRoute.TOURNAMENTS) }
    }
}

@Composable
private fun ConnectedProfile(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Tu perfil.", "Datos de tu sesión") {
        PactCard(state.account?.nickname.orEmpty(), state.account?.email.orEmpty(), R.drawable.ic_pacto_user)
        Text("El cierre de sesión borra la sesión local; no elimina tu cuenta.")
        Action("Cerrar sesión", enabled = !state.busy) { model.logout() }
    }
}

@Composable
fun Feedback(error: String?, notice: String?) {
    val text = error ?: notice ?: return
    Text(text, Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
        color = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary)
}
