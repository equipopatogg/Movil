package com.pactogg.app.feature.connected.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertCountEquals
import com.pactogg.app.feature.connected.domain.Match
import com.pactogg.app.feature.connected.domain.Tournament
import com.pactogg.app.ui.theme.PactoGGTheme
import org.junit.Rule
import org.junit.Test

class TournamentBracketTest {
    @get:Rule val compose = createComposeRule()

    @Test fun nonemptyBracketShowsRoundParticipantsScoresAndNoWriteActions() {
        val match = Match("333333333333333333333333", 2, 1, "Equipo Alfa", null, 3, null, "in_progress")
        val tournament = Tournament("444444444444444444444444", "Halo", "single_elimination",
            "in_progress", "2099-11-10T00:00:00Z", listOf(match))
        compose.setContent { PactoGGTheme { androidx.compose.foundation.layout.Column { TournamentDetails(tournament) } } }
        compose.onNodeWithText("Halo").assertIsDisplayed()
        compose.onNodeWithText("Ronda 2").assertIsDisplayed()
        compose.onNodeWithText("Partida 1").assertIsDisplayed()
        compose.onNodeWithText("Equipo Alfa: 3").assertIsDisplayed()
        compose.onNodeWithText("Por definir: —").assertIsDisplayed()
        for (text in listOf("Crear torneo", "Inscribirse", "Reportar resultado")) {
            compose.onAllNodesWithText(text).assertCountEquals(0)
        }
    }
}
