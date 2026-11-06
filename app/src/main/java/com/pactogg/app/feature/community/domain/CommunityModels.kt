package com.pactogg.app.feature.community.domain

data class CastVoteModel(
    val contractId: String,
    val vote: String,
    val status: String,
    val votesCast: Int,
    val jurorsAssigned: Int,
    val needsAdmin: Boolean
)

data class VoteSummaryModel(
    val contractId: String,
    val status: String,
    val jurorsAssigned: Int,
    val votesCast: Int,
    val votesRemaining: Int,
    val votingEndsAt: String? = null,
    val iAmJuror: Boolean,
    val iVoted: Boolean,
    val adminCanResolve: Boolean,
    val legitVotes: Int? = null,
    val cheatVotes: Int? = null
)

data class RevealedVoteModel(
    val jurorId: String,
    val nickname: String,
    val vote: String,
    val comment: String? = null,
    val createdAt: String
)

data class ContractResultModel(
    val contractId: String,
    val outcome: String,
    val resolvedBy: String? = null,
    val resolvedAt: String? = null,
    val legitVotes: Int,
    val cheatVotes: Int,
    val bondRequired: Int,
    val bondMovement: String,
    val challengerId: String,
    val executorId: String? = null,
    val reward: String,
    val votes: List<RevealedVoteModel>
)

data class PendingVoteModel(
    val contract: com.pactogg.app.feature.connected.domain.Challenge,
    val votingEndsAt: String? = null
)

data class PointsMovementModel(
    val id: String,
    val type: String,
    val amount: Int,
    val balanceAfter: Int,
    val refType: String,
    val refId: String,
    val createdAt: String
)

data class MyPointsModel(
    val groupId: String,
    val balance: Int,
    val movements: List<PointsMovementModel>
)

data class RankingEntryModel(
    val position: Int,
    val userId: String,
    val nickname: String,
    val avatarId: String,
    val pointsBalance: Int
)

data class CommitmentPartyModel(
    val userId: String,
    val nickname: String
)

data class CommitmentModel(
    val id: String,
    val groupId: String,
    val contractId: String,
    val debtor: CommitmentPartyModel,
    val creditor: CommitmentPartyModel,
    val description: String,
    val game: String,
    val status: String,
    val requestedAt: String? = null,
    val completedAt: String? = null,
    val createdAt: String
)

data class NotificationModel(
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    val groupId: String? = null,
    val contractId: String? = null,
    val commitmentId: String? = null,
    val read: Boolean,
    val readAt: String? = null,
    val createdAt: String
)

data class ReadAllModel(
    val updated: Long
)

data class FameExecutorModel(
    val userId: String,
    val nickname: String,
    val avatarId: String
)

data class FameEntryModel(
    val contractId: String,
    val executor: FameExecutorModel? = null,
    val game: String,
    val condition: String,
    val reward: String,
    val resolvedAt: String? = null,
    val photos: List<String>,
    val links: List<String>
)
