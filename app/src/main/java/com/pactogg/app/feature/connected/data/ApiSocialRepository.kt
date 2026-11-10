package com.pactogg.app.feature.connected.data

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.Envelope
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.checked
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.connected.domain.BoardFilter
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.Group
import com.pactogg.app.feature.connected.domain.Member
import com.pactogg.app.feature.connected.domain.NicknameAvailability
import com.pactogg.app.feature.connected.domain.Page
import com.pactogg.app.feature.connected.domain.Profile
import com.pactogg.app.feature.connected.domain.SocialRepository

class ApiSocialRepository(private val api: PactoApi, private val sessions: SessionManager) : SocialRepository {
    override suspend fun profile(): Profile {
        val result = sessions.authorized(readOnly = true) { api.profile(it) }.checked(200).data.toDomain()
        sessions.updateAccount(result.account)
        return result
    }
    override suspend fun updateProfile(nickname: String): Profile {
        require(nickname.matches(NicknamePattern))
        val result = sessions.authorized { api.updateProfile(it, ProfilePatch(nickname)) }.checked(200).data.toDomain()
        sessions.updateAccount(result.account)
        return result
    }
    override suspend fun nickname(value: String): NicknameAvailability {
        require(value.matches(NicknamePattern))
        val result = api.nickname(value).checked(200).data
        if (result.nickname != value) throw ApiFailure(0, "INVALID_RESPONSE")
        return NicknameAvailability(result.nickname, result.available)
    }
    override suspend fun groups(query: String, cursor: String?): Page<Group> {
        require(query.length <= 40)
        val result = sessions.authorized(readOnly = true) { api.groups(it, query.ifBlank { null }, cursor) }
            .checked(200)
        return result.page(cursor) { it.toDomain() }
    }
    override suspend fun group(id: String): Group {
        require(id.matches(SocialId))
        return sessions.authorized(readOnly = true) { api.getGroup(it, id) }.checked(200).data.toDomain().also {
            if (it.id != id) throw ApiFailure(0, "INVALID_RESPONSE")
        }
    }
    override suspend fun join(code: String): Group {
        require(code.matches(Regex("[A-Za-z0-9]{6}")))
        return sessions.authorized { api.join(it, JoinRequest(code.uppercase(java.util.Locale.ROOT))) }
            .checked(200).data.toDomain()
    }
    override suspend fun members(id: String, query: String, cursor: String?): Page<Member> {
        require(id.matches(SocialId) && query.length <= 24)
        return sessions.authorized(readOnly = true) { api.members(it, id, query.ifBlank { null }, cursor) }
            .checked(200).page(cursor) { it.toDomain() }
    }
    override suspend fun contracts(id: String, filter: BoardFilter, cursor: String?): Page<Challenge> {
        require(id.matches(SocialId) && filter.game.length <= 60 && filter.query.length <= 60)
        require(filter.status == null || filter.status in ContractStatuses)
        val filters = buildMap {
            filter.status?.let { put("status", it) }
            put("mine", filter.mine.toString())
            if (filter.game.isNotBlank()) put("game", filter.game)
            if (filter.query.isNotBlank()) put("q", filter.query)
        }
        return sessions.authorized(readOnly = true) { api.contracts(it, id, filters, cursor) }
            .checked(200).page(cursor) { dto -> dto.toDomain().also {
            if (it.groupId != id) throw ApiFailure(0, "INVALID_RESPONSE")
        } }
    }
    override suspend fun accept(contract: Challenge): Challenge {
        val version = requireNotNull(contract.version)
        val operationKey = java.util.UUID.randomUUID().toString()
        val result = sessions.authorized { api.accept(it, contract.id, ContractAction(version), operationKey) }
            .checked(200).data.toDomain().forContract(contract)
        if (result.status != "in_progress" || result.myPoints == null || result.myPoints < 0) {
            throw ApiFailure(0, "INVALID_RESPONSE")
        }
        return result
    }
    override suspend fun reject(contract: Challenge): Challenge {
        val version = requireNotNull(contract.version)
        val result = sessions.authorized { api.reject(it, contract.id, ContractAction(version)) }
            .checked(200).data.toDomain().forContract(contract)
        if (result.status != "cancelled" || result.closedReason != "rejected") throw ApiFailure(0, "INVALID_RESPONSE")
        return result
    }
}

private fun Challenge.forContract(previous: Challenge): Challenge = also {
    if (id != previous.id || groupId != previous.groupId) throw ApiFailure(0, "INVALID_RESPONSE")
}
private fun <T, R> Envelope<List<T>>.page(cursor: String?, map: (T) -> R): Page<R> {
    val paging = meta ?: throw ApiFailure(0, "INVALID_RESPONSE")
    if (paging.hasMore && (paging.nextCursor.isNullOrBlank() || paging.nextCursor == cursor)) {
        throw ApiFailure(0, "INVALID_RESPONSE")
    }
    return Page(data.map(map), paging.hasMore, paging.nextCursor)
}
val NicknamePattern = Regex("[A-Za-z0-9_.-]{3,24}")
val ContractStatuses = listOf("open", "in_progress", "voting", "fulfilled", "cheating", "expired", "cancelled")
