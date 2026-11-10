package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PageMeta
import com.pactogg.app.feature.community.data.toDomain
import kotlinx.coroutines.flow.update

fun ConnectedViewModel.loadCommunity(more: Boolean = false) {
    val before = mutable.value
    if (before.busy || before.account == null) return
    val repository = community
    if (repository == null || more && !before.community.more) return
    val cursor = before.community.cursor.takeIf { more }
    val version = queryVersion
    val base = if (more) before.community else CommunityState()
    // Old permissions must not remain actionable after a failed refresh.
    mutable.update { it.copy(community = base.copy(loaded = false, confirmation = null)) }
    perform {
        val next = repository.fetchCommunity(before, base, cursor)
        if (version == queryVersion && sameCommunityContext(before, mutable.value)) mutable.update {
            it.copy(community = next)
        }
    }
}

internal fun validateCommunityPage(meta: PageMeta?, cursor: String?, route: ConnectedRoute) {
    val paged = route in setOf(ConnectedRoute.JURY, ConnectedRoute.POINTS,
        ConnectedRoute.COMMITMENTS, ConnectedRoute.NOTIFICATIONS, ConnectedRoute.FAME)
    if (!paged) return
    val page = meta ?: throw ApiFailure(0, "INVALID_RESPONSE")
    val invalidCursor = page.nextCursor.isNullOrBlank() || page.nextCursor == cursor
    if (page.hasMore && invalidCursor) throw ApiFailure(0, "INVALID_RESPONSE")
}

fun ConnectedViewModel.openJuryContract(id: String) {
    val contract = mutable.value.community.pending.find { it.contract.id == id }?.contract ?: return
    val repository = community ?: return
    val version = queryVersion
    perform {
        val group = socials.group(contract.groupId)
        if (group.id != contract.groupId) throw ApiFailure(0, "INVALID_RESPONSE")
        val summary = repository.summary(contract.id).toDomain()
        if (version != queryVersion) return@perform
        applyGroup(group)
        mutable.update { it.copy(contract = contract, route = ConnectedRoute.VOTING,
            community = CommunityState(loaded = true, summary = summary)) }
    }
}

private suspend fun com.pactogg.app.feature.community.data.CommunityRepository.fetchCommunity(
    before: ConnectedState, base: CommunityState, cursor: String?
): CommunityState {
    val repository = this
    var next = base
    var paging: PageMeta? = null
    when (before.route) {
        ConnectedRoute.JURY -> {
            val page = repository.pending(cursor)
            paging = page.meta
            next = base.copy(pending = (base.pending + page.data.map { it.toDomain() })
                .distinctBy { it.contract.id })
        }
        ConnectedRoute.VOTING -> {
            val id = requireNotNull(before.contract) { "Select a contract" }.id
            val summary = repository.summary(id).toDomain()
            val result = if (summary.status in setOf("fulfilled", "cheating"))
                repository.result(id).toDomain() else null
            next = base.copy(summary = summary, result = result)
        }
        ConnectedRoute.POINTS -> {
            val page = repository.points(requireNotNull(before.group).id, cursor)
            paging = page.meta
            val points = page.data.toDomain()
            next = base.copy(points = points.copy(movements =
                (base.points?.movements.orEmpty() + points.movements).distinctBy { it.id }))
        }
        ConnectedRoute.RANKING -> next = base.copy(
            ranking = repository.ranking(requireNotNull(before.group).id).map { it.toDomain() })
        ConnectedRoute.COMMITMENTS -> {
            val page = repository.commitments(requireNotNull(before.group).id, cursor)
            paging = page.meta
            next = base.copy(commitments = (base.commitments + page.data.map { it.toDomain() })
                .distinctBy { it.id })
        }
        ConnectedRoute.NOTIFICATIONS -> {
            val page = repository.notices.list(cursor)
            paging = page.meta
            next = base.copy(notifications = (base.notifications + page.data.map { it.toDomain() })
                .distinctBy { it.id })
        }
        ConnectedRoute.FAME -> {
            val page = repository.fame(requireNotNull(before.group).id, cursor)
            paging = page.meta
            next = base.copy(fame = (base.fame + page.data.map { it.toDomain() })
                .distinctBy { it.contractId })
        }
        else -> return base
    }
    validateCommunityPage(paging, cursor, before.route)
    return next.copy(loaded = true, more = paging?.hasMore == true, cursor = paging?.nextCursor)
}
