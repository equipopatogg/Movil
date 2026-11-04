package com.pactogg.app.feature.commitments.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.InitialFeatureScreen

@Composable
fun CommitmentsScreen() {
    InitialFeatureScreen(stringResource(R.string.tab_commitments), stringResource(R.string.commitments_intro))
}
