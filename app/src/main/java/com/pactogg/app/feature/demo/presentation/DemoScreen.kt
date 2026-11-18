package com.pactogg.app.feature.demo.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.GameMark
import com.pactogg.app.core.designsystem.HeroTitle
import com.pactogg.app.core.designsystem.InfoRow
import com.pactogg.app.core.designsystem.Initial
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.PageLayout
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.SafetyNote
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.ui.theme.PactoBackground
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoOrange
import kotlinx.coroutines.delay

private const val TickMillis = 38L
private const val PulseMillis = 700
private val StepNames = listOf("Bienvenida", "Descubre", "Arma tu reto", "Comprueba", "Evidencia", "Jurado",
    "Torneos")
private val Titles = listOf("Prueba PactoGG en 2 minutos", "Una partida. Un pacto.", "Dilo y la IA arma tu reto",
    "Todo claro antes de jugar", "Tu evidencia, en buenas manos", "Tus amigos deciden", "Reta a otros squads")
private val Captions = listOf(
    "Sin registro. Sin dinero real. Descubre una nueva forma de cumplir tu palabra.",
    "Transforma una partida entre amigos en un acuerdo con reglas claras.",
    "Mira cómo una frase se convierte en un reto. Aquí es una animación: nadie habla ni se envía nada.",
    "Comprueba las condiciones del acuerdo antes de lanzarlo.",
    "Subes tu prueba y un jurado de amigos decide. No hay ganador automático.",
    "Tres amigos del grupo ven la evidencia y votan. Prueba a votar tú.",
    "Tu grupo contra otros en eliminación directa. Toca a quien gana para que avance.")

/** Demo guiada antes de iniciar sesión (maqueta `demo`, spec PL-07). Todo es local y de ejemplo. */
@Composable
fun DemoScreen(onExit: () -> Unit, onRegister: () -> Unit) {
    var step by rememberSaveable { mutableIntStateOf(0) }
    BackHandler { if (step > 0) step-- else onExit() }
    PageLayout {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("PACTOGG", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Pill(if (step == 0) "Bienvenido" else "Modo demo", Tone.Violet)
            TextButton(onClick = onExit) { Text("Salir") }
        }
        if (step > 0) Kicker("Paso $step de $DemoLastStep · ${StepNames[step]}", PactoCyan)
        HeroTitle(if (step == 0) "Demo interactiva" else "Simulación · datos de ejemplo", Titles[step], Captions[step])
        when (step) {
            0 -> DemoWelcome()
            1 -> DemoAgreement()
            2 -> DemoVoice()
            3 -> DemoReview()
            4 -> DemoEvidence()
            5 -> DemoJury()
            else -> DemoTournament()
        }
        DemoFooter(step, onNext = { if (step < DemoLastStep) step++ else onRegister() },
            onBack = { if (step > 0) step-- else onExit() })
        SafetyNote("Sin registro previo. Sin dinero real. Nada de esta demo se guarda ni se envía.")
    }
}

@Composable
private fun DemoFooter(step: Int, onNext: () -> Unit, onBack: () -> Unit) {
    MakeButton(when (step) {
        0 -> "Comenzar demo"
        DemoLastStep -> "Crear mi cuenta"
        else -> "Continuar"
    }, onClick = onNext)
    MakeButton(if (step > 0) "Atrás" else "Volver a acceso", ButtonKind.Ghost, icon = null, onClick = onBack)
}

