package com.pactogg.app.feature.play.presentation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.EmptyState
import com.pactogg.app.core.designsystem.InfoRow
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.SafetyNote
import com.pactogg.app.feature.connected.presentation.ConnectedRoute
import com.pactogg.app.feature.connected.presentation.ConnectedState
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.back
import com.pactogg.app.feature.connected.presentation.chooseGroupThen
import com.pactogg.app.feature.play.domain.VoiceDraft
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoOrange

/** Reto por voz (maqueta `voz`, spec PL-05): dictar o escribir, convertir con la IA y pasar al formulario. */
@Composable
internal fun VoiceScreen(state: ConnectedState, model: ConnectedViewModel) {
    val play = state.play
    MakePage("Crear por voz", "Dilo como lo dirías a tu squad", onBack = { model.back() }) {
        val group = state.group
        if (group == null) {
            EmptyState("Necesitas un grupo", "El reto se crea dentro de un grupo.", R.drawable.ic_pacto_users)
            MakeButton("Ir a grupos", enabled = !state.busy) { model.chooseGroupThen(ConnectedRoute.VOICE) }
            return@MakePage
        }
        Kicker("Grupo privado · ${group.name}", PactoCyan)
        PhraseCard(state, model)
        if (play.voiceUnavailable) {
            MakeCard(accent = PactoOrange) {
                Text("La IA no está disponible; escríbelo a mano.", style = MaterialTheme.typography.titleSmall)
                Text("Tu frase se copia al formulario para que no la pierdas.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                MakeButton("Escribir a mano", enabled = !state.busy) { model.manualFromPhrase() }
            }
        }
        play.voiceDraft?.let { DraftCard(it, state.busy, model) }
        SafetyNote("No se graba ni se envía audio: solo el texto de tu frase.", R.drawable.ic_pacto_mic)
    }
}

@Composable
private fun PhraseCard(state: ConnectedState, model: ConnectedViewModel) {
    var noRecognizer by rememberSaveable { mutableStateOf(false) }
    val dictation = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val heard = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
        if (result.resultCode == Activity.RESULT_OK && !heard.isNullOrBlank()) model.phrase(heard)
    }
    MakeCard {
        MakeButton("Dictar mi reto", ButtonKind.Secondary, enabled = !state.busy, icon = R.drawable.ic_pacto_mic) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-MX")
                .putExtra(RecognizerIntent.EXTRA_PROMPT, "Di tu reto")
            try { dictation.launch(intent) } catch (_: ActivityNotFoundException) { noRecognizer = true }
        }
        if (noRecognizer) Text("Este teléfono no tiene dictado. Escribe la frase aquí abajo.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        MakeField("Tu frase", state.play.phrase, { model.phrase(it) }, multiline = true,
            hint = "Ej.: Reto a Dani a ganar 3 partidas de Halo; quien pierda invita las pizzas.")
        MakeButton("Convertir en reto", enabled = !state.busy && state.play.phrase.isNotBlank()) {
            model.parsePhrase()
        }
    }
}

@Composable
private fun DraftCard(draft: VoiceDraft, busy: Boolean, model: ConnectedViewModel) {
    MakeCard(accent = PactoCyan) {
        Kicker("Esto entendimos", PactoCyan)
        InfoRow("Juego", draft.game.ifBlank { "—" })
        InfoRow("Reto", draft.condition.ifBlank { "—" })
        InfoRow("Recompensa", draft.reward.ifBlank { "—" })
        InfoRow("Rival", draft.targetNickname ?: "Sin rival: reto abierto al grupo")
        if (draft.missingFields.isNotEmpty()) {
            Text("Completa: ${draft.missingFields.joinToString { FieldNames[it] ?: it }}.",
                style = MaterialTheme.typography.bodySmall, color = PactoOrange)
        }
        if (draft.rivalUnmatched && draft.members.isNotEmpty()) {
            Text("No reconocimos al rival. Elige a quién retas:", style = MaterialTheme.typography.titleSmall)
            draft.members.forEach { member ->
                MakeButton("Retar a ${member.nickname}", ButtonKind.Secondary, enabled = !busy) {
                    model.useVoiceDraft(member.id, member.nickname)
                }
            }
            MakeButton("Seguir sin rival", ButtonKind.Ghost, enabled = !busy) { model.useVoiceDraft() }
        } else {
            MakeButton("Revisar y lanzar", enabled = !busy) { model.useVoiceDraft() }
        }
    }
}
