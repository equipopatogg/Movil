package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.connected.domain.BoardFilter
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.Group
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun ConnectedViewModel.loadProfile() = perform {
    val profile = socials.profile()
    mutable.update { it.copy(social = it.social.copy(profile = profile, editedNickname = profile.account.nickname)) }
}
fun ConnectedViewModel.loadGroups(more: Boolean = false) {
    val current = mutable.value.social
    if (more && !current.groups.hasMore) return
    perform {
        val page = socials.groups(current.groupQuery.trim(), current.groups.cursor.takeIf { more })
        mutable.update { it.copy(social = it.social.copy(
            groups = current.groups.merge(page, more) { item -> item.id })) }
    }
}
/**
 * Elige un grupo. Si otra pantalla mandó a elegirlo (crear reto, torneo, ranking, voz o una idea de Explorar),
 * al terminar regresa a esa pantalla para que no haya que volver a buscarla.
 */
fun ConnectedViewModel.selectGroup(id: String) {
    if (state.value.busy) return
    val play = state.value.play
    val next = play.afterGroup ?: ConnectedRoute.MEMBERS.takeIf { play.pendingIdea != null }
    perform { applyGroup(socials.group(id)) }
    if (next != null) viewModelScope.launch {
        state.first { !it.busy }
        val current = state.value
        if (current.group?.id == id && current.route == ConnectedRoute.GROUPS) navigate(next)
    }
}

internal fun ConnectedViewModel.applyGroup(group: Group) {
    queryVersion++
    mutable.value.selectedPhoto?.clear()
    val idea = mutable.value.play.pendingIdea
    mutable.update { it.copy(play = it.play.copy(pendingIdea = null, afterGroup = null), group = group, contract = null,
        contractStateStale = false,
        evidence = null, evidenceItems = emptyList(), evidenceLoaded = false, evidenceUnavailable = false,
        evidenceBlockedId = null, selectedPhoto = null, link = "",
        draft = com.pactogg.app.feature.connected.domain.ChallengeDraft(game = idea?.first.orEmpty(),
            condition = idea?.second.orEmpty()), reviewing = false,
        social = it.social.copy(selectedRival = null, members = ListState(), contracts = ListState(),
            profile = null, pendingAction = null)) }
}
fun ConnectedViewModel.loadMembers(more: Boolean = false) {
    val current = mutable.value
    val group = current.group ?: return
    if (more && !current.social.members.hasMore) return
    perform {
        val page = socials.members(group.id, current.social.memberQuery.trim(),
            current.social.members.cursor.takeIf { more })
        mutable.update { it.copy(social = it.social.copy(
            members = current.social.members.merge(page, more) { item -> item.id })) }
    }
}
fun ConnectedViewModel.loadBoard(more: Boolean = false) {
    val current = mutable.value
    val group = current.group ?: return
    if (more && !current.social.contracts.hasMore) return
    perform {
        val page = socials.contracts(group.id, current.social.boardFilter,
            current.social.contracts.cursor.takeIf { more })
        mutable.update { it.copy(social = it.social.copy(
            contracts = current.social.contracts.merge(page, more) { item -> item.id })) }
    }
}
fun ConnectedViewModel.openChallenge(contract: Challenge) = perform {
    val fresh = products.challenge(contract)
    val group = socials.group(fresh.groupId)
    mutable.value.selectedPhoto?.clear()
    mutable.update { it.copy(group = group, contract = fresh, contractStateStale = false,
        route = ConnectedRoute.CONTRACT, evidence = null, evidenceItems = emptyList(),
        evidenceLoaded = false, evidenceUnavailable = false, evidenceBlockedId = null, selectedPhoto = null, link = "",
        social = it.social.copy(pendingAction = null)) }
}
fun ConnectedViewModel.groupSearch(value: String) {
    if (!mutable.value.busy) mutable.update { it.copy(social = it.social.copy(
        groupQuery = value.take(40), groups = ListState())) }
}
fun ConnectedViewModel.memberSearch(value: String) {
    if (!mutable.value.busy) mutable.update { it.copy(social = it.social.copy(
        memberQuery = value.take(24), members = ListState())) }
}
fun ConnectedViewModel.boardFilter(value: BoardFilter) {
    if (!mutable.value.busy) mutable.update { it.copy(social = it.social.copy(
        boardFilter = value, contracts = ListState())) }
}
