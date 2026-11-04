package com.pactogg.app.feature.auth.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pactogg.app.BuildConfig
import com.pactogg.app.R

@Composable
fun PrivacyConsent(accepted: Boolean, enabled: Boolean, onAccepted: (Boolean) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    Row {
        Checkbox(accepted, onCheckedChange = onAccepted, enabled = enabled)
        TextButton(onClick = { visible = true }, enabled = enabled) {
            Text(stringResource(R.string.privacy_accept))
        }
    }
    if (visible) AlertDialog(onDismissRequest = { visible = false },
        title = { Text(stringResource(R.string.privacy_title, BuildConfig.PRIVACY_VERSION)) },
        text = { Text(stringResource(R.string.privacy_basic), Modifier.verticalScroll(rememberScrollState())) },
        confirmButton = { TextButton(onClick = { visible = false }) { Text(stringResource(R.string.privacy_close)) } })
}
