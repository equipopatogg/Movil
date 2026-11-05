package com.pactogg.app.feature.connected.presentation

import androidx.lifecycle.viewModelScope
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.connected.data.NicknamePattern
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal fun ConnectedViewModel.cancelNicknameCheck() {
    nicknameVersion++
    nicknameJob?.cancel()
    mutable.update { it.copy(social = it.social.copy(nicknameNotice = null)) }
}
internal fun ConnectedViewModel.checkNickname(value: String) {
    cancelNicknameCheck()
    val nickname = value.trim()
    if (!NicknamePattern.matches(nickname)) return
    val version = nicknameVersion
    val accountId = mutable.value.account?.id
    nicknameJob = viewModelScope.launch {
        delay(700)
        if (System.currentTimeMillis() < nicknameRetryAt) {
            mutable.update { it.copy(social = it.social.copy(
                nicknameNotice = "Espera antes de consultar otro apodo.")) }
            return@launch
        }
        val message = runCatching { socials.nickname(nickname) }.fold(
            onSuccess = { if (it.available) "Apodo disponible; no está reservado." else "El apodo está ocupado." },
            onFailure = {
                if (it is CancellationException) throw it
                if (it is ApiFailure && it.status == 429) nicknameRetryAt = System.currentTimeMillis() + 60000
                "No se pudo consultar la disponibilidad. Puedes corregir el apodo o esperar."
            })
        if (version == nicknameVersion && mutable.value.account?.id == accountId) {
            mutable.update { it.copy(social = it.social.copy(nicknameNotice = message)) }
        }
    }
}
