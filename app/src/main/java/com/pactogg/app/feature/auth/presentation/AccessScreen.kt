package com.pactogg.app.feature.auth.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.PageLayout
import com.pactogg.app.core.designsystem.AccentHeading
import com.pactogg.app.ui.theme.PactoInput

@Composable
fun AccessScreen(state: AccessState, onEvent: (AccessEvent) -> Unit,
    fieldError: String? = null, fieldErrorCode: String? = null, tools: @Composable () -> Unit = {}) {
    PageLayout {
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary)
        Text(stringResource(R.string.ui_motto), style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary)
        AccessArtwork(state.registering)
        AccentHeading(stringResource(if (state.registering) R.string.ui_register_hero else R.string.ui_login_hero),
            centered = true)
        Text(stringResource(R.string.ui_access_intro), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(14.dp),
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(painterResource(if (state.registering) R.drawable.ic_pacto_users else R.drawable.ic_pacto_shield),
                    contentDescription = null, tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp))
                Text(stringResource(if (state.registering) R.string.ui_register else R.string.ui_login),
                    style = MaterialTheme.typography.titleLarge)
            }
            AccessFields(state, onEvent, fieldError, fieldErrorCode)
            if (state.registering) PrivacyConsent(state.acceptedPrivacy, !state.loading) {
                onEvent(AccessEvent.AcceptPrivacy(it))
            }
            if (state.submitted && state.registering && !state.acceptedPrivacy) {
                Text("Acepta el aviso de privacidad para crear tu cuenta.",
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    color = MaterialTheme.colorScheme.primary)
            }
            Button(onClick = { onEvent(AccessEvent.Submit) }, enabled = !state.loading,
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                if (state.loading) CircularProgressIndicator()
                else {
                    Text(stringResource(if (state.registering) R.string.ui_register else R.string.ui_login))
                    Icon(painterResource(R.drawable.ic_pacto_chevron), contentDescription = null,
                        modifier = Modifier.size(20.dp))
                }
            }
            TextButton(onClick = { onEvent(AccessEvent.SwitchMode) }, enabled = !state.loading,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(if (state.registering) R.string.ui_have_account else R.string.ui_new_account),
                    color = MaterialTheme.colorScheme.secondary)
            }
        }
        }
        Text(stringResource(R.string.ui_virtual_only), style = MaterialTheme.typography.bodySmall)
        tools()
    }
}

@Composable
private fun AccessFields(state: AccessState, onEvent: (AccessEvent) -> Unit,
    fieldError: String?, fieldErrorCode: String?) {
    val emailError = accessEmailError(state, fieldErrorCode, fieldError)
    val passwordError = accessPasswordError(state, fieldErrorCode, fieldError)
    val nicknameError = accessNicknameError(state, fieldErrorCode, fieldError)
    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = PactoInput,
        unfocusedContainerColor = PactoInput,
        disabledContainerColor = PactoInput,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedBorderColor = MaterialTheme.colorScheme.secondary,
        cursorColor = MaterialTheme.colorScheme.secondary,
    )
    if (state.registering) {
        OutlinedTextField(colors = colors, value = state.nickname,
            isError = nicknameError != null, supportingText = nicknameError?.let { text -> { Text(text) } },
            onValueChange = { onEvent(AccessEvent.Nickname(it)) },
            label = { Text(stringResource(R.string.ui_nickname)) }, singleLine = true,
            enabled = !state.loading, modifier = Modifier.fillMaxWidth())
    }
    OutlinedTextField(colors = colors, value = state.email, onValueChange = { onEvent(AccessEvent.Email(it)) },
        isError = emailError != null, supportingText = emailError?.let { text -> { Text(text) } },
        label = { Text(stringResource(R.string.ui_email)) }, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        enabled = !state.loading, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(colors = colors, value = state.password, onValueChange = { onEvent(AccessEvent.Password(it)) },
        isError = passwordError != null, supportingText = passwordError?.let { text -> { Text(text) } },
        label = { Text(stringResource(R.string.ui_password)) }, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            TextButton(onClick = { onEvent(AccessEvent.TogglePassword) }, enabled = !state.loading) {
                Text(stringResource(if (state.passwordVisible) R.string.ui_hide else R.string.ui_show),
                    color = MaterialTheme.colorScheme.secondary)
            }
        }, enabled = !state.loading, modifier = Modifier.fillMaxWidth())
}
