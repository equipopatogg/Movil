package com.pactogg.app.feature.tournaments.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.InitialFeatureScreen

@Composable
fun TournamentsScreen() {
    InitialFeatureScreen(stringResource(R.string.tab_tournaments), stringResource(R.string.tournaments_intro))
}
