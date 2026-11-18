@file:Suppress("TooManyFunctions")

package com.pactogg.app.feature.play.presentation

import androidx.lifecycle.viewModelScope
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.connected.data.PreparedPhoto
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.feature.connected.domain.Member
import com.pactogg.app.feature.connected.presentation.ConnectedRoute
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.ListState
import com.pactogg.app.feature.play.domain.Commitment
import com.pactogg.app.feature.play.domain.TournamentSizes
import com.pactogg.app.feature.play.domain.NewTournament
import com.pactogg.app.feature.play.domain.Notice
import java.util.UUID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val ClosedStatuses = setOf("fulfilled", "cheating", "expired", "cancelled", "rejected")

/** Cargas al entrar a cada pantalla nueva (lo llama ConnectedViewModel.navigate). */
fun onPlayRoute(model: ConnectedViewModel, route: ConnectedRoute) {
    when (route) {
        ConnectedRoute.HOME -> { model.dropPendingIdea(); model.refreshHome() }
        ConnectedRoute.JURY -> model.loadPendingVotes()
        ConnectedRoute.NOTIFICATIONS -> model.loadNotices()
        ConnectedRoute.STANDINGS -> model.loadStandings()
        else -> Unit
    }
}

private fun ConnectedViewModel.updatePlay(change: (PlayState) -> PlayState) =
    mutable.update { it.copy(play = change(it.play)) }

/** Consultas de fondo para el inicio: si fallan no muestran error ni bloquean botones. */
/** Si la persona se va a Inicio sin elegir grupo, la idea pendiente de Explorar se descarta. */
internal fun ConnectedViewModel.dropPendingIdea() {
    val play = state.value.play
    if (play.pendingIdea == null && play.afterGroup == null) return
    updatePlay { it.copy(pendingIdea = null, afterGroup = null) }
}

private fun ConnectedViewModel.quiet(block: suspend () -> Unit) {
    if (state.value.account == null) return
    viewModelScope.launch {
        runCatching { block() }.onFailure { if (it is CancellationException) throw it }
    }
}

fun ConnectedViewModel.refreshHome() {
    quiet { val unread = play.notices(unreadOnly = true,
        cursor = null).unread; updatePlay { it.copy(unread = unread) } }
    quiet { val votes = play.pendingVotes(); updatePlay { it.copy(pendingVotes = votes, votesLoaded = true) } }
    quiet { val next = play.nextMatch(); updatePlay { it.copy(nextMatch = next) } }
}

// ------------------------------------------------------------------ Jurado (PL-01)

fun ConnectedViewModel.loadPendingVotes() = perform {
    val votes = play.pendingVotes()
    updatePlay { it.copy(pendingVotes = votes, votesLoaded = true) }
}

fun ConnectedViewModel.openVote(contract: Challenge) = perform {
    val fresh = products.challenge(contract)
    val summary = play.voteSummary(fresh.id)
    val evidence = runCatching { products.evidence(fresh) }.getOrElse {
        if (it is CancellationException) throw it
        emptyList()
    }
    val result = if (fresh.status in ClosedStatuses) runCatching { play.voteResult(fresh.id) }.getOrNull() else null
    mutable.update { it.copy(route = ConnectedRoute.VOTE, contract = fresh, play = it.play.copy(
        voteContract = fresh, voteSummary = summary, voteEvidence = evidence, voteResult = result,
        voteChoice = null, voteComment = "", voteKey = null, confirmVote = false)) }
}

fun ConnectedViewModel.chooseVote(vote: String) {
    if (!state.value.busy && vote in setOf("legit", "cheat")) updatePlay { it.copy(voteChoice = vote, voteKey = null) }
}

fun ConnectedViewModel.voteComment(value: String) {
    if (!state.value.busy) updatePlay { it.copy(voteComment = value.take(140)) }
}

