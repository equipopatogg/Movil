package com.pactogg.app.feature.connected.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.ui.theme.PactoOrange
import kotlinx.coroutines.flow.update

/** AUTH-05: vuelve a mandar el correo de verificación. 409 = ya estaba verificado. */
fun ConnectedViewModel.resendVerification() = perform {
    val response = sessions.authorized { sessions.api.resendVerification(it) }
    when (response.code()) {
        202 -> mutable.update { it.copy(notice = "Te mandamos el correo. Abre el enlace y después toca " +
            "\"Ya lo verifiqué\". Revisa también la carpeta de spam.") }
        409 -> {
            val profile = socials.profile()
            mutable.update {
                it.copy(social = it.social.copy(profile = profile), notice = "Tu correo ya está verificado.")
            }
        }
        else -> throw ApiFailure(response.code(), if (response.code() == 429) "RATE_LIMITED" else "HTTP_ERROR")
    }
}

/** Correo obligatorio (CLARIFICACION #5): sin verificarlo no se puede crear ni unirse a grupos ni ser jurado. */
internal fun ConnectedState.needsVerification(): Boolean =
    social.profile?.emailVerified == false || diagnostic?.code == "EMAIL_NOT_VERIFIED"

@Composable
internal fun VerifyEmailCard(state: ConnectedState, model: ConnectedViewModel) {
    MakeCard(accent = PactoOrange) {
        Kicker("Verifica tu correo", PactoOrange)
        Text("Para crear o unirte a un grupo y ser jurado necesitas confirmar tu correo. Te mandamos un enlace a " +
            (state.account?.email ?: "tu correo") + ".", style = MaterialTheme.typography.bodySmall)
        MakeButton("Reenviar correo", ButtonKind.Secondary, enabled = !state.busy, icon = null) {
            model.resendVerification()
        }
        MakeButton("Ya lo verifiqué", ButtonKind.Ghost, enabled = !state.busy, icon = null) { model.loadProfile() }
    }
}
