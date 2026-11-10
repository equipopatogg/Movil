package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.feature.connected.domain.Tournament
import kotlinx.coroutines.flow.update

fun ConnectedViewModel.groupName(value: String) {
    if (!mutable.value.busy) mutable.update { it.copy(groupName = value) }
}

fun ConnectedViewModel.draft(value: ChallengeDraft) {
    if (!mutable.value.busy) mutable.update { it.copy(draft = value, reviewing = false, error = null) }
}

fun ConnectedViewModel.deadline(value: String) {
    if (!mutable.value.busy) mutable.update { it.copy(deadlineLocal = value, reviewing = false,
        draft = it.draft.copy(deadline = InputRules.localDeadline(value).orEmpty()), error = null) }
}

fun ConnectedViewModel.link(value: String) {
    if (!mutable.value.busy) mutable.update { it.copy(link = value, error = null) }
}

fun ConnectedViewModel.filter(status: String?, game: String) {
    if (mutable.value.busy) return
    queryVersion++
    mutable.update { it.copy(status = status, gameFilter = game.take(60), tournaments = emptyList(),
        tournamentsLoaded = false, nextCursor = null, hasMore = false, selectedTournament = null) }
}

fun ConnectedViewModel.tournament(value: Tournament?) {
    mutable.update { it.copy(selectedTournament = value, error = null, diagnostic = null, notice = null) }
}

fun ConnectedViewModel.tournamentScope(groupOnly: Boolean) {
    if (mutable.value.busy || groupOnly && mutable.value.group == null) return
    mutable.update { it.copy(groupTournamentsOnly = groupOnly) }
    filter(mutable.value.status, mutable.value.gameFilter)
    loadTournaments()
}


fun ConnectedViewModel.review() {
    val current = mutable.value
    if (current.busy) return
    if (current.group == null || !InputRules.challenge(current.group, current.draft, System.currentTimeMillis())) {
        mutable.update { it.copy(error = "Completa el reto, una fecha futura válida y una fianza del grupo.") }
    } else mutable.update { it.copy(reviewing = true, error = null) }
}
