package com.pactogg.app.feature.connected.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pactogg.app.BuildConfig
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.data.LoginRequest
import com.pactogg.app.feature.auth.data.RegisterRequest
import com.pactogg.app.feature.auth.presentation.AccessEvent
import com.pactogg.app.feature.auth.presentation.AccessState
import com.pactogg.app.feature.auth.presentation.reduce
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.feature.connected.domain.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ConnectedViewModel @Inject constructor(
    private val sessions: SessionManager,
    private val products: ProductRepository,
) : ViewModel() {
    internal val mutable = MutableStateFlow(ConnectedState())
    val state = mutable.asStateFlow()
    internal var queryVersion = 0L

    init {
        viewModelScope.launch {
            runCatching { sessions.restore() }.onFailure { error ->
                if (error is CancellationException) throw error
                mutable.update { it.copy(error = "No se pudo recuperar la sesión. Inicia sesión de nuevo.") }
            }
            mutable.update { it.copy(restoring = false) }
            sessions.current.collect { session ->
                if (session?.account?.id != mutable.value.account?.id) {
                    queryVersion++
                    mutable.value = ConnectedState(restoring = false, account = session?.account,
                        busy = mutable.value.busy)
                }
            }
        }
    }

    fun access(event: AccessEvent) {
        if (mutable.value.busy) return
        if (event != AccessEvent.Submit) {
            mutable.update { it.copy(access = it.access.reduce(event), error = null) }
        } else submitAccess()
    }

    private fun submitAccess() {
        val input = mutable.value.access
        val nickname = input.nickname.takeIf { input.registering }
        if (!InputRules.access(input.email, input.password, nickname, input.acceptedPrivacy)) {
            mutable.update { it.copy(error = "Revisa correo, contraseña, apodo y consentimiento de privacidad.") }
            return
        }
        perform {
            if (input.registering) {
                sessions.register(RegisterRequest(input.email.trim(), input.password, input.nickname.trim(),
                    input.acceptedPrivacy, BuildConfig.PRIVACY_VERSION))
            } else sessions.login(LoginRequest(input.email.trim(), input.password))
            mutable.update { it.copy(access = AccessState()) }
        }
    }

    fun navigate(route: ConnectedRoute) {
        if (mutable.value.busy || mutable.value.account == null) return
        mutable.update { it.copy(route = route, error = null, notice = null, selectedTournament = null) }
        if (route == ConnectedRoute.TOURNAMENTS && !mutable.value.tournamentsLoaded) loadTournaments()
    }

    fun createGroup() {
        val name = mutable.value.groupName
        if (name.trim().length !in 2..40) {
            mutable.update { it.copy(error = "El nombre debe tener entre 2 y 40 caracteres.") }
            return
        }
        perform {
            val group = products.createGroup(name)
            mutable.update { it.copy(group = group, groupName = "", contract = null, evidence = null,
                draft = ChallengeDraft(), notice = "Grupo creado en el servidor.") }
        }
    }

    fun createChallenge() {
        val current = mutable.value
        val group = current.group ?: return
        if (!current.reviewing) return
        perform {
            val contract = products.createChallenge(group, current.draft)
            mutable.update { it.copy(contract = contract, route = ConnectedRoute.CONTRACT, reviewing = false,
                draft = ChallengeDraft(), deadlineLocal = "", evidence = null, link = "") }
        }
    }

    fun sendLink() {
        val current = mutable.value
        val contract = current.contract ?: return
        if (!InputRules.evidenceLink(current.link)) {
            mutable.update { it.copy(error = "Usa un enlace HTTPS de los servicios permitidos.") }
            return
        }
        perform {
            val evidence = products.sendLink(contract, current.link)
            mutable.update { it.copy(evidence = evidence, link = "", notice = "Evidencia confirmada por el servidor.") }
        }
    }

    fun sendPhoto(read: suspend () -> Pair<ByteArray, String>) {
        val contract = mutable.value.contract ?: return
        perform {
            val (bytes, mime) = read()
            try {
                val evidence = products.sendPhoto(contract, bytes, mime)
                mutable.update { it.copy(evidence = evidence, notice = "Foto confirmada por el servidor.") }
            } finally { bytes.fill(0) }
        }
    }

    fun loadTournaments(more: Boolean = false) {
        val current = mutable.value
        if (more && (!current.hasMore || current.nextCursor == null)) return
        val version = queryVersion
        perform {
            val page = products.tournaments(current.status, current.gameFilter.trim().ifEmpty { null },
                current.nextCursor.takeIf { more })
            if (version == queryVersion) mutable.update { it.copy(
                tournaments = ((if (more) it.tournaments else emptyList()) + page.items).distinctBy { item -> item.id },
                tournamentsLoaded = true, nextCursor = page.nextCursor, hasMore = page.hasMore) }
        }
    }

    fun logout() {
        if (mutable.value.busy) return
        perform {
            val failed = runCatching { sessions.logout() }.exceptionOrNull()
            if (failed is CancellationException) throw failed
            if (failed != null) mutable.update { it.copy(
                notice = "Sesión cerrada aquí. No se pudo confirmar el cierre en el servidor.") }

        }
    }

    private fun perform(block: suspend () -> Unit) {
        if (mutable.value.busy) return
        mutable.update { it.copy(busy = true, error = null, notice = null, diagnostic = null) }
        viewModelScope.launch {
            try {
                runCatching { block() }.onFailure { error ->
                    if (error is CancellationException) throw error
                    val diagnostic = (error as? ApiFailure)?.let {
                        FailureDiagnostic(it.status, it.code, it.requestId)
                    }
                    mutable.update { it.copy(error = (error as? Exception)?.userMessage()
                        ?: "No se pudo completar la operación.", diagnostic = diagnostic) }
                }
            } finally { mutable.update { it.copy(busy = false, access = it.access.copy(loading = false)) } }
        }
    }
}
