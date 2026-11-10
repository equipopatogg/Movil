package com.pactogg.app.feature.account.data

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.checked
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.account.domain.NotificationChoices
import javax.inject.Inject

class DeviceRepository @Inject constructor(private val api: PactoApi, private val sessions: SessionManager) {
    suspend fun preferences(): NotificationChoices =
        sessions.authorized(true) { api.preferences(it) }.checked(200).data.toDomain()
    suspend fun preferences(value: NotificationChoices): NotificationChoices =
        sessions.authorized { api.preferences(it,
            PreferencesDto(value.challenges, value.votes, value.tournaments, value.commitments)) }
            .checked(200).data.toDomain()
    suspend fun register(token: String, version: String) {
        require(token.length in 20..4096 && token.none(Char::isWhitespace) && version.length in 1..32)
        val value = sessions.authorized { api.registerDevice(it, DeviceRequest(token, version = version)) }
            .checked(200).data
        if (value.platform != "android" || value.id.isBlank()) invalidAccountResponse()
    }
    suspend fun remove(token: String) {
        require(token.length in 20..4096 && token.none(Char::isWhitespace))
        sessions.authorized { api.removeDevice(it, token) }.expectStatus(204)
    }
}

private fun PreferencesDto.toDomain() = NotificationChoices(retos, votos, torneos, compromisos)
