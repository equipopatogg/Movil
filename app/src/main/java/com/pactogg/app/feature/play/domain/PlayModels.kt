package com.pactogg.app.feature.play.domain

import com.pactogg.app.feature.connected.domain.Challenge

/** Modelos de jurado, avisos, ranking, torneos y voz. Sin Android ni Retrofit. */
data class PendingVote(val contract: Challenge, val votingEndsAt: String?)

data class VoteSummary(val contractId: String, val status: String, val jurorsAssigned: Int, val votesCast: Int,
    val votesRemaining: Int, val votingEndsAt: String?, val iAmJuror: Boolean, val iVoted: Boolean,
    val adminCanResolve: Boolean, val votingExtended: Boolean, val legitVotes: Int?, val cheatVotes: Int?)

data class VoteResult(val contractId: String, val outcome: String, val legitVotes: Int, val cheatVotes: Int,
    val bond: Int, val bondMovement: String, val reward: String, val votes: List<Pair<String, String?>>)

data class Notice(val id: String, val type: String, val title: String, val body: String, val groupId: String?,
    val contractId: String?, val read: Boolean, val createdAt: String)

data class NoticePage(val items: List<Notice>, val unread: Int, val hasMore: Boolean, val nextCursor: String?)

data class RankingEntry(val position: Int, val userId: String, val nickname: String, val points: Int)

data class Ranking(val king: RankingEntry?, val items: List<RankingEntry>)

data class Commitment(val id: String, val groupId: String, val debtorId: String, val debtor: String,
    val creditorId: String, val creditor: String, val description: String, val game: String, val status: String,
    val requestedAt: String?)

data class FameEntry(val contractId: String, val executor: String?, val game: String, val condition: String,
    val reward: String, val photos: List<String>, val links: List<String>)

data class ShameEntry(val contractId: String, val label: String, val executor: String?, val game: String,
    val condition: String)

data class TournamentGroup(val groupId: String, val name: String, val isOrganizer: Boolean)

data class PlayMatch(val id: String, val tournamentId: String, val round: Int, val position: Int, val p1: String?,
    val p2: String?, val p1Score: Int?, val p2Score: Int?, val winner: String?, val status: String,
    val reportedBy: String?, val reportedAt: String?, val disputeReason: String?, val resolution: String?,
    val screenshotUrl: String?)

data class TournamentDetail(val id: String, val groupId: String, val organizerId: String, val game: String,
    val format: String, val maxGroups: Int, val entryPoints: Int, val prizePool: Int, val scheduledAt: String,
    val participants: List<String>, val groups: List<TournamentGroup>, val status: String, val winner: String?,
    val code: String?, val cancelReason: String?, val rounds: Int, val matches: List<PlayMatch>) {
    fun groupName(id: String?): String = when (id) {
        null -> "Por definir"
        else -> groups.firstOrNull { it.groupId == id }?.name?.takeIf { it.isNotBlank() } ?: "Grupo"
    }
}

data class NextMatch(val match: PlayMatch, val myGroupId: String, val opponentGroupId: String?, val game: String,
    val scheduledAt: String)

data class VoiceMember(val id: String, val nickname: String)

data class VoiceDraft(val targetId: String?, val targetNickname: String?, val game: String, val condition: String,
    val reward: String, val missingFields: List<String>, val rivalUnmatched: Boolean, val members: List<VoiceMember>)

data class NewTournament(val game: String, val maxGroups: Int, val entryPoints: Int, val scheduledAt: String)

@Suppress("TooManyFunctions")
interface PlayRepository {
    suspend fun pendingVotes(): List<PendingVote>
    suspend fun voteSummary(contractId: String): VoteSummary
    suspend fun castVote(contractId: String, vote: String, comment: String?, idempotencyKey: String): VoteSummary
    suspend fun voteResult(contractId: String): VoteResult
    suspend fun resolveVoting(contractId: String, outcome: String, idempotencyKey: String): VoteResult

    suspend fun notices(unreadOnly: Boolean, cursor: String?): NoticePage
    suspend fun markRead(id: String)
    suspend fun markAllRead()
    suspend fun deleteNotice(id: String)

    suspend fun ranking(groupId: String): Ranking
    suspend fun commitments(groupId: String, iOwe: Boolean): List<Commitment>
    suspend fun commitmentSummary(groupId: String): Pair<Int, Int>
    suspend fun completeCommitment(id: String)
    suspend fun requestCompletion(id: String)
    suspend fun fame(groupId: String): List<FameEntry>
    suspend fun shame(groupId: String): List<ShameEntry>

    suspend fun createTournament(groupId: String, input: NewTournament): TournamentDetail
    suspend fun tournament(id: String): TournamentDetail
    suspend fun joinTournament(codeOrId: String, groupId: String, idempotencyKey: String): TournamentDetail
    suspend fun leaveTournament(id: String, groupId: String)
    suspend fun startTournament(id: String): TournamentDetail
    suspend fun match(id: String): PlayMatch
    suspend fun nextMatch(): NextMatch?
    suspend fun reportMatch(id: String, p1Score: Int, p2Score: Int, groupId: String?, photo: ByteArray,
        mimeType: String): PlayMatch
    suspend fun confirmMatch(id: String): PlayMatch
    suspend fun disputeMatch(id: String, reason: String): PlayMatch
    suspend fun resolveMatch(id: String, winnerGroupId: String, reason: String?): PlayMatch

    suspend fun parsePhrase(groupId: String, phrase: String): VoiceDraft
}
