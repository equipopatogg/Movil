package com.pactogg.app.feature.demo.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.InfoRow
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoOrange

/** ID 3: así vota el jurado. Dos votos ya están dados (uno y uno); el tuyo decide. */
@Composable
internal fun DemoJury() {
    var vote by rememberSaveable { mutableStateOf<String?>(null) }
    MakeCard {
        Kicker("Reto en votación")
        Text("Ganar un partido 1 contra 1", style = MaterialTheme.typography.titleMedium)
        InfoRow("Evidencia", "Captura del marcador final · ejemplo")
        InfoRow("Votos", if (vote == null) "2 de 3" else "3 de 3")
    }
    if (vote == null) {
        MakeButton("Fue legítimo", icon = R.drawable.ic_pacto_check) { vote = "legit" }
        MakeButton("Hizo trampa", ButtonKind.Secondary, icon = null) { vote = "cheat" }
    } else {
        MakeCard(accent = PactoCyan) {
            Pill("Resultado simulado", Tone.Cyan)
            Text(if (vote == "legit") "¡Reto cumplido!" else "El jurado decidió: hizo trampa.",
                style = MaterialTheme.typography.titleLarge)
            Text(if (vote == "legit") "La fianza regresa y el rival cumple lo pactado."
                else "Pierde la fianza virtual y baja su índice de palabra.",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            MakeButton("Votar otra vez", ButtonKind.Ghost, icon = null) { vote = null }
        }
    }
}

/** ID 4: así se arma un torneo. Cuatro squads de ejemplo; se toca al ganador de cada partida. */
@Composable
internal fun DemoTournament() {
    var first by rememberSaveable { mutableStateOf<String?>(null) }
    var second by rememberSaveable { mutableStateOf<String?>(null) }
    var champion by rememberSaveable { mutableStateOf<String?>(null) }
    Kicker("Semifinales", PactoCyan)
    DemoBracketMatch("Tu squad", "Los Clutch", first) { first = it; champion = null }
    DemoBracketMatch("Team Lag", "GG Izi", second) { second = it; champion = null }
    Kicker("Final", PactoCyan)
    val one = first
    val two = second
    if (one == null || two == null) {
        Text("Elige a los ganadores de las semifinales.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    } else {
        DemoBracketMatch(one, two, champion) { champion = it }
    }
    champion?.let {
        MakeCard(accent = PactoOrange) {
            Pill("Campeón · simulación", Tone.Orange)
            Text(it, style = MaterialTheme.typography.titleLarge)
            Text("En la app real cada resultado lo confirma el grupo rival con una foto.",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DemoBracketMatch(one: String, two: String, winner: String?, onWinner: (String) -> Unit) {
    MakeCard {
        listOf(one, two).forEach { team ->
            MakeButton(if (winner == team) "$team · gana" else team,
                if (winner == team) ButtonKind.Primary else ButtonKind.Ghost, icon = null) { onWinner(team) }
        }
    }
}