fun ConnectedViewModel.requestVote(show: Boolean) {
    if (!state.value.busy && state.value.play.voteChoice != null) updatePlay { it.copy(confirmVote = show) }
}

fun ConnectedViewModel.castVote() {
    val current = state.value.play
    val contract = current.voteContract ?: return
    val vote = current.voteChoice ?: return
    // La misma clave se reutiliza si el voto se reintenta; el backend no lo cuenta dos veces.
    val key = current.voteKey ?: UUID.randomUUID().toString()
    updatePlay { it.copy(voteKey = key, confirmVote = false) }
    perform {
        val summary = play.castVote(contract.id, vote, current.voteComment.trim().ifEmpty { null }, key)
        val result = if (summary.status in ClosedStatuses) runCatching { play.voteResult(contract.id) }.getOrNull()
            else null
        mutable.update { it.copy(notice = "Voto registrado. Gracias por revisar.", play = it.play.copy(
            voteSummary = summary, voteResult = result, voteChoice = null, voteComment = "", voteKey = null,
            pendingVotes = it.play.pendingVotes.filterNot { p -> p.contract.id == contract.id })) }
    }
}

fun ConnectedViewModel.resolveVoting(outcome: String) {
    val contract = state.value.play.voteContract ?: return
    val key = UUID.randomUUID().toString()
    perform {
        val result = play.resolveVoting(contract.id, outcome, key)
        val summary = play.voteSummary(contract.id)
        mutable.update { it.copy(notice = "Decisión registrada.", play = it.play.copy(voteResult = result,
            voteSummary = summary)) }
    }
}

// ------------------------------------------------------------------ Avisos (PL-02)

fun ConnectedViewModel.loadNotices(more: Boolean = false) {
    val current = state.value.play
    if (more && !current.noticesMore) return
    perform {
        val page = play.notices(unreadOnly = false, cursor = current.noticesCursor.takeIf { more })
        updatePlay { it.copy(notices = ((if (more) it.notices else emptyList()) + page.items).distinctBy { n -> n.id },
            noticesLoaded = true, unread = page.unread, noticesCursor = page.nextCursor, noticesMore = page.hasMore) }
    }
}

fun ConnectedViewModel.openNotice(notice: Notice) = perform {
    if (!notice.read) play.markRead(notice.id)
    updatePlay { it.copy(unread = (it.unread - if (notice.read) 0 else 1).coerceAtLeast(0),
        notices = it.notices.map { n -> if (n.id == notice.id) n.copy(read = true) else n }) }
    val contractId = notice.contractId
    val groupId = notice.groupId
    if (contractId != null && groupId != null) {
        val stub = Challenge(contractId, groupId, "-", "-", "-", "", 0, "open")
        val fresh = products.challenge(stub)
        val group = socials.group(fresh.groupId)
        mutable.update { it.copy(group = group, contract = fresh, contractStateStale = false,
            route = ConnectedRoute.CONTRACT, evidenceItems = emptyList(), evidenceLoaded = false) }
    }
}

fun ConnectedViewModel.markAllNotices() = perform {
    play.markAllRead()
    updatePlay { it.copy(unread = 0, notices = it.notices.map { n -> n.copy(read = true) }) }
}

fun ConnectedViewModel.deleteNotice(notice: Notice) = perform {
    play.deleteNotice(notice.id)
    updatePlay { it.copy(notices = it.notices.filterNot { n -> n.id == notice.id },
        unread = (it.unread - if (notice.read) 0 else 1).coerceAtLeast(0)) }
}

// ------------------------------------------------------------------ Palabra y ranking (PL-03)

fun ConnectedViewModel.standingsTab(tab: String) {
    if (state.value.busy) return
    updatePlay { it.copy(standingsTab = tab) }
    loadStandings()
}

