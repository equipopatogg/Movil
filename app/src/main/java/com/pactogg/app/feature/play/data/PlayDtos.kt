package com.pactogg.app.feature.play.data

import com.pactogg.app.feature.connected.data.ContractDto
import com.pactogg.app.feature.play.domain.Commitment
import com.pactogg.app.feature.play.domain.FameEntry
import com.pactogg.app.feature.play.domain.NextMatch
import com.pactogg.app.feature.play.domain.Notice
import com.pactogg.app.feature.play.domain.PendingVote
import com.pactogg.app.feature.play.domain.PlayMatch
import com.pactogg.app.feature.play.domain.Ranking
import com.pactogg.app.feature.play.domain.RankingEntry
import com.pactogg.app.feature.play.domain.ShameEntry
import com.pactogg.app.feature.play.domain.TournamentDetail
import com.pactogg.app.feature.play.domain.TournamentGroup
import com.pactogg.app.feature.play.domain.VoteResult
import com.pactogg.app.feature.play.domain.VoteSummary
import com.pactogg.app.feature.play.domain.VoiceDraft
import com.pactogg.app.feature.play.domain.VoiceMember
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Envoltura de las respuestas de la API con el meta que usan avisos y listas paginadas. */
@Serializable
class PlayEnvelope<T>(val data: T, val meta: PlayMeta? = null)

/** `GET /me/matches/next` responde `{"data": null}` cuando no hay partida. */
@Serializable
class NextMatchEnvelope(val data: NextMatchDto? = null)

@Serializable
data class PlayMeta(
    @SerialName("has_more") val hasMore: Boolean = false,
    @SerialName("next_cursor") val nextCursor: String? = null,
    @SerialName("unread_count") val unreadCount: Int? = null,
)

// ---------------------------------------------------------------- Jurado (VOT)

@Serializable
data class PendingVoteDto(val contract: ContractDto, @SerialName("voting_ends_at") val votingEndsAt: String? = null) {
    fun toDomain() = PendingVote(contract.toDomain(), votingEndsAt)
}

@Serializable
data class CastVoteRequest(val vote: String, val comment: String? = null)

@Serializable
data class ResolveVotingRequest(val outcome: String)

@Serializable
data class CastVoteDto(
    @SerialName("contract_id") val contractId: String,
    val vote: String,
    val status: String,
    @SerialName("votes_cast") val votesCast: Int,
    @SerialName("jurors_assigned") val jurorsAssigned: Int,
    @SerialName("needs_admin") val needsAdmin: Boolean = false,
)

@Serializable
data class VoteSummaryDto(
    @SerialName("contract_id") val contractId: String,
    val status: String,
    @SerialName("jurors_assigned") val jurorsAssigned: Int = 0,
    @SerialName("votes_cast") val votesCast: Int = 0,
    @SerialName("votes_remaining") val votesRemaining: Int = 0,
    @SerialName("voting_ends_at") val votingEndsAt: String? = null,
    @SerialName("i_am_juror") val iAmJuror: Boolean = false,
    @SerialName("i_voted") val iVoted: Boolean = false,
    @SerialName("admin_can_resolve") val adminCanResolve: Boolean = false,
    @SerialName("voting_extended") val votingExtended: Boolean = false,
    @SerialName("legit_votes") val legitVotes: Int? = null,
    @SerialName("cheat_votes") val cheatVotes: Int? = null,
) {
    fun toDomain() = VoteSummary(contractId, status, jurorsAssigned, votesCast, votesRemaining, votingEndsAt,
        iAmJuror, iVoted, adminCanResolve, votingExtended, legitVotes, cheatVotes)
}

@Serializable
data class RevealedVoteDto(val vote: String, val comment: String? = null)

@Serializable
data class VoteResultDto(
    @SerialName("contract_id") val contractId: String,
    val outcome: String,
    @SerialName("legit_votes") val legitVotes: Int = 0,
    @SerialName("cheat_votes") val cheatVotes: Int = 0,
    @SerialName("bond_required") val bond: Int = 0,
    @SerialName("bond_movement") val bondMovement: String = "none",
    val reward: String = "",
    val votes: List<RevealedVoteDto> = emptyList(),
) {
    fun toDomain() = VoteResult(contractId, outcome, legitVotes, cheatVotes, bond, bondMovement, reward,
        votes.map { it.vote to it.comment })
}

// ---------------------------------------------------------------- Avisos (NOT)

@Serializable
data class NoticeDto(
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    @SerialName("group_id") val groupId: String? = null,
    @SerialName("contract_id") val contractId: String? = null,
    val read: Boolean = false,
    @SerialName("created_at") val createdAt: String = "",
) {
    fun toDomain() = Notice(id, type, title, body, groupId, contractId, read, createdAt)
}

// ---------------------------------------------------------------- Palabra y ranking

@Serializable
data class RankingEntryDto(
    val position: Int,
    @SerialName("user_id") val userId: String,
    val nickname: String,
    @SerialName("points_balance") val points: Int = 0,
) {
    fun toDomain() = RankingEntry(position, userId, nickname, points)
}

@Serializable
data class RankingDto(val king: RankingEntryDto? = null, val items: List<RankingEntryDto> = emptyList()) {
    fun toDomain() = Ranking(king?.toDomain(), items.map { it.toDomain() })
}

@Serializable
data class PartyDto(@SerialName("user_id") val userId: String, val nickname: String = "")

@Serializable
data class CommitmentDto(
    val id: String,
    @SerialName("group_id") val groupId: String,
    val debtor: PartyDto,
    val creditor: PartyDto,
    val description: String,
    val game: String = "",
    val status: String,
    @SerialName("requested_at") val requestedAt: String? = null,
) {
    fun toDomain() = Commitment(id, groupId, debtor.userId, debtor.nickname, creditor.userId, creditor.nickname,
        description, game, status, requestedAt)
}

