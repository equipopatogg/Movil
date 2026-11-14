package com.pactogg.app.feature.play.data

import com.pactogg.app.core.network.checked
import com.pactogg.app.core.network.failure
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.connected.data.ApiProductRepository
import com.pactogg.app.feature.play.domain.Commitment
import com.pactogg.app.feature.play.domain.FameEntry
import com.pactogg.app.feature.play.domain.NewTournament
import com.pactogg.app.feature.play.domain.NextMatch
import com.pactogg.app.feature.play.domain.NoticePage
import com.pactogg.app.feature.play.domain.PendingVote
import com.pactogg.app.feature.play.domain.PlayMatch
import com.pactogg.app.feature.play.domain.PlayRepository
import com.pactogg.app.feature.play.domain.Ranking
import com.pactogg.app.feature.play.domain.ShameEntry
import com.pactogg.app.feature.play.domain.TournamentDetail
import com.pactogg.app.feature.play.domain.VoiceDraft
import com.pactogg.app.feature.play.domain.VoteResult
import com.pactogg.app.feature.play.domain.VoteSummary
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

private val IdPattern = Regex("[a-f0-9]{24}")
private val Text = "text/plain".toMediaType()

@Suppress("TooManyFunctions")
class ApiPlayRepository(private val api: PlayApi, private val sessions: SessionManager) : PlayRepository {

    private fun checkId(value: String): String = value.also { require(it.matches(IdPattern)) }

    private fun Response<*>.noContent(vararg ok: Int) {
        if (code() !in ok) throw failure()
    }

    // ------------------------------------------------------------ Jurado

    override suspend fun pendingVotes(): List<PendingVote> =
        sessions.authorized(readOnly = true) { api.pendingVotes(it) }.checked(200).data.map { it.toDomain() }

    override suspend fun voteSummary(contractId: String): VoteSummary =
        sessions.authorized(readOnly = true) { api.voteSummary(it, checkId(contractId)) }.checked(200).data.toDomain()

    override suspend fun castVote(contractId: String, vote: String, comment: String?,
        idempotencyKey: String): VoteSummary {
        require(vote == "legit" || vote == "cheat")
        require(comment == null || comment.length <= 140)
        sessions.authorized { api.castVote(it, idempotencyKey, checkId(contractId), CastVoteRequest(vote, comment)) }
            .checked(200)
        return voteSummary(contractId)
    }

    override suspend fun voteResult(contractId: String): VoteResult =
        sessions.authorized(readOnly = true) { api.voteResult(it, checkId(contractId)) }.checked(200).data.toDomain()

    override suspend fun resolveVoting(contractId: String, outcome: String, idempotencyKey: String): VoteResult {
        require(outcome == "fulfilled" || outcome == "cheating")
        return sessions.authorized { api.resolveVoting(it, idempotencyKey, checkId(contractId),
            ResolveVotingRequest(outcome)) }
            .checked(200).data.toDomain()
    }

    // ------------------------------------------------------------ Avisos

    override suspend fun notices(unreadOnly: Boolean, cursor: String?): NoticePage {
        val body = sessions.authorized(readOnly = true) { api.notices(it, unreadOnly.takeIf { u -> u }, cursor) }
            .checked(200)
        val meta = body.meta
        return NoticePage(body.data.map { it.toDomain() }, meta?.unreadCount ?: body.data.count { !it.read },
            meta?.hasMore ?: false, meta?.nextCursor)
    }

    override suspend fun markRead(id: String) {
        sessions.authorized { api.markRead(it, checkId(id)) }.checked(200)
    }

    override suspend fun markAllRead() {
        sessions.authorized { api.markAllRead(it) }.noContent(200, 204)
    }

    override suspend fun deleteNotice(id: String) {
        sessions.authorized { api.deleteNotice(it, checkId(id)) }.noContent(200, 204)
    }

    // ------------------------------------------------------------ Palabra y ranking

    override suspend fun ranking(groupId: String): Ranking =
        sessions.authorized(readOnly = true) { api.ranking(it, checkId(groupId)) }.checked(200).data.toDomain()

    override suspend fun commitments(groupId: String, iOwe: Boolean): List<Commitment> =
        sessions.authorized(readOnly = true) { api.commitments(it, checkId(groupId), if (iOwe) "debo" else "me-deben") }
            .checked(200).data.map { it.toDomain() }

    override suspend fun commitmentSummary(groupId: String): Pair<Int, Int> =
        sessions.authorized(readOnly = true) { api.commitmentSummary(it, checkId(groupId)) }.checked(200).data
            .let { it.debo to it.meDeben }

