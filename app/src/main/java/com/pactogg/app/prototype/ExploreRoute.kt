package com.pactogg.app.prototype

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import com.pactogg.app.feature.explore.presentation.ExploreEvent
import com.pactogg.app.feature.explore.presentation.ExploreScreen
import com.pactogg.app.feature.explore.presentation.ExploreState
import com.pactogg.app.feature.explore.presentation.ExploreStatus
import com.pactogg.app.feature.explore.presentation.IdeaCategory
import com.pactogg.app.feature.explore.presentation.IdeaPresentation
import com.pactogg.app.feature.home.presentation.ContentStatus

@Composable
fun ExploreRoute(status: ContentStatus, onUseIdea: (IdeaPresentation) -> Unit) {
    val samples = listOf(
        IdeaPresentation("sample-control", "Rocket League", stringResource(R.string.ui_idea_control),
            stringResource(R.string.ui_idea_control_copy), IdeaCategory.SKILL),
        IdeaPresentation("sample-squad", "Valorant", stringResource(R.string.ui_idea_squad),
            stringResource(R.string.ui_idea_squad_copy), IdeaCategory.COOPERATIVE),
        IdeaPresentation("sample-match", "EA Sports FC", stringResource(R.string.ui_idea_match),
            stringResource(R.string.ui_idea_match_copy), IdeaCategory.SKILL),
        IdeaPresentation("sample-routine", "Rocket League", stringResource(R.string.ui_idea_routine),
            stringResource(R.string.ui_idea_routine_copy), IdeaCategory.CONSISTENCY),
    )
    var state by remember { mutableStateOf(ExploreState()) }
    var dialog by remember { mutableStateOf<IdeaPresentation?>(null) }
    ExploreScreen(state.copy(ideas = samples, status = when (status) {
        ContentStatus.EMPTY -> ExploreStatus.READY
        ContentStatus.LOADING -> ExploreStatus.LOADING
        ContentStatus.ERROR -> ExploreStatus.ERROR
    })) { event ->
        when (event) {
            is ExploreEvent.Query -> state = state.copy(query = event.text)
            is ExploreEvent.Category -> state = state.copy(category = event.category)
            ExploreEvent.Clear -> state = ExploreState()
            is ExploreEvent.Details -> { dialog = event.idea }
            is ExploreEvent.UseIdea -> onUseIdea(event.idea)
        }
    }
    dialog?.let { idea ->
        AlertDialog(onDismissRequest = { dialog = null }, title = { Text(idea.title) },
            text = { Text(idea.description + "\n\n" + stringResource(R.string.ui_sample_only)) },
            dismissButton = {
                TextButton(onClick = { dialog = null; onUseIdea(idea) }) {
                    Text(stringResource(R.string.ui_explore_use))
                }
            },
            confirmButton = {
                TextButton(onClick = { dialog = null }) { Text(stringResource(R.string.ui_close)) }
            })
    }
}
