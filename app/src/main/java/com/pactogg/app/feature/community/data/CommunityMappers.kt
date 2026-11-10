package com.pactogg.app.feature.community.data

import com.pactogg.app.feature.community.domain.CastVoteModel
import com.pactogg.app.feature.community.domain.ContractResultModel
import com.pactogg.app.feature.community.domain.PendingVoteModel
import com.pactogg.app.feature.community.domain.RevealedVoteModel
import com.pactogg.app.feature.community.domain.VoteSummaryModel

fun CastVoteResponse.toDomain() = CastVoteModel(
    contractId,
    vote,
    status,
    votesCast,
    jurorsAssigned,
    needsAdmin
)

fun VoteSummaryResponse.toDomain() = VoteSummaryModel(
    contractId,
    status,
    jurorsAssigned,
    votesCast,
    votesRemaining,
    votingEndsAt,
    iAmJuror,
    iVoted,
    adminCanResolve,
    legitVotes,
    cheatVotes
)

fun RevealedVoteResponse.toDomain() = RevealedVoteModel(
    "",
    "",
    vote,
    comment,
    createdAt
)

fun ContractResultResponse.toDomain() = ContractResultModel(
    contractId,
    outcome,
    resolvedBy,
    resolvedAt,
    legitVotes,
    cheatVotes,
    bondRequired,
    bondMovement,
    challengerId,
    executorId,
    reward,
    votes.map { it.toDomain() }
)

fun PendingVoteResponse.toDomain() = PendingVoteModel(
    contract.toDomain(),
    votingEndsAt
)
