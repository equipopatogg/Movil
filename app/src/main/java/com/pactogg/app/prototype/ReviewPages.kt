package com.pactogg.app.prototype

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.pactogg.app.R

@Composable
fun ReviewPage(route: String, nav: (String) -> Unit) {
    when (route) {
        "contract" -> ContractPage(nav)
        "evidence" -> EvidencePage(nav)
        "jury" -> JuryPage()
    }
}

@Composable
private fun ContractPage(nav: (String) -> Unit) {
    var accepted by remember { mutableStateOf(true) }
    AppPage("Detalle del contrato", "Datos de ejemplo · Sin operación remota") {
        PactCard("Gana 3 partidas seguidas", "Rocket League · Con Martín", R.drawable.ic_pacto_bolt) {
            Text(if (accepted) "EN CURSO · aceptación local" else "INVITACIÓN DE EJEMPLO")
            if (!accepted) Action("Aceptar para recorrer el ejemplo") { accepted = true }
            else Action("Añadir evidencia") { nav("evidence") }
        }
        Text("Las reglas y puntos reales dependerán del contrato acordado.")
    }
}

@Composable
private fun EvidencePage(nav: (String) -> Unit) {
    var tab by remember { mutableStateOf("Enlace") }
    var url by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }
    AppPage("Subir evidencia", "Lo que ocurrió, respaldado por pruebas") {
        HeroPanel("RESPALDA TU PACTO", "Lo que ocurrió, demostrado.",
            "Fotos o enlaces, sin datos personales innecesarios.", R.drawable.ic_pacto_shield)
        Choices(listOf("Enlace", "Captura"), tab) { tab = it }
        if (!sent) {
            if (tab == "Enlace") Field("URL HTTPS de ejemplo", url) { url = it }
            else
                Text("El selector de fotos se incorporará con moderación. Sin carga de imágenes todavía.")
            PactCard("Arbitraje asistido", "La IA no está conectada y no decide resultados.",
                R.drawable.ic_pacto_shield)
            if (tab == "Enlace") Action("Simular recepción local") { if (url.startsWith("https://")) sent = true }
        } else {
            PactCard("Evidencia recibida localmente", "Sin subida ni análisis. Pendiente de revisión del ejemplo.")
            Action("Volver al contrato") { nav("contract") }
        }
    }
}

@Composable
private fun JuryPage() {
    var reviewed by remember { mutableStateOf(false) }
    var vote by remember { mutableStateOf("") }
    var confirmed by remember { mutableStateOf(false) }
    AppPage("Jurado · Retos de otros", "Tu tarea: revisar, no participar") {
        PactCard("Una sesión de práctica en equipo", "Lucía · Valorant · Material de ejemplo",
            R.drawable.ic_pacto_vote)
        Image(painterResource(R.drawable.jury_practice_illustration),
            contentDescription = "Material ilustrativo de práctica; no acredita un resultado real",
            modifier = Modifier.fillMaxWidth().aspectRatio(640f / 380f))
        Text("No hay evidencia real para emitir un dictamen. Puedes recorrer los controles de muestra.")
        TextButton(onClick = { reviewed = !reviewed }) {
            Text(if (reviewed) "Material de muestra revisado ✓" else "He revisado el material de muestra")
        }
        if (reviewed && !confirmed) {
            Choices(listOf("Cumple", "No cumple", "Insuficiente"), vote) { vote = it }
            if (vote.isNotBlank()) Action("Confirmar elección local") { confirmed = true }
        }
        if (confirmed) Text("Tu elección local: $vote. Sin consenso ni resultado real.")
    }
}
