package com.pactogg.app.feature.play.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.EmptyState
import com.pactogg.app.core.designsystem.GameMark
import com.pactogg.app.core.designsystem.HeroTitle
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.SafetyNote
import com.pactogg.app.core.designsystem.SectionTitle
import com.pactogg.app.core.designsystem.Segmented
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.feature.assist.presentation.AiIdeasViewModel
import com.pactogg.app.feature.connected.presentation.ConnectedState
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.explore.presentation.ExploreState
import com.pactogg.app.feature.explore.presentation.IdeaCategory
import com.pactogg.app.feature.explore.presentation.IdeaPresentation
import com.pactogg.app.feature.explore.presentation.dailyIdeas
import com.pactogg.app.feature.explore.presentation.parseIdeas
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoViolet
import java.util.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val IdeasAsset = "explorar/retos.json"
private const val DaysPerYear = 1000L
private val CategoryLabels = listOf("Todos" to IdeaCategory.ALL, "Habilidad" to IdeaCategory.SKILL,
    "Cooperativo" to IdeaCategory.COOPERATIVE, "Constancia" to IdeaCategory.CONSISTENCY)

/** Explorar (maqueta `explorar`, spec PL-06): 4 ideas del día de un catálogo local, con descartar y usar. */
@Composable
internal fun ExplorePage(state: ConnectedState, model: ConnectedViewModel) {
    val context = LocalContext.current
    val ideas by produceState<List<IdeaPresentation>?>(null) {
        value = withContext(Dispatchers.IO) {
            runCatching { parseIdeas(context.assets.open(IdeasAsset).bufferedReader().use { it.readText() }) }
                .getOrDefault(emptyList())
        }
    }
    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf(IdeaCategory.ALL) }
    var discarded by rememberSaveable { mutableStateOf(listOf<String>()) }
    MakePage("Explorar", "No necesitas una gran idea. Solo un buen pacto.") {
        HeroTitle("Elige tu próximo desafío", "Menos hablar.\nMás demostrar.",
            "Ideas para poner a prueba tu habilidad y la palabra de tu squad.")
        AiIdeasCard(state.busy, onUse = { game, condition -> model.useIdea(game, condition) })
        MakeField("Busca un juego o un reto", query, { query = it.take(40) })
        Segmented(CategoryLabels.map { it.first }, CategoryLabels.first { it.second == category }.first) { label ->
            category = CategoryLabels.first { it.first == label }.second
        }
        val loaded = ideas
        if (loaded == null) {
            Kicker("Cargando ideas…")
        } else {
            val explore = ExploreState(loaded, query, category)
            val picks = explore.dailyIdeas(today(), discarded)
            SectionTitle("Inspírate y hazlo tuyo", if (query.isBlank()) "Pactos de hoy" else "Coincidencias")
            picks.forEach { idea ->
                IdeaCard(idea, state.busy, onUse = { model.useIdea(idea.game, idea.description) },
                    onDiscard = { discarded = discarded + idea.id })
            }
            if (picks.isEmpty()) {
                EmptyState("Ese pacto aún está por inventar.", "Prueba con otro juego o vuelve a ver todas las ideas.",
                    R.drawable.ic_pacto_search)
                MakeButton("Ver todas las ideas", ButtonKind.Secondary) {
                    query = ""; category = IdeaCategory.ALL; discarded = emptyList()
                }
            }
        }
        SafetyNote("Reglas editables: eliges rival, plazo y fianza al crear el pacto. Solo puntos virtuales.")
    }
}

/** PL-14: "Ideas con IA" con Gemini (Firebase AI Logic). Solo se manda el nombre del juego. */
@Composable
private fun AiIdeasCard(busy: Boolean, onUse: (String, String) -> Unit,
    ai: AiIdeasViewModel = viewModel()) {
    val assist by ai.state.collectAsStateWithLifecycle()
    MakeCard(accent = PactoViolet) {
        Kicker("Ideas con IA · Gemini", PactoCyan)
        MakeField("¿A qué juego?", assist.game, ai::game, hint = "Solo se envía el nombre del juego.")
        MakeButton(if (assist.loading) "Pensando ideas…" else "Generar 3 ideas", ButtonKind.Secondary,
            enabled = !busy && !assist.loading, icon = R.drawable.ic_pacto_plus) { ai.generate() }
        assist.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        assist.ideas.forEach { idea ->
            Text(idea.title, style = MaterialTheme.typography.titleSmall)
            Text(idea.condition, color = MaterialTheme.colorScheme.onSurfaceVariant)
            MakeButton("Usar esta idea", ButtonKind.Ghost, enabled = !busy) {
                onUse(assist.game.trim(), idea.condition)
            }
        }
        if (assist.ideas.isNotEmpty()) {
            Text("Generado por IA: revísalo antes de crear el reto.", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun IdeaCard(idea: IdeaPresentation, busy: Boolean, onUse: () -> Unit, onDiscard: () -> Unit) {
    MakeCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GameMark(idea.game, if (idea.category == IdeaCategory.COOPERATIVE) Tone.Cyan else Tone.Orange)
            Column(Modifier.weight(1f)) {
                Kicker(idea.game)
                Text(idea.title, style = MaterialTheme.typography.titleMedium)
            }
            Pill(CategoryLabels.first { it.second == idea.category }.first, Tone.Violet)
        }
        Text(idea.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
        MakeButton("Usar esta idea", enabled = !busy, icon = R.drawable.ic_pacto_plus, onClick = onUse)
        MakeButton("Descartar", ButtonKind.Ghost, icon = null, onClick = onDiscard)
    }
}

/** Número de día local (año y día del año): cambia a medianoche del teléfono. */
private fun today(): Long = Calendar.getInstance().let {
    it.get(Calendar.YEAR) * DaysPerYear + it.get(Calendar.DAY_OF_YEAR)
}
