package com.pactogg.app.feature.account.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.pactogg.app.feature.auth.presentation.PrivacyConsent
import com.pactogg.app.feature.connected.presentation.ActionConfirmation
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.Field

@Composable
internal fun ExtrasScreen(state: ExtrasState, model: ExtrasViewModel) {
    val page = state.page ?: return
    AppPage(ExtraTitles[page].orEmpty(), "PactoGG") {
        if (state.busy) CircularProgressIndicator()
        if (page == ExtrasPage.FORGOT) {
            Text("Escribe el correo de tu cuenta para solicitar un enlace y cambiar tu contraseña.")
        }
        ExtrasFields(state, model)
        if (page in setOf(ExtrasPage.CONSENT, ExtrasPage.GOOGLE_PROFILE)) {
            PrivacyConsent(state.accepted, !state.busy, model::accepted)
        }
        ExtrasChoices(state, model)
        if (page == ExtrasPage.PREFERENCES) com.pactogg.app.core.push.PushPermission()
        state.lines.forEach { Text(it) }
        state.error?.let { Text(it) }
        Action(if (state.cursor == null) "Continuar" else "Cargar más", enabled = !state.busy) {
            if (page in DestructivePages) model.confirmation(true) else model.submit()
        }
        if (page == ExtrasPage.FORGOT) {
            TextButton(onClick = { model.open(ExtrasPage.RESET) }, enabled = !state.busy) {
                Text("Ya tengo un código de recuperación")
            }
        }
        Action("Volver", enabled = !state.busy) {
            model.open(if (page == ExtrasPage.RESET) ExtrasPage.FORGOT else null)
        }
    }
    if (state.confirmation && page in DestructivePages) ActionConfirmation(
        "¿Confirmar el cambio?", "Se enviará esta acción al servicio.", "Confirmar", !state.busy,
        onConfirm = model::submit, onDismiss = { model.confirmation(false) })
}

@Composable
private fun ExtrasFields(state: ExtrasState, model: ExtrasViewModel) {
    ExtraFields[state.page].orEmpty().forEach { (key, label) ->
        val value = state.fields[key].orEmpty()
        if (key in SecretFields) OutlinedTextField(value, { model.input(key, it) },
            label = { Text(label) }, enabled = !state.busy, singleLine = true,
            visualTransformation = PasswordVisualTransformation())
        else Field(label, value) { model.input(key, it) }
    }
}

@Composable
private fun ExtrasChoices(state: ExtrasState, model: ExtrasViewModel) {
    state.choices?.let { value ->
        ChoiceSwitch("Retos", value.challenges, !state.busy) { model.choices(value.copy(challenges = it)) }
        ChoiceSwitch("Votos", value.votes, !state.busy) { model.choices(value.copy(votes = it)) }
        ChoiceSwitch("Torneos", value.tournaments, !state.busy) { model.choices(value.copy(tournaments = it)) }
        ChoiceSwitch("Compromisos", value.commitments, !state.busy) { model.choices(value.copy(commitments = it)) }
    }
}

@Composable
private fun ChoiceSwitch(label: String, selected: Boolean, enabled: Boolean, changed: (Boolean) -> Unit) {
    Text(label)
    Switch(checked = selected, onCheckedChange = changed, enabled = enabled)
}

private val SecretFields = setOf("token", "current", "password", "repeat")
private val DestructivePages = setOf(ExtrasPage.EDIT, ExtrasPage.CANCEL, ExtrasPage.DELETE_NOTICE)
private val PasswordFields = listOf("password" to "Nueva contraseña", "repeat" to "Repetir contraseña")
private val ExtraFields = mapOf(
    ExtrasPage.FORGOT to listOf("email" to "Correo electrónico"),
    ExtrasPage.VERIFY to listOf("token" to "Código de verificación"),
    ExtrasPage.RESET to listOf("token" to "Código recibido") + PasswordFields,
    ExtrasPage.PASSWORD to listOf("current" to "Contraseña actual") + PasswordFields,
    ExtrasPage.GOOGLE_PROFILE to listOf("nickname" to "Elige tu apodo"),
    ExtrasPage.EDIT to listOf("game" to "Juego", "condition" to "Condición", "reward" to "Recompensa sin dinero"),
)
private val ExtraTitles = mapOf(
    ExtrasPage.VERIFY to "Verificar correo", ExtrasPage.RESEND to "Reenviar verificación",
    ExtrasPage.FORGOT to "Recuperar acceso", ExtrasPage.RESET to "Nueva contraseña",
    ExtrasPage.PASSWORD to "Cambiar contraseña", ExtrasPage.CONSENT to "Aviso de privacidad",
    ExtrasPage.MEMBER to "Perfil del integrante", ExtrasPage.AUDIT to "Historial del grupo",
    ExtrasPage.EDIT to "Editar reto", ExtrasPage.CANCEL to "Cancelar reto",
    ExtrasPage.DELETE_NOTICE to "Eliminar aviso", ExtrasPage.PREFERENCES to "Preferencias de avisos",
    ExtrasPage.SERVICE to "Estado del servicio", ExtrasPage.GOOGLE_PROFILE to "Completar registro con Google",
)
