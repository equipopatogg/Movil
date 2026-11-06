package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.community.data.toDomain
import kotlinx.coroutines.flow.update

fun ConnectedViewModel.communityComment(value: String) {
    if (!mutable.value.busy) mutable.update { it.copy(community = it.community.copy(comment = value.take(140))) }
}
fun ConnectedViewModel.requestCommunityAction(action: CommunityAction?) {
    if (!mutable.value.busy) mutable.update { it.copy(community = it.community.copy(confirmation = action)) }
}
fun ConnectedViewModel.confirmCommunityAction() {
    val before = mutable.value
    val action = before.community.confirmation
    val repository = community
    if (before.busy || action == null || repository == null) return
    if (!communityActionAllowed(before, action)) return
    val version = queryVersion
    mutable.update { it.copy(community = it.community.copy(confirmation = null, loaded = false)) }
    perform {
        val updated = repository.executeCommunityAction(before, action)
        if (version == queryVersion && sameCommunityContext(before, mutable.value)) mutable.update { it.copy(
            community = updated.copy(confirmation = null), notice =
                "Acción confirmada. Actualiza para consultar el estado reciente.") }
    }
}


private suspend fun com.pactogg.app.feature.community.data.CommunityRepository.executeCommunityAction(
    before: ConnectedState, action: CommunityAction
): CommunityState {
    val repository = this
    return when (action.kind) {
        "vote" -> {
            repository.vote(action.id, action.value, before.community.comment.ifBlank { null })
            before.community.copy(summary = null, loaded = false, comment = "")
        }
        "resolve" -> before.community.copy(result = repository.resolve(action.id, action.value).toDomain(),
            summary = null, loaded = false)
        "complete", "request" -> {
            val item = repository.complete(action.id, action.kind == "request").toDomain()
            require(item.groupId == before.group?.id)
            before.community.copy(commitments = before.community.commitments.map { if (
                it.id == item.id) item else it })
        }
        "read" -> {
            val item = repository.notices.read(action.id).toDomain()
            before.community.copy(notifications = before.community.notifications.map { if (
                it.id == item.id) item else it })
        }
        "readAll" -> {
            repository.notices.readAll()
            before.community.copy(notifications = emptyList(), loaded = false)
        }
        else -> before.community
    }
}
