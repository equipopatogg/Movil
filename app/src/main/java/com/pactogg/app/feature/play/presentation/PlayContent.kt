package com.pactogg.app.feature.play.presentation

import androidx.compose.runtime.Composable
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.EmptyState
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.feature.connected.presentation.ConnectedRoute
import com.pactogg.app.feature.connected.presentation.ConnectedState
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.back

/** Pantallas nuevas (specs PL-01, PL-02, PL-03, PL-04, PL-05). Se agregan por bloques. */
@Composable
fun PlayContent(state: ConnectedState, model: ConnectedViewModel) {
    when (state.route) {
        ConnectedRoute.JURY -> JuryScreen(state, model)
        ConnectedRoute.VOTE -> VoteScreen(state, model)
        ConnectedRoute.NOTIFICATIONS -> NoticesScreen(state, model)
        ConnectedRoute.STANDINGS -> StandingsScreen(state, model)
        ConnectedRoute.TOURNAMENT_NEW -> TournamentNewScreen(state, model)
        ConnectedRoute.TOURNAMENT_PLAY -> TournamentPlayScreen(state, model)
        ConnectedRoute.MATCH -> MatchScreen(state, model)
        ConnectedRoute.VOICE -> VoiceScreen(state, model)
        else -> MakePage("Próximamente", "Esta parte se está terminando", onBack = { model.back() }) {
            EmptyState("Muy pronto.", "Estamos conectando esta pantalla con el servidor.", R.drawable.ic_pacto_clock)
        }
    }
}
