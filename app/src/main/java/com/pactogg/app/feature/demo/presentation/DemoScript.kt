package com.pactogg.app.feature.demo.presentation

const val DemoLastStep = 6
const val DemoPhrase = "Reto a Vortex a ganarme un 1 contra 1 en EA Sports FC 26. Quien pierda invita la pizza."
private const val DemoPause = 10

/** Campos que la demo llena sola, en orden: etiqueta y valor de ejemplo. */
val DemoFields = listOf("Videojuego" to "EA Sports FC 26", "Reto" to "Ganar un partido 1 contra 1",
    "Rival" to "@Vortex_99 · ejemplo", "Recompensa" to "Quien pierda invita la pizza")

/** Ticks que dura la animación completa: frase, pausa y cada campo letra por letra. */
val DemoTicks = DemoPhrase.length + DemoPause + DemoFields.sumOf { it.second.length }

/** Lo que se ve en un instante de la animación: la frase dictada y cada campo, escritos hasta donde va. */
data class DemoFrame(val phrase: String, val fields: List<String>, val done: Boolean) {
    val listening: Boolean get() = phrase.length < DemoPhrase.length
}

/**
 * Animación del reto por voz de la demo: nadie habla ni se envía nada.
 * Primero se escribe la frase, luego una pausa y después cada campo en orden.
 */
fun demoFrame(tick: Int): DemoFrame {
    var left = tick.coerceIn(0, DemoTicks)
    val phrase = DemoPhrase.take(left)
    left = (left - DemoPhrase.length - DemoPause).coerceAtLeast(0)
    val fields = DemoFields.map { (_, value) ->
        value.take(left).also { left = (left - value.length).coerceAtLeast(0) }
    }
    return DemoFrame(phrase, fields, tick >= DemoTicks)
}