    override suspend fun completeCommitment(id: String) {
        sessions.authorized { api.completeCommitment(it, checkId(id)) }.checked(200)
    }

    override suspend fun requestCompletion(id: String) {
        sessions.authorized { api.requestCompletion(it, checkId(id)) }.checked(200)
    }

    override suspend fun fame(groupId: String): List<FameEntry> =
        sessions.authorized(readOnly = true) { api.fame(it, checkId(groupId)) }.checked(200).data.map { it.toDomain() }

    override suspend fun shame(groupId: String): List<ShameEntry> =
        sessions.authorized(readOnly = true) { api.shame(it, checkId(groupId)) }.checked(200).data.map { it.toDomain() }

    // ------------------------------------------------------------ Torneos

    override suspend fun createTournament(groupId: String, input: NewTournament): TournamentDetail {
        require(input.game.trim().length in 1..60 && input.maxGroups in 2..16 && input.entryPoints in 0..500)
        val body = CreateTournamentRequest(input.game.trim(), maxGroups = input.maxGroups,
            entryPoints = input.entryPoints, scheduledAt = input.scheduledAt)
        return sessions.authorized { api.createTournament(it, checkId(groupId), body) }.checked(201).data.toDomain()
    }

    override suspend fun tournament(id: String): TournamentDetail =
        sessions.authorized(readOnly = true) { api.tournament(it, checkId(id)) }.checked(200).data.toDomain()

    override suspend fun joinTournament(codeOrId: String, groupId: String, idempotencyKey: String): TournamentDetail {
        val value = codeOrId.trim()
        require(value.length in 4..40 && value.none(Char::isWhitespace))
        val code = value.takeUnless { it.matches(IdPattern) }
        return sessions.authorized {
            api.joinTournament(it, idempotencyKey, value, JoinTournamentRequest(checkId(groupId), code))
        }.checked(200).data.toDomain()
    }

    override suspend fun leaveTournament(id: String, groupId: String) {
        sessions.authorized { api.leaveTournament(it, checkId(id), checkId(groupId)) }.noContent(204)
    }

    override suspend fun startTournament(id: String): TournamentDetail =
        sessions.authorized { api.startTournament(it, checkId(id)) }.checked(200).data.toDomain()

    override suspend fun match(id: String): PlayMatch =
        sessions.authorized(readOnly = true) { api.match(it, checkId(id)) }.checked(200).data.toDomain()

    override suspend fun nextMatch(): NextMatch? =
        sessions.authorized(readOnly = true) { api.nextMatch(it) }.checked(200).data?.toDomain()

    override suspend fun reportMatch(id: String, p1Score: Int, p2Score: Int, groupId: String?, photo: ByteArray,
        mimeType: String): PlayMatch {
        require(p1Score in 0..999 && p2Score in 0..999 && p1Score != p2Score)
        require(photo.size in 1..ApiProductRepository.MaxPhotoBytes)
        require(mimeType in ApiProductRepository.AllowedPhotoTypes)
        val extension = when (mimeType) { "image/png" -> "png"; "image/webp" -> "webp"; else -> "jpg" }
        val file = MultipartBody.Part.createFormData("file", "resultado.$extension",
            photo.toRequestBody(mimeType.toMediaType()))
        return sessions.authorized {
            api.reportMatch(it, checkId(id), file, p1Score.toString().toRequestBody(Text),
                p2Score.toString().toRequestBody(Text),
                groupId?.let { g -> checkId(g).toRequestBody(Text) })
        }.checked(200).data.toDomain()
    }

    override suspend fun confirmMatch(id: String): PlayMatch =
        sessions.authorized { api.confirmMatch(it, checkId(id)) }.checked(200).data.toDomain()

    override suspend fun disputeMatch(id: String, reason: String): PlayMatch {
        require(reason.trim().length in 5..140)
        return sessions.authorized { api.disputeMatch(it, checkId(id),
            DisputeRequest(reason.trim())) }.checked(200).data.toDomain()
    }

    override suspend fun resolveMatch(id: String, winnerGroupId: String, reason: String?): PlayMatch =
        sessions.authorized {
            api.resolveMatch(it, checkId(id), ResolveMatchRequest(checkId(winnerGroupId),
                reason?.trim()?.takeIf(String::isNotEmpty)))
        }.checked(200).data.toDomain()

    // ------------------------------------------------------------ Voz

    override suspend fun parsePhrase(groupId: String, phrase: String): VoiceDraft {
        require(phrase.trim().length in 1..300)
        return sessions.authorized { api.parsePhrase(it, checkId(groupId), ParseRequest(phrase.trim())) }
            .checked(200).data.toDomain()
    }
}

