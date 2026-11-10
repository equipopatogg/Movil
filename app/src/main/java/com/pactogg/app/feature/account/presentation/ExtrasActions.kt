package com.pactogg.app.feature.account.presentation

import com.pactogg.app.BuildConfig
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.account.data.AccountAccessRepository
import com.pactogg.app.feature.account.data.DeviceRepository
import com.pactogg.app.feature.account.data.MaintenanceRepository
import com.pactogg.app.feature.account.data.ServiceRepository
import javax.inject.Inject

internal class ExtrasActions @Inject constructor(
    private val access: AccountAccessRepository,
    private val maintenance: MaintenanceRepository,
    private val devices: DeviceRepository,
    private val service: ServiceRepository,
    private val sessions: SessionManager,
) {
    suspend fun execute(state: ExtrasState, googleToken: String?): ExtrasResult = when (state.page) {
        ExtrasPage.VERIFY, ExtrasPage.RESEND, ExtrasPage.FORGOT, ExtrasPage.RESET,
        ExtrasPage.PASSWORD, ExtrasPage.CONSENT, ExtrasPage.GOOGLE_PROFILE -> account(state, googleToken)
        ExtrasPage.EDIT, ExtrasPage.CANCEL, ExtrasPage.DELETE_NOTICE -> mutate(state)
        else -> read(state)
    }
    private suspend fun account(state: ExtrasState, googleToken: String?): ExtrasResult {
        val input = state.fields
        fun field(key: String) = input[key].orEmpty()
        val message = when (state.page) {
            ExtrasPage.VERIFY -> { access.verify(field("token")); "Correo verificado." }
            ExtrasPage.RESEND -> {
                access.resend()
                "Solicitud aceptada. Revisa tu correo cuando el servicio de envío esté disponible."
            }
            ExtrasPage.FORGOT -> {
                access.forgot(field("email").trim())
                "Si existe una cuenta con ese correo, recibirás instrucciones cuando el envío esté disponible."
            }
            ExtrasPage.RESET -> {
                require(field("password") == field("repeat"))
                access.reset(field("token"), field("password"))
                "Contraseña restablecida. Inicia sesión."
            }
            ExtrasPage.PASSWORD -> {
                require(field("password") == field("repeat"))
                access.password(field("current"), field("password"))
                "Contraseña actualizada."
            }
            ExtrasPage.CONSENT -> {
                require(state.accepted)
                access.consent(BuildConfig.PRIVACY_VERSION)
                "Aceptación registrada."
            }
            ExtrasPage.GOOGLE_PROFILE -> {
                access.google(requireNotNull(googleToken), field("nickname"),
                    state.accepted, BuildConfig.PRIVACY_VERSION)
                "Sesión iniciada."
            }
            else -> error("Unsupported account action")
        }
        return ExtrasResult(listOf(message), close = state.page in setOf(ExtrasPage.RESET, ExtrasPage.GOOGLE_PROFILE))
    }
    private suspend fun mutate(state: ExtrasState): ExtrasResult {
        require(state.confirmation)
        if (state.page == ExtrasPage.DELETE_NOTICE) {
            maintenance.deleteNotice(state.resource)
        } else {
            val contract = requireNotNull(state.contract)
            require(contract.challengerId == sessions.current.value?.account?.id)
            require(contract.status == "open" && contract.acceptedBy == null)
            if (state.page == ExtrasPage.CANCEL) maintenance.cancel(contract)
            else maintenance.edit(contract, state.fields["game"].orEmpty(),
                state.fields["condition"].orEmpty(), state.fields["reward"].orEmpty())
        }
        return ExtrasResult(listOf("Cambio confirmado. Actualiza la pantalla para consultar su estado."),
            close = true)
    }
    private suspend fun read(state: ExtrasState): ExtrasResult = when (state.page) {
        ExtrasPage.MEMBER -> {
            val profile = maintenance.profile(state.resource)
            ExtrasResult(listOf(profile.nickname, "Índice de palabra: ${profile.wordIndex}",
                "Completados: ${profile.completed} · Incumplidos: ${profile.cheated}"))
        }
        ExtrasPage.AUDIT -> {
            val page = maintenance.audit(state.resource, state.cursor)
            ExtrasResult(state.lines + page.entries, page.cursor)
        }
        ExtrasPage.PREFERENCES -> {
            val preferences = if (state.confirmation) devices.preferences(requireNotNull(state.choices))
                else devices.preferences()
            ExtrasResult(listOf("Preferencias consultadas."), choices = preferences)
        }
        ExtrasPage.SERVICE -> ExtrasResult(listOf(service.describe(),
            if (service.healthy()) "Servicio disponible" else "Servicio no disponible"))
        else -> error("Unsupported read action")
    }
}
