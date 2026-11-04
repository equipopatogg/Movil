package com.pactogg.app.feature.home.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.PageLayout
import com.pactogg.app.core.designsystem.AccentHeading

enum class FirstEntryPage { HOME, PROFILE, GROUPS }
enum class ContentStatus { EMPTY, LOADING, ERROR }

@Composable
fun FirstEntryScreen(page: FirstEntryPage, status: ContentStatus, onNavigate: (FirstEntryPage) -> Unit) {
    PageLayout {
        Text(stringResource(R.string.app_name), color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge)
        AccentHeading(stringResource(when (page) {
            FirstEntryPage.HOME -> R.string.ui_home_hero
            FirstEntryPage.PROFILE -> R.string.ui_profile_title
            FirstEntryPage.GROUPS -> R.string.ui_groups_title
        }))
        EntryCard(page, status)
        if (page == FirstEntryPage.HOME) {
            Button(onClick = { onNavigate(FirstEntryPage.GROUPS) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.my_groups))
            }
            OutlinedButton(onClick = { onNavigate(FirstEntryPage.PROFILE) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.tab_profile))
            }
        } else {
            OutlinedButton(onClick = { onNavigate(FirstEntryPage.HOME) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.ui_back_home))
            }
        }
    }
}

@Composable
private fun EntryCard(page: FirstEntryPage, status: ContentStatus) {
    Card(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(when (status) {
                ContentStatus.LOADING -> R.string.ui_loading
                ContentStatus.ERROR -> R.string.ui_load_error
                ContentStatus.EMPTY -> when (page) {
                    FirstEntryPage.HOME -> R.string.ui_no_activity
                    FirstEntryPage.PROFILE -> R.string.ui_no_profile
                    FirstEntryPage.GROUPS -> R.string.ui_no_groups
                }
            }), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.ui_sample_only), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
