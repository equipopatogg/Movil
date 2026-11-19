package com.pactogg.app.feature.play.presentation

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.Segmented
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.play.domain.EvidenceReportReasons
import kotlinx.coroutines.flow.update

/** Reportar una evidencia (foto o enlace) desde la revisión del jurado; la revisa el administrador del grupo. */
fun ConnectedViewModel.reportEvidence(evidenceId: String, reason: String) {
    val group = state.value.play.voteContract?.groupId ?: state.value.group?.id ?: return
    perform {
        play.reportEvidence(group, evidenceId, reason)
        mutable.update { it.copy(notice = "Reporte enviado. El administrador del grupo revisará la evidencia.") }
    }
}

/** Botón "Reportar evidencia" con motivo, debajo de cada evidencia que revisa el jurado. */
@Composable
fun EvidenceReport(evidenceId: String, busy: Boolean, model: ConnectedViewModel) {
    var open by rememberSaveable(evidenceId) { mutableStateOf(false) }
    var reason by rememberSaveable(evidenceId) { mutableStateOf(EvidenceReportReasons.first().first) }
    TextButton(onClick = { open = !open }, enabled = !busy) {
        Text(if (open) "Cancelar reporte" else "Reportar evidencia")
    }
    if (open) {
        Segmented(EvidenceReportReasons.map { it.first }, reason) { reason = it }
        MakeButton("Enviar reporte", ButtonKind.Secondary, enabled = !busy, icon = null) {
            open = false
            model.reportEvidence(evidenceId, EvidenceReportReasons.first { it.first == reason }.second)
        }
    }
}