fun ConnectedViewModel.loadStandings() {
    val group = state.value.group ?: return
    when (state.value.play.standingsTab) {
        "libro" -> perform {
            val owe = play.commitments(group.id, iOwe = true)
            val owed = play.commitments(group.id, iOwe = false)
            updatePlay { it.copy(iOwe = owe, owedToMe = owed, commitmentsLoaded = true) }
        }
        "fama" -> perform { val fame = play.fame(group.id); updatePlay { it.copy(fame = fame) } }
        "integridad" -> perform { val shame = play.shame(group.id); updatePlay { it.copy(shame = shame) } }
        else -> perform { val ranking = play.ranking(group.id); updatePlay { it.copy(ranking = ranking) } }
    }
}

fun ConnectedViewModel.completeCommitment(item: Commitment, asCreditor: Boolean) = perform {
    if (asCreditor) play.completeCommitment(item.id) else play.requestCompletion(item.id)
    mutable.update { it.copy(notice = if (asCreditor) "Compromiso marcado como cumplido."
        else "Le avisamos a ${item.creditor} para que lo confirme.") }
    val group = state.value.group ?: return@perform
    val owe = play.commitments(group.id, iOwe = true)
    val owed = play.commitments(group.id, iOwe = false)
    updatePlay { it.copy(iOwe = owe, owedToMe = owed, commitmentsLoaded = true) }
}

// ------------------------------------------------------------------ Torneos (PL-04)

fun ConnectedViewModel.tournamentForm(value: TournamentForm) {
    if (!state.value.busy) updatePlay { it.copy(form = value) }
}

fun ConnectedViewModel.createTournament() {
    val group = state.value.group ?: return
    val form = state.value.play.form
    val date = InputRules.localDeadline(form.date.trim())
    val max = form.maxGroups.toIntOrNull()
    val entry = form.entryPoints.toIntOrNull()
    val draft = date?.let { day ->
        val sizeOk = max?.let { it in TournamentSizes } == true
        if (form.game.isNotBlank() && sizeOk && (entry ?: -1) in 0..500) {
            NewTournament(form.game, max ?: 0, entry ?: 0, day)
        } else null
    }
    if (draft == null) {
        mutable.update { it.copy(error = "Escribe el juego, elige 4, 8 o 16 grupos, inscripción de 0 a 500 " +
            "y elige la fecha en el calendario.") }
        return
    }
    perform {
        val created = play.createTournament(group.id, draft)
        mutable.update { it.copy(route = ConnectedRoute.TOURNAMENT_PLAY, tournamentsLoaded = false,
            notice = "Torneo creado. Comparte el código ${created.code.orEmpty()} con los otros grupos.",
            play = it.play.copy(tournament = created, form = TournamentForm())) }
    }
}

fun ConnectedViewModel.tournamentCode(value: String) {
    if (!state.value.busy) updatePlay { it.copy(joinCode = value.trim().take(40), joinKey = null) }
}

fun ConnectedViewModel.joinTournament() {
    val group = state.value.group
    val code = state.value.play.joinCode
    if (group == null || code.length < 4) {
        mutable.update { it.copy(error = "Elige tu grupo y escribe el código del torneo.") }
        return
    }
    val key = state.value.play.joinKey ?: UUID.randomUUID().toString()
    updatePlay { it.copy(joinKey = key) }
    perform {
        val joined = play.joinTournament(code, group.id, key)
        mutable.update { it.copy(route = ConnectedRoute.TOURNAMENT_PLAY, tournamentsLoaded = false,
            notice = "Tu grupo quedó inscrito.", play = it.play.copy(tournament = joined, joinCode = "",
                joinKey = null)) }
    }
}

fun ConnectedViewModel.openTournament(id: String) = perform {
    val detail = play.tournament(id)
    mutable.update { it.copy(route = ConnectedRoute.TOURNAMENT_PLAY, play = it.play.copy(tournament = detail)) }
}

