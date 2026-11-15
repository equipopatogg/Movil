package com.pactogg.app.feature.explore.presentation

import kotlin.random.Random
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Una fila de `assets/explorar/retos.json`. */
@Serializable
private data class IdeaRow(val juego: String, val titulo: String, val reto: String, val categoria: String)

private val IdeasJson = Json { ignoreUnknownKeys = true }
private val Categories = mapOf("Habilidad" to IdeaCategory.SKILL, "Cooperativo" to IdeaCategory.COOPERATIVE,
    "Constancia" to IdeaCategory.CONSISTENCY)

const val DailyIdeaCount = 4
const val MaxIdeaGame = 60
const val MaxIdeaRule = 280

/** Convierte el catálogo local en ideas. Descarta filas vacías, demasiado largas o de categoría desconocida. */
fun parseIdeas(text: String): List<IdeaPresentation> =
    IdeasJson.decodeFromString<List<IdeaRow>>(text).mapIndexedNotNull { index, row ->
        val category = Categories[row.categoria.trim()]
        val game = row.juego.trim()
        val rule = row.reto.trim()
        val fits = game.length in 1..MaxIdeaGame && rule.length in 1..MaxIdeaRule
        if (category == null || !fits) null
        else IdeaPresentation("idea-$index", game, row.titulo.trim().ifEmpty { game }, rule, category)
    }

/**
 * Ideas del día: el mismo día salen las mismas [count] para todos; al descartar una entra la siguiente.
 * Con búsqueda o categoría activas se aplica sobre las ideas que coinciden.
 */
fun ExploreState.dailyIdeas(day: Long, discarded: Collection<String>, count: Int = DailyIdeaCount):
    List<IdeaPresentation> = visibleIdeas().shuffled(Random(day)).filterNot { it.id in discarded }.take(count)
