package com.pactogg.app.feature.connected.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pactogg.app.feature.auth.presentation.AccessScreen

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
        if (state.selectedTournament != null) model.tournament(null) else model.back()
    }
    Scaffold(topBar = {
        AppHeader(state, model)
    }, bottomBar = { ConnectedNavigation(state.route, state.busy, model::navigate) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            if (state.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
            Box(Modifier.weight(1f)) { ConnectedContent(state, model) }
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
fun SafeAppContent(content: @Composable androidx.compose.foundation.layout.BoxScope.() -> Unit) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(Modifier.fillMaxSize().safeDrawingPadding()) {
            Box(Modifier.fillMaxSize().testTag("safe-app-content"), content = content)
        }
    }
}
