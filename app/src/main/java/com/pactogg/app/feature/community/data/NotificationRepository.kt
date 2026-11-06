package com.pactogg.app.feature.community.data

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.checked
import com.pactogg.app.core.session.SessionManager

class NotificationRepository(private val api: PactoApi, private val sessions: SessionManager) {
    suspend fun list(cursor: String? = null) = sessions.authorized(readOnly = true) { api.notifications(it,
        cursor) }.checked(200)
    suspend fun read(id: String): NotificationResponse {
        validId(id)
        return sessions.authorized { api.readNotification(it, id) }.checked(200).data.also {
            same(id, it.id)
            if (!it.read) throw ApiFailure(0, "INVALID_RESPONSE")
        }
    }
    suspend fun readAll() = sessions.authorized { api.readAllNotifications(it) }.checked(200).data
}
