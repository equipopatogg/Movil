package com.pactogg.app.feature.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pactogg.app.BuildConfig
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.account.data.AccountAccessRepository
import com.pactogg.app.feature.account.domain.NotificationChoices
import com.pactogg.app.feature.connected.domain.Challenge
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExtrasViewModel @Inject internal constructor(
    private val actions: ExtrasActions,
    private val access: AccountAccessRepository,
    private val sessions: SessionManager,
) : ViewModel() {
    private val mutable = MutableStateFlow(ExtrasState())
    val state = mutable.asStateFlow()
    private var version = 0L
    private var googleToken: String? = null
    private var pendingLink: Pair<ExtrasPage, String>? = null
    init {
        viewModelScope.launch {
            var account = sessions.current.value?.account?.id
            sessions.current.collect { session ->
                if (account != session?.account?.id) {
                    account = session?.account?.id
                    version++
                    googleToken = null
                    mutable.value = ExtrasState()
                    pendingLink?.let { (page, token) ->
                        mutable.value = ExtrasState(page = page, fields = mapOf("token" to token))
                    }
                }
            }
        }
    }
    fun open(page: ExtrasPage?, resource: String = "", contract: Challenge? = null) {
        if (mutable.value.busy) return
        version++
        googleToken = null
        pendingLink = null
        mutable.value = ExtrasState(page = page, resource = resource, contract = contract,
            fields = contract?.let { mapOf("game" to it.game, "condition" to it.condition, "reward" to it.reward) }
                ?: emptyMap())
    }
    fun input(key: String, value: String) {
        if (!mutable.value.busy && value.length <= 4096) mutable.value = mutable.value.copy(
            fields = mutable.value.fields + (key to value), confirmation = false, error = null)
    }
    fun accepted(value: Boolean) { mutable.value = mutable.value.copy(accepted = value) }
    fun choices(value: NotificationChoices) { mutable.value = mutable.value.copy(choices = value, confirmation = true) }
    fun confirmation(value: Boolean) { mutable.value = mutable.value.copy(confirmation = value) }
    fun link(raw: String?) {
        parseAccountLink(raw)?.let { (page, token) ->
            open(page)
            pendingLink = page to token
            mutable.value = mutable.value.copy(fields = mapOf("token" to token))
        }
    }
    fun google(token: String) {
        if (mutable.value.busy) return
        googleToken = token
        run {
            access.google(token, null, false, BuildConfig.PRIVACY_VERSION)
            ExtrasResult(close = true)
        }
    }
    fun submit() {
        val snapshot = mutable.value
        pendingLink = null
        run { actions.execute(snapshot, googleToken) }
    }
    private fun run(block: suspend () -> ExtrasResult) {
        if (mutable.value.busy) return
        val ticket = version
        mutable.value = mutable.value.copy(busy = true, error = null)
        viewModelScope.launch {
            runCatching { block() }.onSuccess { result ->
                if (ticket == version) {
                    mutable.value = if (result.close) ExtrasState()
                        else mutable.value.copy(busy = false, fields = emptyMap(), confirmation = false,
                            lines = result.lines, cursor = result.cursor, choices = result.choices)
                    if (result.close) { googleToken = null; pendingLink = null }
                }
            }.onFailure { error ->
                if (error is CancellationException || error !is Exception) throw error
                if (ticket == version) mutable.value = mutable.value.copy(busy = false,
                    page = if (error is ApiFailure && error.code == "NICKNAME_REQUIRED")
                        ExtrasPage.GOOGLE_PROFILE else mutable.value.page,
                    error = accountError(error))
            }
        }
    }
}

private fun accountError(error: Exception): String = when ((error as? ApiFailure)?.code) {
    "NICKNAME_REQUIRED" -> "Elige un apodo y acepta el aviso para terminar tu registro."
    "INVALID_OR_EXPIRED_TOKEN" -> "El código no es válido o ya venció."
    "EMAIL_ALREADY_VERIFIED" -> "Tu correo ya está verificado."
    "GOOGLE_AUTH_UNAVAILABLE" -> "El acceso con Google aún no está habilitado en el servidor. " +
        "Puedes iniciar sesión con tu correo."
    "INVALID_GOOGLE_TOKEN" -> "No se pudo validar Google. Vuelve a elegir tu cuenta."
    "PASSWORD_NOT_SET" -> "Esta cuenta usa Google. Recupera el acceso desde Google."
    "INVALID_CREDENTIALS" -> "Revisa tu contraseña actual."
    "RATE_LIMITED" -> "Espera antes de volver a intentarlo."
    "NOT_GROUP_ADMIN", "NO_SHARED_GROUP", "NOT_CONTRACT_OWNER" -> "No tienes permiso para esta acción."
    "INVALID_CONTRACT_STATE", "VERSION_CONFLICT" -> "El reto cambió. Vuelve a consultarlo."
    else -> if (error is IllegalArgumentException) "Revisa los datos y la confirmación de contraseña."
        else "No se pudo completar. Comprueba la conexión y vuelve a consultar el estado."
}
