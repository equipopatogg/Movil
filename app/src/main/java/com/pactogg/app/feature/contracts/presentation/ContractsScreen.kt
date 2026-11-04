package com.pactogg.app.feature.contracts.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.InitialFeatureScreen

@Composable
fun ContractsScreen() {
    InitialFeatureScreen(stringResource(R.string.tab_contracts), stringResource(R.string.contracts_intro))
}
