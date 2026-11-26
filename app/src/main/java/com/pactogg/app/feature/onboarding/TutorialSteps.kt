package com.pactogg.app.feature.onboarding

import androidx.annotation.DrawableRes
import com.pactogg.app.R

/** Un paso del tutorial: qué botón es, dónde está y para qué sirve. */
data class TutorialStep(val title: String, val where: String, val body: String, @param:DrawableRes val icon: Int)

/** PL-15: recorrido de primer uso. El orden sigue lo que hace una persona nueva: grupo, reto, evidencia, jurado. */
object TutorialSteps {
    const val MaxBody = 240

    val all = listOf(
        TutorialStep("Bienvenido a PactoGG", "Toda la app",
            "Aquí retas a tus amigos en sus juegos, demuestras lo que hiciste y el grupo decide. " +
                "Solo se usan puntos virtuales: nunca dinero real.", R.drawable.ic_pacto_shield),
        TutorialStep("Grupos", "Barra de abajo · Grupos",
            "Crea tu squad o únete con el código de invitación que te pase un amigo. " +
                "Los retos y torneos siempre viven dentro de un grupo privado.", R.drawable.ic_pacto_users),
        TutorialStep("Crear un reto", "Barra de abajo · Crear (o \"Nuevo pacto\" en Inicio)",
            "Escribe el juego, qué hay que conseguir y la fecha límite. Luego elige a tu rival " +
                "o ábrelo a todo el grupo, pon la recompensa y la fianza en puntos virtuales.",
            R.drawable.ic_pacto_plus),
        TutorialStep("Por voz", "Inicio · Por voz",
            "Dicta el reto, por ejemplo \"le apuesto a Toño que le gano 3 partidas de Halo\". " +
                "La IA arma el borrador y tú lo revisas antes de crearlo.", R.drawable.ic_pacto_mic),
        TutorialStep("Aceptar y subir evidencia", "Inicio · Tablero de retos",
            "Tu rival acepta el reto desde el tablero. Al terminar, quien lo cumplió sube una captura " +
                "o un enlace como evidencia. Antes de guardarse, se revisa que sea apropiada.",
            R.drawable.ic_pacto_check),
        TutorialStep("Jurado", "Inicio · Jurado",
            "Los demás miembros del grupo votan si el reto se cumplió o fue trampa. " +
                "Se necesitan al menos 3 personas en el grupo para que haya jurado.", R.drawable.ic_pacto_vote),
        TutorialStep("Explorar e Ideas con IA", "Barra de abajo · Explorar",
            "Si no se te ocurre nada, toma una idea del día o escribe un juego en \"Ideas con IA\" " +
                "y la IA te propone 3 retos.", R.drawable.ic_pacto_bolt),
        TutorialStep("Torneos", "Barra de abajo · Torneos",
            "Organiza torneos entre grupos con su bracket. Al reportar un resultado con foto, " +
                "la app lee el marcador para ahorrarte escribirlo.", R.drawable.ic_pacto_trophy),
        TutorialStep("Avisos y ranking", "Campana de arriba · Inicio · Ranking",
            "La campana te avisa de retos, votos y resultados. En Ranking ves quién cumple su palabra, " +
                "la Fama y Los Tramposos.", R.drawable.ic_pacto_bell),
        TutorialStep("Tu perfil", "Tu avatar, arriba a la derecha",
            "Cambia tu apodo y avatar, revisa el aviso de privacidad, cierra sesión o elimina tu cuenta. " +
                "Este tutorial está en Perfil · Cómo usar PactoGG.", R.drawable.ic_pacto_user),
    )
}

/** Posición dentro del tutorial, sin Android, para probar la navegación. */
data class TutorialPager(val index: Int = 0, val total: Int = TutorialSteps.all.size) {
    val isFirst get() = index == 0
    val isLast get() = index == total - 1
    fun next() = copy(index = (index + 1).coerceAtMost(total - 1))
    fun back() = copy(index = (index - 1).coerceAtLeast(0))
}
