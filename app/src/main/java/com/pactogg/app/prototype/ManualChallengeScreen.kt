package com.pactogg.app.prototype

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.PageLayout
import com.pactogg.app.feature.explore.presentation.IdeaPresentation

@Composable
fun ManualChallengeScreen(idea: IdeaPresentation?, onExit: () -> Unit) {
    var step by remember { mutableStateOf(1) }
    var game by remember { mutableStateOf(idea?.game.orEmpty()) }
    var condition by remember { mutableStateOf(idea?.description.orEmpty()) }
    var deadline by remember { mutableStateOf("") }
    var rival by remember { mutableStateOf("") }
    var agreement by remember { mutableStateOf("") }
    val members = listOf(stringResource(R.string.demo_member_one), stringResource(R.string.demo_member_two))
    val back = { if (step > 1) step -= 1 else onExit() }
    BackHandler(onBack = back)
    PageLayout {
        Text(stringResource(R.string.demo_challenge_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.demo_local_notice), color = MaterialTheme.colorScheme.secondary)
        HeroPanel("NUEVO PACTO", stringResource(R.string.demo_step, step.coerceAtMost(3)),
            "Condiciones claras antes de jugar.", R.drawable.ic_pacto_bolt)
        Choices(listOf("1 · Condiciones", "2 · Rival", "3 · Revisar"),
            listOf("1 · Condiciones", "2 · Rival", "3 · Revisar")[step.coerceIn(1, 3) - 1]) { choice ->
            val target = choice.substringBefore(" ").toInt()
            if (target < step) step = target
        }
        when (step) {
            1 -> {
                DemoField(stringResource(R.string.demo_game), game) { game = it }
                DemoField(stringResource(R.string.demo_condition), condition) { condition = it }
                DemoField(stringResource(R.string.demo_deadline), deadline) { deadline = it }
                Text(stringResource(R.string.demo_group))
            }
            2 -> {
                Text(stringResource(R.string.demo_choose_rival))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    members.forEach { member ->
                        FilterChip(selected = rival == member, onClick = { rival = member }, label = { Text(member) })
                    }
                }
                DemoField(stringResource(R.string.demo_agreement), agreement) { agreement = it }
            }
            3 -> {
                Text(game, style = MaterialTheme.typography.titleLarge)
                Text(condition)
                Text(stringResource(R.string.demo_summary, deadline, rival, agreement))
                TextButton(onClick = { step = 1 }) { Text(stringResource(R.string.demo_edit_conditions)) }
                TextButton(onClick = { step = 2 }) { Text(stringResource(R.string.demo_edit_rival)) }
            }
            else -> {
                Text(stringResource(R.string.demo_created), style = MaterialTheme.typography.titleLarge)
                Text(condition)
                Text(stringResource(R.string.demo_not_sent))
            }
        }
        if (step <= 3) {
            val valid = validDraftStep(step, game, condition, deadline, rival, agreement)
            Button(onClick = { step += 1 }, enabled = valid, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(if (step == 3) R.string.demo_launch else R.string.demo_next))
            }
            TextButton(onClick = back) { Text(stringResource(R.string.demo_back)) }
        } else {
            Button(onClick = onExit, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.demo_return_explore))
            }
        }
    }
}

@Composable
private fun DemoField(label: String, value: String, onChange: (String) -> Unit) {
    Field(label, value, onChange)
}

private fun validDraftStep(step: Int, game: String, condition: String, deadline: String,
    rival: String, agreement: String): Boolean = when (step) {
    1 -> game.isNotBlank() && condition.isNotBlank() && deadline.isNotBlank()
    2 -> rival.isNotBlank() && agreement.isNotBlank()
    else -> true
}
