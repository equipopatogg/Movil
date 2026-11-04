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

    override suspend fun createChallenge(group: Group, draft: ChallengeDraft): Challenge {
        require(InputRules.challenge(group, draft, System.currentTimeMillis()))
        val body = ContractRequest(group.id, draft.game.trim(), draft.condition.trim(), draft.reward.trim(),
            draft.deadline, requireNotNull(draft.bond))
        return sessions.authorized { api.contract(it, body) }.checked(201).data.toDomain().also {
            if (it.groupId != group.id) throw ApiFailure(0, "INVALID_RESPONSE")
        }
    }

    override suspend fun sendLink(contract: Challenge, url: String): Evidence {
        require(InputRules.evidenceLink(url))
        return sessions.authorized { api.link(it, contract.id, LinkRequest(url)) }.checked(201).data
            .toDomain().verified(contract.id)
    }

    override suspend fun sendPhoto(contract: Challenge, bytes: ByteArray, mimeType: String): Evidence {
        require(bytes.size in 1..MaxPhotoBytes && mimeType in AllowedPhotoTypes)
        val file = MultipartBody.Part.createFormData("file", "evidence", bytes.toRequestBody(mimeType.toMediaType()))
        return sessions.authorized { api.photo(it, contract.id, file, "photo".toRequestBody()) }.checked(201).data
            .toDomain().verified(contract.id)
    }

    override suspend fun tournaments(status: String?, game: String?, cursor: String?): TournamentPage {
        require(status == null || status in listOf("open", "in_progress", "finished", "cancelled"))
        require(game == null || game.length <= 60)
        val body = sessions.authorized(readOnly = true) { api.tournaments(it, status, game, cursor) }.checked(200)
        val meta = body.meta ?: throw ApiFailure(0, "INVALID_RESPONSE")
        if (meta.hasMore && (meta.nextCursor.isNullOrBlank() || meta.nextCursor == cursor)) {
            throw ApiFailure(0, "INVALID_RESPONSE")
        }
        return TournamentPage(body.data.map { it.toDomain() }, meta.hasMore, meta.nextCursor)
    }

    private fun Evidence.verified(id: String): Evidence = also {
        if (contractId != id) throw ApiFailure(0, "INVALID_RESPONSE")
    }

    companion object {
        const val MaxPhotoBytes = 5 * 1024 * 1024
        val AllowedPhotoTypes = setOf("image/jpeg", "image/png", "image/webp")
    }
}