@Composable
private fun DemoWelcome() {
    listOf(Triple(R.drawable.ic_pacto_bolt, "Arma tu reto", "Elige juego, rival y condiciones."),
        Triple(R.drawable.ic_pacto_plus, "Sube evidencia", "Una captura o un enlace a tu clip."),
        Triple(R.drawable.ic_pacto_shield, "Revisión transparente", "Evidencia y jurado, sin sorpresas."),
    ).forEachIndexed { index, (icon, title, copy) ->
        MakeCard {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(painterResource(icon), null, tint = PactoCyan, modifier = Modifier.size(24.dp))
                Column(Modifier.weight(1f)) {
                    Kicker("0${index + 1}")
                    Text(title, style = MaterialTheme.typography.titleSmall)
                    Text(copy, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun DemoMatch() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Initial("Alex", Tone.Violet, 40); Text("@Tú (Alex)"); Kicker("Retador")
        }
        Text("VS", style = MaterialTheme.typography.titleLarge, color = PactoOrange)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Initial("Vortex", Tone.Cyan, 40); Text("@Vortex_99"); Kicker("Rival · ejemplo")
        }
    }
}

@Composable
private fun DemoAgreement() {
    MakeCard(accent = PactoOrange) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GameMark("FC", Tone.Orange)
            Column(Modifier.weight(1f)) {
                Kicker("Ficha del acuerdo")
                Text("EA Sports FC 26", style = MaterialTheme.typography.titleLarge)
                Text("1 contra 1 · amistoso", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        DemoMatch()
        InfoRow("Pacto propuesto", "Quien pierda invita la pizza")
    }
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Pill("Reglas claras", Tone.Cyan); Pill("Evidencia", Tone.Cyan); Pill("Jurado de amigos", Tone.Cyan)
    }
}

/** El reto por voz de la demo: la frase y los campos se escriben solos; no usa micrófono ni red. */
@Composable
private fun DemoVoice() {
    var tick by rememberSaveable { mutableIntStateOf(0) }
    var run by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(run) {
        while (tick < DemoTicks) { delay(TickMillis); tick++ }
    }
    val frame = demoFrame(tick)
    val pulse by rememberInfiniteTransition(label = "mic").animateFloat(1f, 0.35f,
        infiniteRepeatable(tween(PulseMillis), RepeatMode.Reverse), label = "mic-alpha")
    MakeCard(accent = PactoCyan) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(44.dp).alpha(if (frame.listening) pulse else 1f)
                .background(PactoCyan, RoundedCornerShape(22.dp)), contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.ic_pacto_mic), null, tint = PactoBackground,
                    modifier = Modifier.size(24.dp))
            }
            Column(Modifier.weight(1f)) {
                Kicker(if (frame.listening) "Escuchando · simulación" else "Frase lista", PactoCyan)
                Text(frame.phrase + if (frame.listening) "▌" else "", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
    MakeCard {
        Kicker(if (frame.done) "Reto armado" else "La IA llena los campos")
        DemoFields.forEachIndexed { index, (label, _) ->
            InfoRow(label, frame.fields[index].ifEmpty { "—" })
        }
    }
    if (!frame.done) MakeButton("Saltar animación", ButtonKind.Ghost, icon = null) { tick = DemoTicks }
    else MakeButton("Repetir animación", ButtonKind.Secondary, icon = null) { tick = 0; run++ }
}

@Composable
private fun DemoReview() {
    MakeCard {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Kicker("EA Sports FC 26 · 1 contra 1"); Pill("Vista previa", Tone.Cyan)
        }
        DemoMatch()
    }
    MakeCard {
        InfoRow("Estado", "Por lanzar")
        InfoRow("Plazo propuesto", "Hoy, 45 minutos")
        InfoRow("Fianza virtual", "100 pts cada quien")
        InfoRow("Pacto de honor", "Quien pierda invita la pizza")
    }
    SafetyNote("PactoGG no maneja dinero real. Los puntos no se compran ni se canjean; el pacto es de honor.")
}

@Composable
private fun DemoEvidence() {
    var sent by rememberSaveable { mutableStateOf(false) }
    MakeCard(accent = if (sent) PactoCyan else null) {
        Pill(if (sent) "Pendiente de revisión" else "Revisión simulada", Tone.Cyan)
        Text(if (sent) "Evidencia recibida" else "Añade tu evidencia", style = MaterialTheme.typography.titleLarge)
        Text(if (sent) "El jurado podrá revisar el material. No hay un resultado definido."
            else "Este ejercicio no sube archivos ni decide un ganador.",
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        MakeButton(if (sent) "Restablecer simulación" else "Simular envío de evidencia", ButtonKind.Secondary,
            icon = if (sent) R.drawable.ic_pacto_check else R.drawable.ic_pacto_plus) { sent = !sent }
    }
    Text("Así de simple: acuerda, juega y demuestra.", style = MaterialTheme.typography.titleMedium)
}