fun ConnectedViewModel.startTournament() {
    val tournament = state.value.play.tournament ?: return
    perform {
        val started = play.startTournament(tournament.id)
        mutable.update { it.copy(notice = "Torneo iniciado. Ya está el cuadro.",
            play = it.play.copy(tournament = started)) }
    }
}

fun ConnectedViewModel.leaveTournament() {
    val tournament = state.value.play.tournament ?: return
    val group = state.value.group ?: return
    perform {
        play.leaveTournament(tournament.id, group.id)
        mutable.update { it.copy(route = ConnectedRoute.TOURNAMENTS, tournamentsLoaded = false,
            notice = "Tu grupo salió del torneo y se devolvió la inscripción.",
                play = it.play.copy(tournament = null)) }
    }
}

fun ConnectedViewModel.openMatch(id: String) = perform {
    val match = play.match(id)
    val known = state.value.play.tournament
    val tournament = if (known?.id == match.tournamentId) known else play.tournament(match.tournamentId)
    mutable.value.play.reportPhoto?.clear()
    mutable.update { it.copy(route = ConnectedRoute.MATCH, play = it.play.copy(match = match,
        tournament = tournament, scoreFirst = "",
        scoreSecond = "", reportPhoto = null, disputeReason = "")) }
}

fun ConnectedViewModel.scores(first: String, second: String) {
    if (!state.value.busy) updatePlay { it.copy(scoreFirst = first.filter(Char::isDigit).take(3),
        scoreSecond = second.filter(Char::isDigit).take(3)) }
}

fun ConnectedViewModel.prepareReportPhoto(read: suspend () -> PreparedPhoto) = perform {
    mutable.value.play.reportPhoto?.clear()
    val photo = read()
    updatePlay { it.copy(reportPhoto = photo) }
}

fun ConnectedViewModel.reportMatch() {
    val current = state.value.play
    val match = current.match ?: return
    val photo = current.reportPhoto
    val scores = current.scoreFirst.toIntOrNull()?.let { a -> current.scoreSecond.toIntOrNull()?.let { a to it } }
    val problem = when {
        scores == null || photo == null -> "Escribe los dos marcadores y elige la foto del resultado."
        scores.first == scores.second -> "No hay empates: repitan la partida y reporten al ganador."
        else -> null
    }
    if (problem != null || scores == null || photo == null) {
        mutable.update { it.copy(error = problem) }
        return
    }
    val (first, second) = scores
    perform {
        try {
            val updated = play.reportMatch(match.id, first, second,
                state.value.group?.id?.takeIf { it == match.p1 || it == match.p2 },
                photo.bytes, photo.mimeType)
            mutable.update { it.copy(notice = "Resultado enviado. El otro grupo debe confirmarlo.",
                play = it.play.copy(match = updated, reportPhoto = null)) }
        } finally { photo.clear() }
    }
}

fun ConnectedViewModel.confirmMatch() {
    val match = state.value.play.match ?: return
    perform {
        val updated = play.confirmMatch(match.id)
        val tournament = play.tournament(updated.tournamentId)
        mutable.update { it.copy(notice = "Resultado confirmado.",
            play = it.play.copy(match = updated, tournament = tournament)) }
    }
}

fun ConnectedViewModel.disputeReason(value: String) {
    if (!state.value.busy) updatePlay { it.copy(disputeReason = value.take(140)) }
}

fun ConnectedViewModel.disputeMatch() {
    val current = state.value.play
    val match = current.match ?: return
    if (current.disputeReason.trim().length !in 5..140) {
        mutable.update { it.copy(error = "Explica el motivo en 5 a 140 caracteres.") }
        return
    }
    perform {
        val updated = play.disputeMatch(match.id, current.disputeReason)
        mutable.update { it.copy(notice = "Disputa enviada. El organizador decidirá.",
            play = it.play.copy(match = updated, disputeReason = "")) }
    }
}

