package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.pactogg.app.BuildConfig
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.session.google
import com.pactogg.app.feature.auth.data.GoogleRequest
import com.pactogg.app.feature.auth.presentation.PrivacyConsent
import com.pactogg.app.feature.connected.data.NicknamePattern
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Entra con Google. Si la cuenta no existe, el servidor pide apodo y se guarda el token solo en memoria. */
fun ConnectedViewModel.googleAccess(token: String) = perform {
    try {
        sessions.google(GoogleRequest(token))
    } catch (error: ApiFailure) {
        if (error.code != "NICKNAME_REQUIRED") throw error
        mutable.update { it.copy(play = it.play.copy(googleToken = token)) }
    }
}

/** Termina la cuenta nueva de Google con apodo y aviso de privacidad aceptado. */
fun ConnectedViewModel.googleRegister(nickname: String, accepted: Boolean) {
    val token = state.value.play.googleToken ?: return
    if (!nickname.trim().matches(NicknamePattern) || !accepted) {
        mutable.update { it.copy(error = "Elige un apodo de 3 a 24 letras, números, punto, guion o guion bajo, " +
            "y acepta el aviso de privacidad.") }
        return
    }
    perform { sessions.google(GoogleRequest(token, nickname.trim(), true, BuildConfig.PRIVACY_VERSION)) }
}

fun ConnectedViewModel.cancelGoogle() {
    if (!state.value.busy) mutable.update { it.copy(play = it.play.copy(googleToken = null)) }
}

/** Botón "Continuar con Google" (AUTH-02) y, si es la primera vez, el formulario de apodo. */
@Composable
internal fun GoogleAccess(state: ConnectedState, model: ConnectedViewModel) {
    if (state.play.googleToken != null) {
        GoogleNickname(state.busy, model)
        return
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var choosing by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    OutlinedButton(modifier = Modifier.fillMaxWidth(),
        enabled = !state.busy && !choosing && BuildConfig.GOOGLE_WEB_CLIENT_ID.isNotBlank(),
        onClick = {
            scope.launch {
                choosing = true
                message = null
                try {
                    val option = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_WEB_CLIENT_ID).build()
                    val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
                    val credential = CredentialManager.create(context).getCredential(context, request)
                        .credential as? CustomCredential
                    val token = credential?.takeIf {
                        it.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    }?.let { runCatching { GoogleIdTokenCredential.createFrom(it.data).idToken }.getOrNull() }
                    if (token == null) message = "No se pudo comprobar la respuesta de Google."
                    else model.googleAccess(token)
                } catch (_: GetCredentialCancellationException) {
                    // La persona cerró el selector de cuentas: no se envía nada.
                } catch (_: NoCredentialException) {
                    message = "No hay una cuenta de Google en este teléfono. Agrega una y vuelve a intentarlo."
                } catch (_: GetCredentialException) {
                    message = "No se pudo abrir Google. Revisa tu conexión y vuelve a intentarlo."
                } finally {
                    choosing = false
                }
            }
        }) { Text(if (choosing) "Abriendo Google…" else "Continuar con Google") }
    message?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error) }
}

@Composable
private fun GoogleNickname(busy: Boolean, model: ConnectedViewModel) {
    var nickname by rememberSaveable { mutableStateOf("") }
    var accepted by rememberSaveable { mutableStateOf(false) }
    MakeCard {
        Text("Es tu primera vez con Google", style = MaterialTheme.typography.titleMedium)
        Text("Elige el apodo con el que te verá tu squad.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        MakeField("Apodo", nickname, { nickname = it.take(24) }, hint = "3 a 24: letras, números, punto o guion")
        PrivacyConsent(accepted, !busy) { accepted = it }
        MakeButton("Crear cuenta con Google", enabled = !busy) { model.googleRegister(nickname, accepted) }
        MakeButton("Cancelar", ButtonKind.Ghost, enabled = !busy, icon = null) { model.cancelGoogle() }
    }
}
