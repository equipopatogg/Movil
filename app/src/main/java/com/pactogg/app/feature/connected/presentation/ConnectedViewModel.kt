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
    internal val products: ProductRepository,
    internal val socials: com.pactogg.app.feature.connected.domain.SocialRepository,
    internal val community: com.pactogg.app.feature.community.data.CommunityRepository? = null,
) : ViewModel() {
    internal val mutable = MutableStateFlow(ConnectedState())
    val state = mutable.asStateFlow()
    internal var queryVersion = 0L
    internal var nicknameJob: kotlinx.coroutines.Job? = null
    internal var nicknameVersion = 0L
    internal var nicknameRetryAt = 0L

    override fun onCleared() {
        mutable.value.selectedPhoto?.clear()
        super.onCleared()
    }

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
                    nicknameVersion++
                    nicknameJob?.cancel()
                    mutable.value.selectedPhoto?.clear()
                    mutable.value = ConnectedState(restoring = false, account = session?.account,
                        busy = mutable.value.busy)
                } else if (session?.account != mutable.value.account) {
                    mutable.update { it.copy(account = session?.account) }
                }
            }
        }
    }

    fun access(event: AccessEvent) {
        if (mutable.value.busy) return
        if (event != AccessEvent.Submit) {
            mutable.update { it.copy(access = it.access.reduce(event), error = null,
                diagnostic = null, notice = null) }
            if (event is AccessEvent.Nickname) checkNickname(mutable.value.access.nickname)
            if (event == AccessEvent.SwitchMode) cancelNicknameCheck()
        } else submitAccess()
    }

    private fun submitAccess() {
        val input = mutable.value.access
        val nickname = input.nickname.takeIf { input.registering }
        if (!InputRules.access(input.email, input.password, nickname, input.acceptedPrivacy)) {
            mutable.update { it.copy(access = it.access.copy(submitted = true),
                error = "Revisa los campos señalados y acepta el aviso de privacidad si estás creando una cuenta.") }
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
        if (route != ConnectedRoute.EVIDENCE) mutable.value.selectedPhoto?.clear()
        mutable.update { it.copy(route = route, community = if (
            it.route == route) it.community else CommunityState(), error = null, diagnostic = null, notice = null,
            evidenceReturnRoute = evidenceBackRoute(it, route),
            selectedPhoto = if (route == ConnectedRoute.EVIDENCE) it.selectedPhoto else null,
            selectedTournament = null) }
        loadRoute(route)
    }

    fun createGroup() {
        val name = mutable.value.groupName
        if (name.trim().length !in 2..40) {
            mutable.update { it.copy(error = "El nombre debe tener entre 2 y 40 caracteres.") }
            return
        }
        perform {
            val group = products.createGroup(name)
            applyGroup(group)
            mutable.update { it.copy(groupName = "", notice = "Grupo creado. Ya puedes invitar a tu equipo.") }
        }
    }

    fun createChallenge() {
        val current = mutable.value
        val group = current.group ?: return
        val targetAllowed = current.draft.targetId == null ||
            (current.draft.targetId != current.account?.id &&
                current.social.selectedRival?.id == current.draft.targetId)
        if (!current.reviewing || !targetAllowed) return
        perform {
            val contract = products.createChallengeInGroup(group, current.draft)
            mutable.value.selectedPhoto?.clear()
            mutable.update { it.copy(contract = contract, contractStateStale = false,
                route = ConnectedRoute.CONTRACT, reviewing = false,
                draft = ChallengeDraft(), deadlineLocal = "", evidence = null, link = "",
                evidenceItems = emptyList(), evidenceLoaded = false, evidenceUnavailable = false,
                evidenceBlockedId = null, selectedPhoto = null,
                social = it.social.copy(contracts = ListState(), pendingAction = null, selectedRival = null)) }
        }
    }

    fun sendLink() = sendEvidenceLink()

    fun loadTournaments(more: Boolean = false) {
        val current = mutable.value
        if (more && (!current.hasMore || current.nextCursor == null)) return
        val version = queryVersion
        perform {
            val game = current.gameFilter.trim().ifEmpty { null }
            val cursor = current.nextCursor.takeIf { more }
            val page = if (current.groupTournamentsOnly) products.groupTournaments(
                requireNotNull(current.group).id, current.status, game, cursor)
            else products.tournaments(current.status, game, cursor)
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
                notice = "Cerraste sesión en este dispositivo. No pudimos confirmar el cierre remoto.") }

        }
    }

    internal fun perform(block: suspend () -> Unit) {
        if (mutable.value.busy) return
        val accountId = mutable.value.account?.id
        mutable.update { it.copy(busy = true, error = null, notice = null, diagnostic = null) }
        viewModelScope.launch {
            try {
                runCatching { block() }.onFailure { error ->
                    if (error is CancellationException) throw error
                    if (accountId != null && mutable.value.account?.id != accountId) return@onFailure
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
