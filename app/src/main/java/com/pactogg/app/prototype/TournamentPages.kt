package com.pactogg.app.prototype

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.pactogg.app.R

@Composable
fun TournamentPage(route: String, nav: (String) -> Unit) {
    when (route) {
        "tournaments" -> TournamentsPage(nav)
        "new-tournament" -> NewTournamentPage(nav)
        "bracket" -> BracketPage(nav)
        "match" -> MatchPage()
    }
}

@Composable
private fun TournamentsPage(nav: (String) -> Unit) {
    var tab by remember { mutableStateOf("Todos") }
    AppPage("Torneos", "Compite con tu grupo") {
        Choices(listOf("Todos", "Mi grupo"), tab) { tab = it }
        TournamentFeature { nav("bracket") }
        if (tab == "Todos") PactCard("Noche táctica", "VALORANT · Próxima convocatoria", R.drawable.ic_pacto_clock)
        Action("Crear torneo") { nav("new-tournament") }
    }
}

@Composable
private fun NewTournamentPage(nav: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var game by remember { mutableStateOf("") }
    var format by remember { mutableStateOf("Eliminación directa") }
    var teams by remember { mutableStateOf("4 equipos") }
    AppPage("Nuevo torneo", "Configura un cuadro amistoso local") {
        HeroPanel("COMPITE CON LOS TUYOS", "Arma el siguiente encuentro.",
            "Define el cuadro antes de convocar a los grupos.", R.drawable.ic_pacto_trophy)
        Field("Nombre", name) { name = it }
        Field("Videojuego", game) { game = it }
        Choices(listOf("Eliminación directa", "Todos contra todos"), format) { format = it }
        Choices(listOf("4 equipos", "8 equipos"), teams) { teams = it }
        Text("Formato/tamaño de ejemplo; contrato funcional pendiente.")
        Action("Revisar cuadro de muestra") { if (name.isNotBlank() && game.isNotBlank()) nav("bracket") }
    }
}

@Composable
private fun BracketPage(nav: (String) -> Unit) {
    val rounds = listOf("Cuartos", "Semifinales", "Final")
    var round by remember { mutableStateOf("Cuartos") }
    val scroll = androidx.compose.foundation.rememberScrollState()
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val widthPx = with(androidx.compose.ui.platform.LocalDensity.current) { 236.dp.roundToPx() }
    AppPage("Copa del finde", "Rocket League · Eliminación directa · cuadro de ejemplo") {
        Choices(rounds, round) {
            round = it
            scope.launch { scroll.animateScrollTo(rounds.indexOf(it) * widthPx) }
        }
        Text("Desliza para explorar el cuadro · Toca una partida", style = MaterialTheme.typography.bodySmall)
        Row(Modifier.horizontalScroll(scroll), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            rounds.forEachIndexed { index, title ->
                Column(Modifier.width(220.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(title.uppercase(), style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary)
                    repeat(listOf(4, 2, 1)[index]) { number ->
                        BracketMatch(number + 1) { nav("match") }
                    }
                }
            }
        }
        Text("Sin marcadores precargados. No se avanza una ronda real.")
    }
}

@Composable
private fun MatchPage() {
    var score by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }
    AppPage("Detalle de partida", "Solo reporte local de muestra") {
        PactCard("Equipos por confirmar", "No se avanza ninguna ronda real.", R.drawable.ic_pacto_users)
        Field("Reporte de ejemplo", score) { score = it }
        Action("Revisar reporte local") { submitted = score.isNotBlank() }
        if (submitted) Text("Reporte: $score. Pendiente de confirmación de ejemplo; no enviado.")
    }
}
