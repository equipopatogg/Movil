package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.session.RecoveryEmail
import com.pactogg.app.core.session.RecoveryToken
import com.pactogg.app.core.session.forgotPassword
import com.pactogg.app.core.session.resetPassword
import com.pactogg.app.core.session.validRecoveryPassword
import kotlinx.coroutines.flow.update

/** AUTH-06: manda el enlace. El aviso es el mismo exista o no la cuenta. */
fun ConnectedViewModel.forgotPassword(email: String) {
    val clean = email.trim()
    if (clean.length > 254 || !clean.matches(RecoveryEmail)) {
        mutable.update { it.copy(error = "Escribe un correo válido.") }
        return
    }
    perform {
        sessions.forgotPassword(clean)
        mutable.update { it.copy(notice = "Si ese correo tiene cuenta, te llegó un enlace para cambiar la " +
            "contraseña. Caduca en 1 hora y solo sirve una vez.") }
    }
}

/** Enlace `pactogg://auth/reset-password?token=…` abierto desde el correo. */
fun ConnectedViewModel.resetLink(token: String?) {
    if (token == null) return
    if (!token.matches(RecoveryToken)) {
        mutable.update { it.copy(error = "El enlace no es válido o ya caducó. Pide uno nuevo.") }
        return
    }
    mutable.update { it.copy(play = it.play.copy(resetToken = token)) }
}

/** AUTH-07: cambia la contraseña. Después hay que iniciar sesión con la nueva. */
fun ConnectedViewModel.resetPassword(password: String, confirmation: String) {
    val token = state.value.play.resetToken ?: return
    if (!validRecoveryPassword(password) || password != confirmation) {
        mutable.update { it.copy(error = "La contraseña debe tener de 12 a 128 caracteres y coincidir.") }
        return
    }
    perform {
        sessions.resetPassword(token, password)
        mutable.update { it.copy(play = it.play.copy(resetToken = null),
            notice = "Contraseña cambiada. Inicia sesión con la nueva; se cerraron las demás sesiones.") }
    }
}

fun ConnectedViewModel.cancelReset() {
    if (!state.value.busy) mutable.update { it.copy(play = it.play.copy(resetToken = null)) }
}

/** "¿Olvidaste tu contraseña?" en iniciar sesión, o el formulario de contraseña nueva si llegó un enlace. */
@Composable
internal fun PasswordRecovery(state: ConnectedState, model: ConnectedViewModel) {
    if (state.play.resetToken != null) {
        NewPasswordCard(state.busy, model)
        return
    }
    var open by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf(state.access.email) }
    if (!open) {
        TextButton(onClick = { open = true }) { Text("¿Olvidaste tu contraseña?") }
        return
    }
    MakeCard {
        Kicker("Recuperar contraseña")
        Text("Te mandamos un enlace a tu correo para elegir una contraseña nueva.",
            style = MaterialTheme.typography.bodySmall)
        MakeField("Correo de tu cuenta", email, { email = it.take(254) })
        MakeButton("Enviar enlace", enabled = !state.busy) { model.forgotPassword(email) }
        MakeButton("Cancelar", ButtonKind.Ghost, enabled = !state.busy) { open = false }
    }
}

@Composable
private fun NewPasswordCard(busy: Boolean, model: ConnectedViewModel) {
    var password by rememberSaveable { mutableStateOf("") }
    var confirmation by rememberSaveable { mutableStateOf("") }
    MakeCard {
        Kicker("Contraseña nueva")
        Text("Abriste el enlace del correo. Elige una contraseña de 12 a 128 caracteres.",
            style = MaterialTheme.typography.bodySmall)
        SecretField("Contraseña nueva", password, !busy) { password = it.take(128) }
        SecretField("Repite la contraseña", confirmation, !busy) { confirmation = it.take(128) }
        MakeButton("Guardar contraseña", enabled = !busy) { model.resetPassword(password, confirmation) }
        MakeButton("Cancelar", ButtonKind.Ghost, enabled = !busy) { model.cancelReset() }
    }
}

@Composable
private fun SecretField(label: String, value: String, enabled: Boolean, onChange: (String) -> Unit) {
    OutlinedTextField(value, onChange, Modifier.fillMaxWidth(), singleLine = true, enabled = enabled,
        label = { Text(label) }, visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
}
