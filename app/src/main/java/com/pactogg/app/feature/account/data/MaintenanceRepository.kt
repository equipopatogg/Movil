package com.pactogg.app.feature.account.data

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.checked
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.account.domain.HistoryPage
import com.pactogg.app.feature.account.domain.MemberPreview
import com.pactogg.app.feature.connected.domain.Challenge
import javax.inject.Inject

class MaintenanceRepository @Inject constructor(private val api: PactoApi, private val sessions: SessionManager) {
    suspend fun profile(id: String): MemberPreview {
        validObjectId(id)
        val value = sessions.authorized(true) { api.publicProfile(it, id) }.checked(200).data
        if (value.id != id || value.nickname.isBlank() || value.stats.wordIndex !in 0..100) invalidAccountResponse()
        return MemberPreview(value.id, value.nickname, value.stats.wordIndex,
            value.stats.completed, value.stats.cheated)
    }
    suspend fun edit(previous: Challenge, game: String, condition: String, reward: String): Challenge {
        validObjectId(previous.id)
        require(game.length in 1..60 && condition.length in 1..280 && reward.length in 3..80)
        require(!Regex("[$€£]|(?i)\\b(pesos?|usd|mxn|d[oó]lares?)\\b").containsMatchIn(reward))
        val version = requireNotNull(previous.version)
        val value = sessions.authorized { api.editContract(it, previous.id,
            EditContractRequest(version, game, condition, reward)) }.checked(200).data.toDomain()
        if (value.id != previous.id || value.groupId != previous.groupId || value.version != version + 1) {
            invalidAccountResponse()
        }
        return value
    }
    suspend fun cancel(previous: Challenge) {
        validObjectId(previous.id)
        val version = requireNotNull(previous.version)
        require(version > 0)
        sessions.authorized { api.cancelContract(it, previous.id, version) }.expectStatus(204)
    }
    suspend fun deleteNotice(id: String) {
        validObjectId(id)
        sessions.authorized { api.deleteNotification(it, id) }.expectStatus(204)
    }
    suspend fun audit(group: String, cursor: String?): HistoryPage {
        validObjectId(group)
        val result = sessions.authorized(true) { api.audit(it, group, cursor) }.checked(200)
        val meta = result.meta ?: invalidAccountResponse()
        if (meta.hasMore && (meta.nextCursor.isNullOrBlank() || meta.nextCursor == cursor)) invalidAccountResponse()
        return HistoryPage(result.data.map { "${it.createdAt} · ${it.action} · ${it.actor ?: "Sistema"}" },
            meta.nextCursor.takeIf { meta.hasMore })
    }
}
