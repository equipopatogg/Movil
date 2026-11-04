package com.pactogg.app.feature.groups.presentation

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.InitialFeatureScreen

@Composable
fun GroupsScreen(onBack: () -> Unit) {
    InitialFeatureScreen(stringResource(R.string.my_groups), stringResource(R.string.groups_intro)) {
        TextButton(onClick = onBack) { Text(stringResource(R.string.back_to_profile)) }
    }
}
