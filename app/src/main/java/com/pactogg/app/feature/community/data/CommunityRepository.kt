package com.pactogg.app.feature.community.data

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.checked
import com.pactogg.app.core.session.SessionManager
import javax.inject.Inject

/** Consumer boundary: server owns authorization, consensus and point transfers. */
class CommunityRepository @Inject constructor(private val api: PactoApi, private val sessions: SessionManager) {
    val notices = NotificationRepository(api, sessions)

    suspend fun pending(cursor: String? = null) = sessions.authorized(readOnly = true) { api.pendingVotes(it,
        cursor) }.checked(200)
    suspend fun summary(id: String): VoteSummaryResponse {
        validId(id)
        return sessions.authorized(readOnly = true) { api.voteSummary(it, id) }.checked(200).data.also { same(
            id, it.contractId) }
    }
    suspend fun vote(id: String, vote: String, comment: String?): CastVoteResponse {
        validId(id)
        require(vote in setOf("legit", "cheat") && (comment == null || comment.length <= 140))
        val operationKey = java.util.UUID.randomUUID().toString()
        return sessions.authorized { api.castVote(it, id, CastVoteRequest(vote, comment), operationKey) }
            .checked(200).data.also {
            same(id, it.contractId)
            if (it.vote != vote) throw ApiFailure(0, "INVALID_RESPONSE")
        }
    }
    suspend fun resolve(id: String, outcome: String): ContractResultResponse {
        validId(id)
        require(outcome in setOf("fulfilled", "cheating"))
        val operationKey = java.util.UUID.randomUUID().toString()
        return sessions.authorized { api.resolveVotes(it, id, ResolveVotingRequest(outcome), operationKey) }
            .checked(200).data.also {
            same(id, it.contractId)
            if (it.outcome != outcome) throw ApiFailure(0, "INVALID_RESPONSE")
        }
    }
    suspend fun result(id: String): ContractResultResponse {
        validId(id)
        return sessions.authorized(readOnly = true) { api.contractResult(it, id) }.checked(200).data.also {
            same(id, it.contractId) }
    }
    suspend fun points(gid: String, cursor: String? = null): com.pactogg.app.core.network.Envelope<MyPointsResponse> {
        validId(gid)
        return sessions.authorized(readOnly = true) { api.myPoints(it, gid, cursor) }.checked(200).also {
            same(gid, it.data.groupId) }
    }
    suspend fun ranking(gid: String): List<RankingEntryResponse> {
        validId(gid)
        return sessions.authorized(readOnly = true) { api.pointsRanking(it, gid) }.checked(200).data
    }
    suspend fun commitments(gid: String, cursor: String? =
        null): com.pactogg.app.core.network.Envelope<List<CommitmentResponse>> {
        validId(gid)
        return sessions.authorized(readOnly = true) { api.commitments(it, gid, cursor) }.checked(200).also {
            page -> page.data.forEach { same(gid, it.groupId) } }
    }
    suspend fun complete(id: String, request: Boolean): CommitmentResponse {
        validId(id)
        return sessions.authorized { if (request) api.requestCompletion(it, id) else api.completeCommitment(
            it, id) }.checked(200).data.also { same(id, it.id) }
    }
    suspend fun fame(gid: String, cursor: String? =
        null): com.pactogg.app.core.network.Envelope<List<FameEntryResponse>> {
        validId(gid)
        return sessions.authorized(readOnly = true) { api.fame(it, gid, cursor) }.checked(200)
    }
}

internal fun validId(id: String) { require(id.matches(Regex("[a-fA-F0-9]{24}"))) }
internal fun same(expected: String, actual: String) { if (expected != actual) throw ApiFailure(0, "INVALID_RESPONSE") }
