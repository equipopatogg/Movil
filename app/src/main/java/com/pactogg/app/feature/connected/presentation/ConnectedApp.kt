package com.pactogg.app.feature.connected.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
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
    SafeAppContent {
        ConnectedBody(state, model)
        AppFeedback(state, model::dismissError, model::dismissNotice)
    }
}

@Composable
private fun ConnectedBody(state: ConnectedState, model: ConnectedViewModel) {
    if (state.restoring) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    if (state.account == null) {
        AccessScreen(state.access.copy(loading = state.busy), model::access,
            fieldError = state.inlineFeedback(), fieldErrorCode = state.diagnostic?.code) {
            if (state.access.registering) state.social.nicknameNotice?.let { Text(it) }
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
            if (state.busy) CircularProgressIndicator(Modifier.padding(12.dp))
            Box(Modifier.weight(1f)) { ConnectedContent(state, model) }
        }
    }
}

@Composable
private fun ConnectedNavigation(route: ConnectedRoute, busy: Boolean, nav: (ConnectedRoute) -> Unit) {
    val routes = listOf(ConnectedRoute.HOME, ConnectedRoute.CREATE,
        ConnectedRoute.TOURNAMENTS, ConnectedRoute.GROUPS)
    val labels = listOf("Inicio", "Crear", "Torneos", "Grupos")
    val icons = listOf(R.drawable.ic_pacto_home, R.drawable.ic_pacto_plus,
        R.drawable.ic_pacto_trophy, R.drawable.ic_pacto_users)
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
        ConnectedRoute.GROUPS -> ConnectedGroups(state, model)
        ConnectedRoute.MEMBERS -> ConnectedMembers(state, model)
        ConnectedRoute.BOARD -> ConnectedBoard(state, model)
        ConnectedRoute.CREATE -> ConnectedCreate(state, model)
        ConnectedRoute.CONTRACT -> ConnectedContract(state, model)
        ConnectedRoute.EVIDENCE -> ConnectedEvidence(state, model)
        ConnectedRoute.TOURNAMENTS -> ConnectedTournaments(state, model)
        ConnectedRoute.PROFILE -> ConnectedProfile(state, model)
    }
}

@Composable
private fun ConnectedHome(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Tu palabra.\nTu juego.", "Bienvenido, ${state.account?.nickname.orEmpty()}") {
        HeroPanel("JUEGA CON TU GRUPO", "Crea tu próximo reto.",
            "Grupos, retos manuales y torneos disponibles en el servicio.", R.drawable.ic_pacto_bolt)
        PactCard(state.group?.name ?: "Tus grupos", "Recupera tus grupos o únete a tu equipo.",
            R.drawable.ic_pacto_users) {
            Action("Grupos", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
        }
        if (state.group != null) Action("Tablero de retos", enabled = !state.busy) {
            model.navigate(ConnectedRoute.BOARD)
        }
        if (state.group != null) Action("Crear reto manual", enabled = !state.busy) {
            model.navigate(ConnectedRoute.CREATE)
        }
        Action("Consultar torneos", enabled = !state.busy) { model.navigate(ConnectedRoute.TOURNAMENTS) }
        if (state.contract != null) Action("Ver reto seleccionado", enabled = !state.busy) {
            model.navigate(ConnectedRoute.CONTRACT)
        }
    }
}

@Composable
fun SafeAppContent(content: @Composable androidx.compose.foundation.layout.BoxScope.() -> Unit) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(Modifier.fillMaxSize().safeDrawingPadding()) {
            Box(Modifier.fillMaxSize().testTag("safe-app-content"), content = content)
        }
    }
}
