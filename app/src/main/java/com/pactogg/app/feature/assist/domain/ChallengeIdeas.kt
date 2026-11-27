package com.pactogg.app.feature.assist.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Idea de reto generada por IA. Se copia al borrador; la persona la edita y el backend la modera al crear. */
data class AiIdea(val title: String, val condition: String)

/** Generador de ideas con IA generativa (PL-14). Lanza [AssistUnavailable] si el servicio no está listo. */
interface ChallengeIdeaGenerator {
    suspend fun ideas(game: String): List<AiIdea>
}

class AssistUnavailable(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Prompt y lectura de la respuesta del modelo, separados del SDK para probarlos sin red.
 * Al modelo solo se manda el nombre del juego (sin apodos, correos, grupo ni ubicación).
 */
object IdeaPrompt {
    const val MaxGame = 40
    const val MaxTitle = 60
    const val MaxCondition = 280
    const val MaxIdeas = 3
    private val AllowedGame = Regex("[\\p{L}\\p{N} :'&.+-]+")
    private val Money = Regex("(?i)(\\b(?:mxn|usd|eur(?:os?)?|pesos?|d[oó]lar(?:es)?|dinero|apuesta)\\b|[$€£¥])")
    private val Fence = Regex("^```(?:json)?\\s*|\\s*```$")
    private val Lenient = Json { ignoreUnknownKeys = true; isLenient = true }

    /** Nombre de juego limpio o null si trae caracteres que podrían usarse para cambiar las instrucciones. */
    fun cleanGame(raw: String): String? {
        val game = raw.trim().replace(Regex(" {2,}"), " ")
        return game.takeIf { it.length in 1..MaxGame && AllowedGame.matches(it) }
    }

    fun build(game: String): String = """
        Eres el asistente de PactoGG, una app de retos entre amigos gamers. Responde solo en español de México.
        Propón exactamente $MaxIdeas retos distintos para el videojuego "$game".
        Reglas: cada reto debe ser posible, medible y fácil de comprobar con una captura o un video;
        sin dinero, apuestas ni premios reales; sin contenido ofensivo, sexual, violento fuera del juego ni peligroso.
        Devuelve solo un arreglo JSON, sin texto extra, con esta forma:
        [{"titulo":"máximo $MaxTitle caracteres","reto":"qué hay que conseguir, máximo $MaxCondition caracteres"}]
    """.trimIndent()

    fun parse(text: String): List<AiIdea> {
        val body = text.trim().replace(Fence, "")
        val rows = runCatching { Lenient.decodeFromString<List<Row>>(body) }.getOrElse { return emptyList() }
        return rows.asSequence()
            .map { AiIdea(it.titulo.trim().take(MaxTitle), it.reto.trim()) }
            .filter { it.title.isNotEmpty() && it.condition.length in 1..MaxCondition }
            .filterNot { Money.containsMatchIn(it.title) || Money.containsMatchIn(it.condition) }
            .distinctBy { it.condition.lowercase() }
            .take(MaxIdeas)
            .toList()
    }

    @Serializable
    private data class Row(val titulo: String = "", val reto: String = "")
}
