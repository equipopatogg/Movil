package com.pactogg.app.feature.profile.presentation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.InitialFeatureScreen
import com.pactogg.app.core.environment.AppEnvironment

@Composable
fun ProfileScreen(environment: AppEnvironment, onGroups: () -> Unit) {
    InitialFeatureScreen(stringResource(R.string.tab_profile), stringResource(R.string.profile_intro)) {
        Text(stringResource(R.string.app_version, environment.version))
        Text(stringResource(if (environment.apiConfigured) R.string.api_ready else R.string.api_pending))
        Button(onClick = onGroups) { Text(stringResource(R.string.my_groups)) }
    }
}
