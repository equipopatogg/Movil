package com.pactogg.app.feature.community.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CastVoteRequest(
    @SerialName("vote") val vote: String,
    @SerialName("comment") val comment: String? = null
)

@Serializable
data class ResolveVotingRequest(
    @SerialName("outcome") val outcome: String
)

@Serializable
data class CastVoteResponse(
    @SerialName("contract_id") val contractId: String,
    @SerialName("vote") val vote: String,
    @SerialName("status") val status: String,
    @SerialName("votes_cast") val votesCast: Int,
    @SerialName("jurors_assigned") val jurorsAssigned: Int,
    @SerialName("needs_admin") val needsAdmin: Boolean
)

@Serializable
data class VoteSummaryResponse(
    @SerialName("contract_id") val contractId: String,
    @SerialName("status") val status: String,
    @SerialName("jurors_assigned") val jurorsAssigned: Int,
    @SerialName("votes_cast") val votesCast: Int,
    @SerialName("votes_remaining") val votesRemaining: Int,
    @SerialName("voting_ends_at") val votingEndsAt: String? = null,
    @SerialName("i_am_juror") val iAmJuror: Boolean,
    @SerialName("i_voted") val iVoted: Boolean,
    @SerialName("admin_can_resolve") val adminCanResolve: Boolean,
    @SerialName("legit_votes") val legitVotes: Int? = null,
    @SerialName("cheat_votes") val cheatVotes: Int? = null
)

@Serializable
data class RevealedVoteResponse(
    @SerialName("juror_id") val jurorId: String = "",
    @SerialName("nickname") val nickname: String = "",
    @SerialName("vote") val vote: String,
    @SerialName("comment") val comment: String? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class ContractResultResponse(
    @SerialName("contract_id") val contractId: String,
    @SerialName("outcome") val outcome: String,
    @SerialName("resolved_by") val resolvedBy: String? = null,
    @SerialName("resolved_at") val resolvedAt: String? = null,
    @SerialName("legit_votes") val legitVotes: Int,
    @SerialName("cheat_votes") val cheatVotes: Int,
    @SerialName("bond_required") val bondRequired: Int,
    @SerialName("bond_movement") val bondMovement: String,
    @SerialName("challenger_id") val challengerId: String,
    @SerialName("executor_id") val executorId: String? = null,
    @SerialName("reward") val reward: String,
    @SerialName("votes") val votes: List<RevealedVoteResponse>
)

@Serializable
data class PendingVoteResponse(
    @SerialName("contract") val contract: com.pactogg.app.feature.connected.data.ContractDto,
    @SerialName("voting_ends_at") val votingEndsAt: String? = null
)

@Serializable
data class PointsMovementResponse(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("amount") val amount: Int,
    @SerialName("balance_after") val balanceAfter: Int,
    @SerialName("ref_type") val refType: String,
    @SerialName("ref_id") val refId: String,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class MyPointsResponse(
    @SerialName("group_id") val groupId: String,
    @SerialName("balance") val balance: Int,
    @SerialName("movements") val movements: List<PointsMovementResponse>
)

@Serializable
data class RankingEntryResponse(
    @SerialName("position") val position: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("avatar_id") val avatarId: String,
    @SerialName("points_balance") val pointsBalance: Int
)

@Serializable
data class CommitmentPartyResponse(
    @SerialName("user_id") val userId: String,
    @SerialName("nickname") val nickname: String
)

@Serializable
data class CommitmentResponse(
    @SerialName("id") val id: String,
    @SerialName("group_id") val groupId: String,
    @SerialName("contract_id") val contractId: String,
    @SerialName("debtor") val debtor: CommitmentPartyResponse,
    @SerialName("creditor") val creditor: CommitmentPartyResponse,
    @SerialName("description") val description: String,
    @SerialName("game") val game: String,
    @SerialName("status") val status: String,
    @SerialName("requested_at") val requestedAt: String? = null,
    @SerialName("completed_at") val completedAt: String? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class NotificationResponse(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
    @SerialName("group_id") val groupId: String? = null,
    @SerialName("contract_id") val contractId: String? = null,
    @SerialName("commitment_id") val commitmentId: String? = null,
    @SerialName("read") val read: Boolean,
    @SerialName("read_at") val readAt: String? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class ReadAllResponse(
    @SerialName("updated") val updated: Long
)

@Serializable
data class FameExecutorResponse(
    @SerialName("user_id") val userId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("avatar_id") val avatarId: String
)

@Serializable
data class FameEntryResponse(
    @SerialName("contract_id") val contractId: String,
    @SerialName("executor") val executor: FameExecutorResponse? = null,
    @SerialName("game") val game: String,
    @SerialName("condition") val condition: String,
    @SerialName("reward") val reward: String,
    @SerialName("resolved_at") val resolvedAt: String? = null,
    @SerialName("photos") val photos: List<String>,
    @SerialName("links") val links: List<String>
)