@Serializable
data class CommitmentSummaryDto(val debo: Int = 0, @SerialName("me_deben") val meDeben: Int = 0)

@Serializable
data class PersonDto(@SerialName("user_id") val userId: String = "", val nickname: String = "")

@Serializable
data class FameDto(
    @SerialName("contract_id") val contractId: String,
    val executor: PersonDto? = null,
    val game: String = "",
    val condition: String = "",
    val reward: String = "",
    val photos: List<String> = emptyList(),
    val links: List<String> = emptyList(),
) {
    fun toDomain() = FameEntry(contractId, executor?.nickname, game, condition, reward, photos, links)
}

@Serializable
data class ShameDto(
    @SerialName("contract_id") val contractId: String,
    val label: String = "",
    val executor: PersonDto? = null,
    val game: String = "",
    val condition: String = "",
) {
    fun toDomain() = ShameEntry(contractId, label, executor?.nickname, game, condition)
}

// ---------------------------------------------------------------- Torneos (TOR / MAT)

@Serializable
data class CreateTournamentRequest(
    val game: String,
    val format: String = "single_elimination",
    @SerialName("max_groups") val maxGroups: Int,
    @SerialName("entry_points") val entryPoints: Int,
    @SerialName("scheduled_at") val scheduledAt: String,
)

@Serializable
data class JoinTournamentRequest(@SerialName("group_id") val groupId: String, val code: String? = null)

@Serializable
data class DisputeRequest(val reason: String)

@Serializable
data class ResolveMatchRequest(@SerialName("winner_group_id") val winnerGroupId: String, val reason: String? = null)

@Serializable
data class PlayMatchDto(
    val id: String,
    @SerialName("tournament_id") val tournamentId: String,
    val round: Int,
    val position: Int,
    @SerialName("p1_group_id") val p1: String? = null,
    @SerialName("p2_group_id") val p2: String? = null,
    @SerialName("p1_score") val p1Score: Int? = null,
    @SerialName("p2_score") val p2Score: Int? = null,
    @SerialName("winner_group_id") val winner: String? = null,
    val status: String,
    @SerialName("reported_by_group_id") val reportedBy: String? = null,
    @SerialName("reported_at") val reportedAt: String? = null,
    @SerialName("dispute_reason") val disputeReason: String? = null,
    val resolution: String? = null,
    @SerialName("screenshot_url") val screenshotUrl: String? = null,
) {
    fun toDomain() = PlayMatch(id, tournamentId, round, position, p1, p2, p1Score, p2Score, winner, status,
        reportedBy, reportedAt, disputeReason, resolution, screenshotUrl)
}

@Serializable
data class BracketDto(val size: Int = 0, val rounds: Int = 0, val matches: List<PlayMatchDto> = emptyList())

@Serializable
data class TournamentGroupDto(
    @SerialName("group_id") val groupId: String,
    val name: String = "",
    @SerialName("is_organizer") val isOrganizer: Boolean = false,
)

@Serializable
data class TournamentDetailDto(
    val id: String,
    @SerialName("group_id") val groupId: String,
    @SerialName("organizer_id") val organizerId: String,
    val game: String,
    val format: String = "",
    @SerialName("max_groups") val maxGroups: Int = 0,
    @SerialName("entry_points") val entryPoints: Int = 0,
    @SerialName("prize_pool") val prizePool: Int = 0,
    @SerialName("scheduled_at") val scheduledAt: String = "",
    val participants: List<String> = emptyList(),
    val groups: List<TournamentGroupDto>? = null,
    val status: String,
    @SerialName("winner_group_id") val winner: String? = null,
    val code: String? = null,
    @SerialName("cancel_reason") val cancelReason: String? = null,
    val bracket: BracketDto = BracketDto(),
) {
    fun toDomain() = TournamentDetail(id, groupId, organizerId, game, format, maxGroups, entryPoints, prizePool,
        scheduledAt, participants, groups.orEmpty().map { TournamentGroup(it.groupId, it.name, it.isOrganizer) },
        status, winner, code, cancelReason, bracket.rounds, bracket.matches.map { it.toDomain() })
}

@Serializable
data class NextMatchDto(
    val match: PlayMatchDto,
    @SerialName("my_group_id") val myGroupId: String,
    @SerialName("opponent_group_id") val opponentGroupId: String? = null,
    val game: String = "",
    @SerialName("scheduled_at") val scheduledAt: String = "",
) {
    fun toDomain() = NextMatch(match.toDomain(), myGroupId, opponentGroupId, game, scheduledAt)
}

// ---------------------------------------------------------------- Voz (CON-08)

@Serializable
data class ParseRequest(val phrase: String)

@Serializable
data class VoiceMemberDto(val id: String, val nickname: String)

@Serializable
data class VoiceDraftDto(
    @SerialName("target_type") val targetType: String = "group",
    @SerialName("target_id") val targetId: String? = null,
    @SerialName("target_nickname") val targetNickname: String? = null,
    val game: String = "",
    val condition: String = "",
    val reward: String = "",
    @SerialName("missing_fields") val missingFields: List<String> = emptyList(),
    @SerialName("rival_unmatched") val rivalUnmatched: Boolean = false,
    val members: List<VoiceMemberDto>? = null,
    val cached: Boolean = false,
) {
    fun toDomain() = VoiceDraft(targetId.takeIf { targetType == "member" }, targetNickname, game, condition, reward,
        missingFields, rivalUnmatched, members.orEmpty().map { VoiceMember(it.id, it.nickname) })
}
