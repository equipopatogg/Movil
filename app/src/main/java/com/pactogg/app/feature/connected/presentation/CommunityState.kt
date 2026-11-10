package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.community.domain.CommitmentModel
import com.pactogg.app.feature.community.domain.ContractResultModel
import com.pactogg.app.feature.community.domain.FameEntryModel
import com.pactogg.app.feature.community.domain.MyPointsModel
import com.pactogg.app.feature.community.domain.NotificationModel
import com.pactogg.app.feature.community.domain.PendingVoteModel
import com.pactogg.app.feature.community.domain.RankingEntryModel
import com.pactogg.app.feature.community.domain.VoteSummaryModel

data class CommunityState(
    val pending: List<PendingVoteModel> = emptyList(),
    val summary: VoteSummaryModel? = null,
    val result: ContractResultModel? = null,
    val points: MyPointsModel? = null,
    val ranking: List<RankingEntryModel> = emptyList(),
    val commitments: List<CommitmentModel> = emptyList(),
    val notifications: List<NotificationModel> = emptyList(),
    val fame: List<FameEntryModel> = emptyList(),
    val loaded: Boolean = false,
    val cursor: String? = null,
    val more: Boolean = false,
    val comment: String = "",
    val confirmation: CommunityAction? = null,
)

data class CommunityAction(val kind: String, val id: String = "", val value: String = "")
