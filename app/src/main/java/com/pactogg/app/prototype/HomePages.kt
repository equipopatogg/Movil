package com.pactogg.app.prototype

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.pactogg.app.R

@Composable
fun HomePage(nav: (String) -> Unit) {
    HomeDesign(nav)
}

@Composable
fun NotificationsPage(nav: (String) -> Unit) {
    AppPage("Notificaciones", "Novedades de tus pactos · ejemplos locales") {
        HeroPanel("RETO DE OTRA PERSONA", "Tu criterio cuenta.",
            "Te asignaron al jurado de Lucía.", R.drawable.ic_pacto_vote)
        Action("Revisar reto ajeno") { nav("jury") }
        LinkRow("Nueva invitación de Martín", "Información y condiciones del pacto", R.drawable.ic_pacto_bell) {
            nav("contract")
        }
        LinkRow("Tu jurado ya puede votar", "Sin resultados ni recompensas garantizados", R.drawable.ic_pacto_vote) {
            nav("jury")
        }
        LinkRow("Inscripción de torneo abierta", "Compite con tu grupo", R.drawable.ic_pacto_trophy) {
            nav("tournaments")
        }
    }
}

@Composable
fun VoicePage(nav: (String) -> Unit) {
    var phrase by remember { mutableStateOf("") }
    AppPage("Contrato por voz con IA", "Sin grabación ni IA conectada en esta versión local.") {
        HeroPanel("CREAR RETO → VOZ → REVISAR", "De tu voz a un pacto.",
            "Rival, juego, condición y recompensa.", R.drawable.ic_pacto_mic)
        PactCard("Cuatro campos, sin letra pequeña", "Siempre podrás revisar el borrador.",
            R.drawable.ic_pacto_mic) {
            Action("Cargar frase de ejemplo") { phrase = "Completar juntos una sesión de entrenamiento" }
        }
        if (phrase.isNotEmpty()) {
            Field("Lo que entendió · editable", phrase) { phrase = it }
            Action("Continuar creando manualmente") { nav("create") }
        }
        TextButton(onClick = { nav("create") }) { Text("Escribir manualmente") }
    }
}

@Composable
fun RankingPage(nav: (String) -> Unit = {}) {
    var tab by remember { mutableStateOf("Ranking") }
    AppPage("Palabra y ranking", "Solo entre amigos del mismo grupo") {
        HeroPanel("ÍNDICE DE PALABRA", "¿Quién cumple mejor?",
            "Solo cuentan los acuerdos resueltos.", R.drawable.ic_pacto_trophy)
        GroupSwitch { nav("groups") }
        Choices(listOf("Ranking", "Libro", "Fama", "Integridad"), tab) { tab = it }
        val copy = when (tab) {
            "Libro" -> "Tu palabra está al día. Los compromisos aparecerán al resolver los contratos."
            "Fama" -> "El muro espera buenas historias respaldadas por contratos cerrados."
            "Integridad" -> "Sin casos confirmados. Una evidencia rechazada no equivale a trampa."
            else -> "El grupo necesita contratos resueltos. Todavía no hay posiciones."
        }
        if (tab == "Ranking") EmptyPodium()
        PactCard(tab, copy, R.drawable.ic_pacto_trophy)
        Text("La fórmula y los resultados reales se definirán con el backend.")
    }
}