fun ConnectedViewModel.resolveMatch(winnerGroupId: String) {
    val match = state.value.play.match ?: return
    perform {
        val updated = play.resolveMatch(match.id, winnerGroupId, null)
        val tournament = play.tournament(updated.tournamentId)
        mutable.update { it.copy(notice = "Partida resuelta.", play = it.play.copy(match = updated,
            tournament = tournament)) }
    }
}

// ------------------------------------------------------------------ Voz (PL-05)

fun ConnectedViewModel.phrase(value: String) {
    if (!state.value.busy) updatePlay { it.copy(phrase = value.take(300), voiceDraft = null) }
}

fun ConnectedViewModel.parsePhrase() {
    val group = state.value.group ?: return
    val text = state.value.play.phrase.trim()
    if (text.isEmpty()) {
        mutable.update { it.copy(error = "Dicta o escribe tu reto primero.") }
        return
    }
    perform {
        try {
            val draft = play.parsePhrase(group.id, text)
            updatePlay { it.copy(voiceDraft = draft, voiceUnavailable = false) }
        } catch (error: ApiFailure) {
            if (error.status == 503) updatePlay { it.copy(voiceUnavailable = true) }
            throw error
        }
    }
}

/** Pasa el borrador de la IA al formulario manual para revisarlo, poner fecha y fianza, y lanzarlo. */
fun ConnectedViewModel.useVoiceDraft(rivalId: String? = null, rivalName: String? = null) {
    val current = state.value
    val voice = current.play.voiceDraft ?: return
    val targetId = rivalId ?: voice.targetId
    val nickname = rivalName ?: voice.targetNickname
    val rival = if (targetId != null && targetId != current.account?.id && nickname != null) {
        Member(targetId, nickname, "default", "member", 0, "")
    } else null
    mutable.update { it.copy(route = ConnectedRoute.CREATE, reviewing = false,
        draft = it.draft.copy(game = voice.game, condition = voice.condition, reward = voice.reward,
            targetId = rival?.id, originalPhrase = current.play.phrase.trim()),
        social = it.social.copy(selectedRival = rival, members = if (rival != null) ListState(listOf(rival), true)
            else it.social.members),
        notice = if (voice.missingFields.isEmpty()) "Revisa el reto, pon fecha y fianza, y lánzalo."
            else "Completa lo que faltó: ${voice.missingFields.joinToString { f -> FieldNames[f] ?: f }}.") }
}

/**
 * Explorar (PL-06): copia la idea al borrador y manda a elegir rival entre los miembros del grupo.
 * Al tocar "Retar a ..." se abre Crear con juego, reto y rival ya puestos. Sin grupo, la idea queda pendiente:
 * al elegir el grupo se aplica al borrador y se abre la lista de miembros (ver `selectGroup` y `applyGroup`).
 */
fun ConnectedViewModel.useIdea(game: String, condition: String) {
    if (state.value.busy) return
    mutable.update { it.copy(reviewing = false,
        draft = it.draft.copy(game = game.take(60), condition = condition.take(280))) }
    if (state.value.group == null) {
        navigate(ConnectedRoute.GROUPS)
        updatePlay { it.copy(pendingIdea = game.take(60) to condition.take(280)) }
        mutable.update { it.copy(notice = "Idea guardada. Elige el grupo y luego a quién retar.") }
    } else {
        navigate(ConnectedRoute.MEMBERS)
    }
}

/** Sin IA: lleva la frase al formulario manual como condición para no perderla (PL-05 RF4). */
fun ConnectedViewModel.manualFromPhrase() {
    val text = state.value.play.phrase.trim()
    mutable.update { it.copy(route = ConnectedRoute.CREATE, reviewing = false,
        draft = if (it.draft.condition.isBlank()) it.draft.copy(condition = text.take(280)) else it.draft,
        notice = "Completa el reto a mano.") }
}

internal val FieldNames = mapOf("game" to "juego", "condition" to "condición", "reward" to "recompensa")
