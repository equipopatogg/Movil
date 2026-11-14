package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.EmptyState
import com.pactogg.app.core.designsystem.GameMark
import com.pactogg.app.core.designsystem.InfoRow
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.SafetyNote
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.core.designsystem.localDate
import com.pactogg.app.core.designsystem.statusLabel
import com.pactogg.app.core.designsystem.statusTone
import com.pactogg.app.prototype.Choices
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoViolet

/** Paso numerado de la maqueta (`form-intro`: 01, 02, 03). */
@Composable
internal fun StepIntro(number: String, title: String, copy: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(Modifier.size(35.dp).background(PactoViolet, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center) {
            Text(number, style = MaterialTheme.typography.titleSmall)
        }
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(copy, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Crear reto manual con los tres pasos de la maqueta (`Create`). La lógica es la misma de antes. */
@Composable
fun ConnectedCreate(state: ConnectedState, model: ConnectedViewModel) {
    MakePage("Nuevo pacto", "Define, elige rival y revisa") {
        val group = state.group
        if (group == null) {
            EmptyState("Necesitas un grupo", "Selecciona o crea un grupo para usar su configuración.",
                R.drawable.ic_pacto_users)
            MakeButton("Ir a grupos", enabled = !state.busy) { model.navigate(ConnectedRoute.GROUPS) }
            return@MakePage
        }
        val draft = state.draft
        val rival = state.social.selectedRival
        MakeCard {
            Kicker("Grupo privado", PactoCyan)
            Text(group.name, style = MaterialTheme.typography.titleMedium)
        }
        MakeCard {
            StepIntro("01", "Define el reto", "Que sea claro, posible y fácil de comprobar.")
            MakeField("Videojuego", draft.game, { model.draft(draft.copy(game = it)) })
            MakeField("¿Qué hay que conseguir?", draft.condition, { model.draft(draft.copy(condition = it)) },
                hint = "Sé específico para evitar dudas del jurado.", multiline = true)
            MakeField("Fecha límite (AAAA-MM-DD HH:mm)", state.deadlineLocal, model::deadline,
                hint = "Hora de tu teléfono; se envía en UTC.")
        }
        MakeCard {
            StepIntro("02", "Elige a tu rival", "La invitación queda pendiente hasta que acepte.")
            InfoRow("Rival", if (draft.targetId == null) "Todo el grupo" else rival?.nickname.orEmpty())
            MakeButton("Elegir rival del grupo", ButtonKind.Ghost, enabled = !state.busy) {
                model.navigate(ConnectedRoute.MEMBERS)
            }
            if (draft.targetId != null) MakeButton("Retar a todo el grupo", ButtonKind.Ghost, enabled = !state.busy) {
                model.targetMember(null)
            }
            MakeField("Recompensa o pacto de honor", draft.reward, { model.draft(draft.copy(reward = it)) },
                hint = "Sin dinero: un acuerdo entre amigos.")
            Kicker("Fianza en puntos virtuales")
            Choices(group.bonds.map(Int::toString), draft.bond?.toString().orEmpty()) {
                model.draft(draft.copy(bond = it.toInt()))
            }
        }
        if (state.reviewing) {
            MakeCard(accent = PactoViolet) {
                StepIntro("03", "Revisa el pacto", "Podrás subir evidencia cuando tu rival acepte.")
                Pill("Invitación", Tone.Orange)
                Text(draft.condition, style = MaterialTheme.typography.titleLarge)
                InfoRow("Juego", draft.game)
                InfoRow("Rival", if (draft.targetId == null) "Grupo" else rival?.nickname.orEmpty())
                InfoRow("Acuerdo", draft.reward)
                InfoRow("Fianza", "${draft.bond} pts virtuales")
                InfoRow("Fecha límite", localDate(draft.deadline))
                MakeButton("Confirmar y crear reto", enabled = !state.busy) { model.createChallenge() }
            }
        } else MakeButton("Revisar reto", enabled = !state.busy) { model.review() }
        SafetyNote("No se mueve dinero real. La fianza usa puntos virtuales dentro de PactoGG.")
    }
}

/** Detalle del reto con el estilo de la maqueta (`Challenge`). */
@Composable
fun ConnectedContract(state: ConnectedState, model: ConnectedViewModel) {
    MakePage("Contrato", "Lo que se acordó, tal como lo tiene el servidor", onBack = { model.back() }) {
        val contract = state.contract
        if (contract == null) {
            EmptyState("No hay reto abierto.", "Ábrelo desde el tablero de tu grupo.", R.drawable.ic_pacto_shield)
            return@MakePage
        }
        MakeCard {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Pill(if (state.contractStateStale) "Por actualizar" else statusLabel(contract.status),
                    if (state.contractStateStale) Tone.Muted else statusTone(contract.status))
                Pill("${contract.bond} pts", Tone.Violet)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GameMark(contract.game)
                Column(Modifier.weight(1f)) {
                    Kicker(contract.game)
                    Text(contract.condition, style = MaterialTheme.typography.titleLarge)
                }
            }
            InfoRow("Recompensa", contract.reward)
            InfoRow("Fecha límite", localDate(contract.deadline))
            InfoRow("Fianza", "${contract.bond} puntos virtuales")
            contract.evidenceCount?.let { InfoRow("Evidencias", it.toString()) }
            contract.closedReason?.let { InfoRow("Motivo de cierre", it) }
            ConnectedContractMetadata(contract)
        }
        ConnectedContractActions(state, model, contract)
        MakeButton("Ver evidencias", enabled = !state.busy) { model.navigate(ConnectedRoute.EVIDENCE) }
        MakeButton("Actualizar reto", ButtonKind.Ghost, enabled = !state.busy) { model.openChallenge(contract) }
        SafetyNote("Los puntos son virtuales. Con la primera evidencia el reto pasa a votación del jurado.")
    }
}

