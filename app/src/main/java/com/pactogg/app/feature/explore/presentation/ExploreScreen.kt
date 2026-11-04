package com.pactogg.app.feature.explore.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.prototype.HeroPanel
import com.pactogg.app.prototype.IconBadge
import com.pactogg.app.core.designsystem.PageLayout

@Composable
fun ExploreScreen(state: ExploreState, onEvent: (ExploreEvent) -> Unit) {
    PageLayout {
        Text(stringResource(R.string.ui_explore), style = MaterialTheme.typography.titleLarge)
        HeroPanel("ELIGE TU PRÓXIMO DESAFÍO", stringResource(R.string.ui_explore_hero),
            stringResource(R.string.ui_explore_intro), R.drawable.ic_pacto_bolt)
        OutlinedTextField(value = state.query, onValueChange = { onEvent(ExploreEvent.Query(it)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            label = { Text(stringResource(R.string.ui_explore_search)) },
            leadingIcon = { Icon(painterResource(R.drawable.ic_pacto_search), contentDescription = null) })
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IdeaCategory.entries.forEach { item ->
                FilterChip(selected = state.category == item,
                    onClick = { onEvent(ExploreEvent.Category(item)) },
                    label = { Text(stringResource(item.labelResource())) })
            }
        }
        ExploreResults(state, onEvent)
    }
}

@Composable
private fun ExploreResults(state: ExploreState, onEvent: (ExploreEvent) -> Unit) {
    when (state.status) {
        ExploreStatus.LOADING -> {
            CircularProgressIndicator()
            Text(stringResource(R.string.ui_loading))
        }
        ExploreStatus.ERROR -> Text(stringResource(R.string.ui_load_error))
        ExploreStatus.READY -> {
            val visible = state.visibleIdeas()
            if (visible.isEmpty()) {
                Text(stringResource(if (state.ideas.isEmpty()) R.string.ui_explore_empty
                    else R.string.ui_explore_no_matches), style = MaterialTheme.typography.titleMedium)
                if (state.query.isNotEmpty() || state.category != IdeaCategory.ALL) {
                    TextButton(onClick = { onEvent(ExploreEvent.Clear) }) {
                        Text(stringResource(R.string.ui_explore_clear))
                    }
                }
            } else visible.forEach { IdeaCard(it, onEvent) }
        }
    }
}

@Composable
private fun IdeaCard(idea: IdeaPresentation, onEvent: (ExploreEvent) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            IconBadge(R.drawable.ic_pacto_bolt, lavender = true)
            Text(idea.game, color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelLarge)
            Text(idea.title, style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary)
            Text(idea.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
            TextButton(onClick = { onEvent(ExploreEvent.Details(idea)) }) {
                Text(stringResource(R.string.ui_explore_details))
            }
            Button(onClick = { onEvent(ExploreEvent.UseIdea(idea)) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.ui_explore_use))
            }
        }
    }
}

private fun IdeaCategory.labelResource(): Int = when (this) {
    IdeaCategory.ALL -> R.string.ui_category_all
    IdeaCategory.SKILL -> R.string.ui_category_skill
    IdeaCategory.COOPERATIVE -> R.string.ui_category_cooperative
    IdeaCategory.CONSISTENCY -> R.string.ui_category_consistency
}
