package com.pactogg.app.feature.community.data

import com.pactogg.app.feature.community.domain.CommitmentModel
import com.pactogg.app.feature.community.domain.CommitmentPartyModel
import com.pactogg.app.feature.community.domain.FameEntryModel
import com.pactogg.app.feature.community.domain.FameExecutorModel
import com.pactogg.app.feature.community.domain.MyPointsModel
import com.pactogg.app.feature.community.domain.NotificationModel
import com.pactogg.app.feature.community.domain.PointsMovementModel
import com.pactogg.app.feature.community.domain.RankingEntryModel
import com.pactogg.app.feature.community.domain.ReadAllModel

fun PointsMovementResponse.toDomain() = PointsMovementModel(
    id,
    type,
    amount,
    balanceAfter,
    refType,
    refId,
    createdAt
)

fun MyPointsResponse.toDomain() = MyPointsModel(
    groupId,
    balance,
    movements.map { it.toDomain() }
)

fun RankingEntryResponse.toDomain() = RankingEntryModel(
    position,
    userId,
    nickname,
    avatarId,
    pointsBalance
)

fun CommitmentPartyResponse.toDomain() = CommitmentPartyModel(
    userId,
    nickname
)

fun CommitmentResponse.toDomain() = CommitmentModel(
    id,
    groupId,
    contractId,
    debtor.toDomain(),
    creditor.toDomain(),
    description,
    game,
    status,
    requestedAt,
    completedAt,
    createdAt
)

fun NotificationResponse.toDomain() = NotificationModel(
    id,
    type,
    title,
    body,
    groupId,
    contractId,
    commitmentId,
    read,
    readAt,
    createdAt
)

fun ReadAllResponse.toDomain() = ReadAllModel(
    updated
)

fun FameExecutorResponse.toDomain() = FameExecutorModel(
    userId,
    nickname,
    avatarId
)

fun FameEntryResponse.toDomain() = FameEntryModel(
    contractId,
    executor?.toDomain(),
    game,
    condition,
    reward,
    resolvedAt,
    photos,
    links
)
