package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.update

fun ConnectedViewModel.dismissError(message: String) {
    mutable.update { if (it.error == message) it.copy(error = null, diagnostic = null) else it }
}

fun ConnectedViewModel.dismissNotice(message: String) {
    mutable.update { if (it.notice == message) it.copy(notice = null) else it }
}

/** An uncertain business request is never retried by a dialog. */
@Composable
internal fun BoxScope.AppFeedback(state: ConnectedState, dismissError: (String) -> Unit,
    dismissNotice: (String) -> Unit) {
    val host = remember { SnackbarHostState() }
    LaunchedEffect(state.notice, state.route, state.account?.id, state.access.registering) {
        host.currentSnackbarData?.dismiss()
        val message = state.notice ?: return@LaunchedEffect
        host.showSnackbar(message, actionLabel = "Entendido", withDismissAction = true,
            duration = SnackbarDuration.Short)
        dismissNotice(message)
    }
    SnackbarHost(host, Modifier.align(Alignment.BottomCenter)
        .padding(horizontal = 16.dp, vertical = if (state.account == null) 16.dp else 88.dp))
    val error = state.error
    if (error != null && state.inlineFeedback() == null) {
        AlertDialog(onDismissRequest = { dismissError(error) },
            title = { Text("No pudimos completar la acción") }, text = { Text(error) },
            confirmButton = { TextButton(onClick = { dismissError(error) }) { Text("Entendido") } })
    }
}

internal fun ConnectedState.inlineFeedback(): String? = error?.takeIf {
    when (diagnostic?.code) {
        null -> account == null && access.submitted
        "NICKNAME_ALREADY_EXISTS" -> account == null || route == ConnectedRoute.PROFILE
        "EMAIL_ALREADY_EXISTS", "INVALID_CREDENTIALS" -> account == null
        else -> false
    }
}

@Composable
internal fun ActionConfirmation(title: String, message: String, confirmLabel: String,
    enabled: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit, confirmEnabled: Boolean = enabled) {
    AlertDialog(onDismissRequest = { if (enabled) onDismiss() },
        title = { Text(title) }, text = { Text(message) },
        confirmButton = { TextButton(enabled = confirmEnabled, onClick = onConfirm) { Text(confirmLabel) } },
        dismissButton = { TextButton(enabled = enabled, onClick = onDismiss) { Text("Cancelar") } })
}
