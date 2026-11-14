package com.pactogg.app.feature.play.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.EmptyState
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.Pill
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.core.designsystem.localDate
import com.pactogg.app.feature.connected.presentation.ConnectedState
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.back
import com.pactogg.app.feature.play.domain.Notice
import com.pactogg.app.ui.theme.PactoCyan

/** Avisos de la cuenta (maqueta `notificaciones`, spec PL-02). */
@Composable
internal fun NoticesScreen(state: ConnectedState, model: ConnectedViewModel) {
    val play = state.play
    MakePage("Notificaciones", "Avisos de tus pactos", onBack = { model.back() }) {
        if (play.unread > 0) MakeButton("Marcar todo como leído (${play.unread})", ButtonKind.Ghost,
            enabled = !state.busy) { model.markAllNotices() }
        if (play.noticesLoaded && play.notices.isEmpty()) {
            EmptyState("Sin avisos por ahora.",
                "Aquí te avisamos cuando te reten, te toque votar o se resuelva un pacto.",
                R.drawable.ic_pacto_bell)
        }
        play.notices.forEach { NoticeCard(it, state.busy, { model.openNotice(it) }, { model.deleteNotice(it) }) }
        if (play.noticesMore) MakeButton("Ver más", ButtonKind.Ghost, enabled = !state.busy) { model.loadNotices(true) }
        MakeButton("Actualizar", ButtonKind.Ghost, enabled = !state.busy) { model.loadNotices() }
    }
}

@Composable
private fun NoticeCard(notice: Notice, busy: Boolean, onOpen: () -> Unit, onDelete: () -> Unit) {
    MakeCard(accent = if (notice.read) null else PactoCyan, onClick = { if (!busy) onOpen() }) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f)) {
                Kicker(localDate(notice.createdAt))
                Text(notice.title, style = MaterialTheme.typography.titleSmall)
            }
            if (!notice.read) Pill("Nuevo", Tone.Orange)
        }
        Text(notice.body, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(if (notice.contractId != null) "Ver reto" else "", color = PactoCyan,
                style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
            TextButton(onClick = onDelete, enabled = !busy) { Text("Borrar") }
        }
    }
}
