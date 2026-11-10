package com.pactogg.app.core.push

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pactogg.app.PactoGGApplication
import com.pactogg.app.R

@Composable
internal fun PushConnectionStatus() {
    val app = LocalContext.current.applicationContext as? PactoGGApplication ?: return
    val state by app.push.connection.collectAsStateWithLifecycle()
    val message = when (state) {
        PushConnectionState.NOT_CONFIGURED -> R.string.push_not_configured
        PushConnectionState.SIGN_IN_REQUIRED -> R.string.push_sign_in_required
        PushConnectionState.PERMISSION_REQUIRED -> R.string.push_permission_required
        PushConnectionState.CONNECTING -> R.string.push_connecting
        PushConnectionState.REGISTERING -> R.string.push_registering
        PushConnectionState.REGISTERED -> R.string.push_registered
        PushConnectionState.TOKEN_ERROR -> R.string.push_token_error
        PushConnectionState.SERVER_ERROR -> R.string.push_server_error
    }
    Text(stringResource(message))
    if (state == PushConnectionState.TOKEN_ERROR || state == PushConnectionState.SERVER_ERROR) {
        TextButton(onClick = app.push::refresh) {
            Text(stringResource(R.string.push_check_connection))
        }
    }
}
