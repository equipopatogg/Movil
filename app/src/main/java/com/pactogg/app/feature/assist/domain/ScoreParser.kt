package com.pactogg.app.feature.assist.domain

/**
 * PL-14 (ML): convierte el texto que ML Kit lee de la captura del resultado en un marcador.
 * Solo acepta dos números de 0 a 999 separados por "-", ":", "x" o "a" (por ejemplo "3 - 1", "2:0", "10 x 8").
 * Si hay varios marcadores distintos devuelve null: la persona escribe el resultado a mano.
 */
object ScoreParser {
    private const val MaxScore = 999
    private val Score = Regex("(?<![\\d.])(\\d{1,3})\\s*(?:-|–|—|:|x|X|a)\\s*(\\d{1,3})(?![\\d.])")

    fun parse(text: String): Pair<Int, Int>? {
        val found = Score.findAll(text.take(MaxText))
            .mapNotNull { match ->
                val first = match.groupValues[1].toInt()
                val second = match.groupValues[2].toInt()
                (first to second).takeIf { first <= MaxScore && second <= MaxScore && !looksLikeTime(match.value) }
            }
            .distinct()
            .toList()
        return found.singleOrNull()
    }

    /** "18:30" o "09:05" son horas, no marcadores. */
    private fun looksLikeTime(value: String): Boolean {
        val parts = value.split(':').map(String::trim)
        return parts.size == 2 && parts[1].length == 2 && parts[0].length == 2
    }

    private const val MaxText = 4000
}
