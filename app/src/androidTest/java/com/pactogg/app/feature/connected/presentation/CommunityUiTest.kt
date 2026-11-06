package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithText
import com.pactogg.app.feature.community.domain.*
import com.pactogg.app.ui.theme.PactoGGTheme
import org.junit.Rule
import org.junit.Test

class CommunityUiTest {
    @get:Rule val compose = createComposeRule()
    @Test fun PTS_01_displaysServerBalanceAndMovements() {
        val points = MyPointsModel("group", 75, listOf(PointsMovementModel("movement", "bond_hold",
            -25, 75, "contract", "contract", "2026-11-05")))
        compose.setContent { PactoGGTheme { Column {
            CommunityPoints(ConnectedState(community = CommunityState(loaded = true, points = points)))
        } } }
        compose.onNodeWithText("Saldo: 75 puntos virtuales, sin valor monetario").assertExists()
        compose.onNodeWithText("Retención de fianza").assertExists()
    }
    @Test fun PTS_02_emptyRankingIsHonest() {
        compose.setContent { PactoGGTheme { Column {
            CommunityRanking(ConnectedState(community = CommunityState(loaded = true)))
        } } }
        compose.onNodeWithText("Sin posiciones disponibles.").assertExists()
    }
    @Test fun ACH_01_emptyFameDoesNotShowSampleRewards() {
        compose.setContent { PactoGGTheme { Column {
            CommunityFame(ConnectedState(community = CommunityState(loaded = true)))
        } } }
        compose.onNodeWithText("Todavía no hay retos en el muro.").assertExists()
    }
    @Test fun VOT_02_nonJurorCannotSeeVoteButtons() {
        val summary = VoteSummaryModel("contract", "voting", 3, 0, 3,
            iAmJuror = false, iVoted = false, adminCanResolve = false)
        compose.setContent { PactoGGTheme { Column {
            CommunityVotingContent(ConnectedState(community = CommunityState(loaded = true, summary = summary)),
                {}, {}, {})
        } } }
        compose.onNodeWithText("Votar legítimo").assertDoesNotExist()
        compose.onNodeWithText("Votar trampa").assertDoesNotExist()
        compose.onNodeWithText("Resolver como cumplido").assertDoesNotExist()
    }
    @Test fun VOT_02_jurorChoosesVoteWithoutAutomaticSubmission() {
        var chosen: CommunityAction? = null
        val summary = VoteSummaryModel("contract", "voting", 3, 0, 3,
            iAmJuror = true, iVoted = false, adminCanResolve = false)
        compose.setContent { PactoGGTheme { Column {
            CommunityVotingContent(ConnectedState(community = CommunityState(loaded = true, summary = summary)),
                {}, { chosen = it }, {})
        } } }
        compose.onNodeWithText("Votar legítimo").performClick()
        compose.runOnIdle { org.junit.Assert.assertEquals(CommunityAction("vote", "contract", "legit"), chosen) }
    }
}
