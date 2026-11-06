package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.community.domain.CommitmentModel
import com.pactogg.app.feature.community.domain.VoteSummaryModel

internal fun communityActionAllowed(state: ConnectedState, action: CommunityAction): Boolean {
    if (!state.community.loaded || state.account == null) return false
    val summary = state.community.summary
    return when (action.kind) {
        "vote" -> canCastVote(summary, action)
        "resolve" -> canResolveVote(summary, action)
        "complete", "request" -> state.community.commitments.any {
            canCompleteCommitment(it, state, action)
        }
        "read" -> state.community.notifications.any { it.id == action.id && !it.read }
        "readAll" -> state.community.notifications.any { !it.read }
        else -> false
    }
}

internal fun canCastVote(summary: VoteSummaryModel?, action: CommunityAction): Boolean {
    if (summary?.contractId != action.id || summary.status != "voting") return false
    return summary.iAmJuror && !summary.iVoted && action.value in setOf("legit", "cheat")
}

internal fun canResolveVote(summary: VoteSummaryModel?, action: CommunityAction): Boolean {
    if (summary?.contractId != action.id || summary.status != "voting") return false
    return summary.adminCanResolve && action.value in setOf("fulfilled", "cheating")
}

private fun canCompleteCommitment(item: CommitmentModel, state: ConnectedState, action: CommunityAction): Boolean {
    if (item.id != action.id || item.groupId != state.group?.id || item.status == "completed") return false
    return if (action.kind == "complete") item.creditor.userId == state.account?.id
    else item.debtor.userId == state.account?.id && item.requestedAt == null
}

internal fun sameCommunityContext(before: ConnectedState, current: ConnectedState): Boolean =
    before.account?.id == current.account?.id && before.route == current.route && before.group?.id == current.group?.id
