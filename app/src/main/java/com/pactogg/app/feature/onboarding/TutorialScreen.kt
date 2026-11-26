package com.pactogg.app.feature.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoViolet

/** PL-15: tutorial de primer uso a pantalla completa, paso por paso. */
@Composable
fun TutorialScreen(model: TutorialViewModel) {
    val pager by model.pager.collectAsStateWithLifecycle()
    val step = TutorialSteps.all[pager.index]
    BackHandler { if (pager.isFirst) model.finish() else model.back() }
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).testTag("tutorial")
        .verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Kicker("Cómo usar PactoGG · ${pager.index + 1} de ${pager.total}", PactoCyan)
            Box(Modifier.weight(1f))
            MakeButton("Saltar tutorial", ButtonKind.Ghost, icon = null, onClick = model::finish)
        }
        LinearProgressIndicator(progress = { (pager.index + 1f) / pager.total }, modifier = Modifier.fillMaxWidth())
        MakeCard(accent = PactoViolet) {
            Box(Modifier.size(56.dp).background(PactoViolet, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center) {
                Icon(painterResource(step.icon), null, tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(30.dp))
            }
            Text(step.title, style = MaterialTheme.typography.headlineSmall)
            Kicker("Dónde está: ${step.where}")
            Text(step.body, style = MaterialTheme.typography.bodyLarge)
        }
        MakeButton(if (pager.isLast) "¡Listo, a jugar!" else "Siguiente",
            onClick = if (pager.isLast) model::finish else model::next)
        if (!pager.isFirst) MakeButton("Atrás", ButtonKind.Secondary, icon = null, onClick = model::back)
    }
}
