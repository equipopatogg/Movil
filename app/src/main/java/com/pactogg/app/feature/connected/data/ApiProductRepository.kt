package com.pactogg.app.feature.connected.data

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.checked
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.feature.connected.domain.Evidence
import com.pactogg.app.feature.connected.domain.Group
import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.feature.connected.domain.ProductRepository
import com.pactogg.app.feature.connected.domain.TournamentPage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ApiProductRepository(private val api: PactoApi, private val sessions: SessionManager) : ProductRepository {
    override suspend fun createGroup(name: String): Group {
        require(name.trim().length in 2..40)
        return sessions.authorized { api.group(it, GroupRequest(name.trim())) }.checked(201).data.toDomain()
    }

    override suspend fun createChallenge(group: Group, draft: ChallengeDraft): Challenge =
        createChallenge(group, draft, false)

    override suspend fun createChallengeInGroup(group: Group, draft: ChallengeDraft): Challenge =
        createChallenge(group, draft, true)

    private suspend fun createChallenge(group: Group, draft: ChallengeDraft, scoped: Boolean): Challenge {
        require(InputRules.challenge(group, draft, System.currentTimeMillis()))
        val body = ContractRequest(group.id, draft.game.trim(), draft.condition.trim(), draft.reward.trim(),
            draft.deadline, requireNotNull(draft.bond),
            targetType = if (draft.targetId == null) "group" else "member", targetId = draft.targetId)
        return sessions.authorized {
            if (scoped) api.groupContract(it, group.id, body) else api.contract(it, body)
        }.checked(201).data.toDomain().also {
            if (it.groupId != group.id) throw ApiFailure(0, "INVALID_RESPONSE")
        }
    }

    override suspend fun challenge(contract: Challenge): Challenge {
        val result = sessions.authorized(readOnly = true) { api.challenge(it, contract.id) }
            .checked(200).data.toDomain()
        if (result.id != contract.id || result.groupId != contract.groupId) {
            throw ApiFailure(0, "INVALID_RESPONSE")
        }
        return result
    }

    override suspend fun sendLink(contract: Challenge, url: String): Evidence {
        require(InputRules.evidenceLink(url))
        return sessions.authorized { api.link(it, contract.id, LinkRequest(url)) }.checked(201).data
            .toDomain().verified(contract.id, "link")
    }

    override suspend fun sendPhoto(contract: Challenge, bytes: ByteArray, mimeType: String): Evidence {
        require(bytes.size in 1..MaxPhotoBytes && mimeType in AllowedPhotoTypes)
        val extension = when (mimeType) { "image/png" -> "png"; "image/webp" -> "webp"; else -> "jpg" }
        val file = MultipartBody.Part.createFormData("file", "evidencia.$extension",
            bytes.toRequestBody(mimeType.toMediaType()))
        return sessions.authorized { api.photo(it, contract.id, file,
            "photo".toRequestBody("text/plain".toMediaType())) }.checked(201).data
            .toDomain().verified(contract.id, "photo")
    }

    override suspend fun evidence(contract: Challenge): List<Evidence> {
        val items = sessions.authorized(readOnly = true) { api.evidence(it, contract.id) }.checked(200).data
        return items.map { it.toDomain().verified(contract.id) }.distinctBy { it.id }
    }

    override suspend fun tournaments(status: String?, game: String?, cursor: String?): TournamentPage =
        tournamentPage(api, sessions, null, status, game, cursor)

    override suspend fun groupTournaments(groupId: String, status: String?, game: String?,
        cursor: String?): TournamentPage {
        require(groupId.matches(Regex("[a-fA-F0-9]{24}")))
        return tournamentPage(api, sessions, groupId, status, game, cursor)
    }

    companion object {
        const val MaxPhotoBytes = 5 * 1024 * 1024
        val AllowedPhotoTypes = setOf("image/jpeg", "image/png", "image/webp")
    }
}

private suspend fun tournamentPage(api: PactoApi, sessions: SessionManager, groupId: String?, status: String?,
    game: String?, cursor: String?): TournamentPage {
    require(status == null || status in listOf("open", "in_progress", "finished", "cancelled"))
    require(game == null || game.length <= 60)
    val body = sessions.authorized(readOnly = true) {
        if (groupId == null) api.tournaments(it, status, game, cursor)
        else api.groupTournaments(it, groupId, status, game, cursor)
    }.checked(200)
    val meta = body.meta ?: throw ApiFailure(0, "INVALID_RESPONSE")
    if (meta.hasMore && (meta.nextCursor.isNullOrBlank() || meta.nextCursor == cursor)) {
        throw ApiFailure(0, "INVALID_RESPONSE")
    }
    return TournamentPage(body.data.map { it.toDomain() }, meta.hasMore, meta.nextCursor)
}

private fun Evidence.verified(id: String, expectedType: String = type): Evidence = also {
    if (contractId != id || type != expectedType) throw ApiFailure(0, "INVALID_RESPONSE")
}
