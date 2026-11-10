package com.pactogg.app.feature.account.presentation

import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.pactogg.app.BuildConfig
import kotlinx.coroutines.launch

/** Google returns an ID token; the backend alone creates and rotates the PactoGG session. */
@Composable
fun GoogleAccessButton(busy: Boolean) {
    val extras = LocalExtras.current
    val accountState = extras?.state?.collectAsStateWithLifecycle()?.value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var choosing by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    OutlinedButton(modifier = Modifier.fillMaxWidth(),
        enabled = !busy && !choosing && accountState?.busy != true && extras != null &&
            BuildConfig.GOOGLE_WEB_CLIENT_ID.isNotBlank(),
        onClick = {
        scope.launch {
            choosing = true
            message = null
            extras?.open(null)
            try {
                val option = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_WEB_CLIENT_ID).build()
                val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
                val response = CredentialManager.create(context).getCredential(context, request)
                val credential = response.credential as? CustomCredential
                val token = credential?.takeIf {
                    it.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                }?.let { runCatching { GoogleIdTokenCredential.createFrom(it.data).idToken }.getOrNull() }
                if (token == null) message = "No se pudo comprobar la respuesta de Google."
                else extras?.google(token)
            } catch (_: GetCredentialCancellationException) {
                // The user closed the account picker; no authentication request is sent.
            } catch (_: NoCredentialException) {
                message = "No hay una cuenta de Google disponible. " +
                    "Agrega una cuenta en tu dispositivo y vuelve a intentarlo."
            } catch (_: GetCredentialException) {
                message = "No se pudo abrir Google. Comprueba la conexión y vuelve a intentarlo."
            } finally {
                choosing = false
            }
        }
    }) {
        Text(if (choosing) "Abriendo Google…" else "Continuar con Google")
    }
    val error = message ?: accountState?.error
    error?.let {
        Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
    }
}
