package com.pactogg.app.feature.play

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.domain.Account
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.play.data.ApiPlayRepository
import com.pactogg.app.feature.play.data.BracketDto
import com.pactogg.app.feature.play.data.CommitmentDto
import com.pactogg.app.feature.play.data.FameDto
import com.pactogg.app.feature.play.data.NextMatchDto
import com.pactogg.app.feature.play.data.NoticeDto
import com.pactogg.app.feature.play.data.PartyDto
import com.pactogg.app.feature.play.data.PersonDto
import com.pactogg.app.feature.play.data.PlayApi
import com.pactogg.app.feature.play.data.PlayMatchDto
import com.pactogg.app.feature.play.data.RankingDto
import com.pactogg.app.feature.play.data.RankingEntryDto
import com.pactogg.app.feature.play.data.ShameDto
import com.pactogg.app.feature.play.data.TournamentDetailDto
import com.pactogg.app.feature.play.data.VoteResultDto
import com.pactogg.app.feature.play.data.VoteSummaryDto
import com.pactogg.app.feature.play.domain.NewTournament
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ContractId
import com.pactogg.app.testing.shared.FixtureStore
import com.pactogg.app.testing.shared.GroupId
import com.pactogg.app.testing.shared.OtherId
import com.pactogg.app.testing.shared.UserId
import java.lang.reflect.Proxy
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/**
 * Reglas puras de cada operación de juego, sin servidor: identificadores inválidos se detienen
 * en el teléfono y las respuestas del servidor se traducen bien al modelo de pantalla.
 */
@Category(UnitChecks::class)
class PlayRulesTest {
    private inline fun <reified T> noNetwork(): T = Proxy.newProxyInstance(T::class.java.classLoader,
        arrayOf(T::class.java)) { _, _, _ -> throw AssertionError("No se esperaba red en una prueba unitaria") } as T

    private val signedIn = SessionManager(noNetwork<PactoApi>(), FixtureStore().apply {
        saved = Session(Account(UserId, "qa@example.com", "Player"), "a", "r", Long.MAX_VALUE / 2)
    })
    private val play = ApiPlayRepository(noNetwork<PlayApi>(), signedIn)

    private fun op(name: String, call: suspend ApiPlayRepository.(String) -> Unit) = name to call

    private val byId = listOf(
        op("voteSummary") { voteSummary(it) }, op("castVote") { castVote(it, "legit", null, "k") },
        op("voteResult") { voteResult(it) }, op("resolveVoting") { resolveVoting(it, "fulfilled", "k") },
        op("markRead") { markRead(it) }, op("deleteNotice") { deleteNotice(it) },
        op("ranking") { ranking(it) }, op("commitments") { commitments(it, iOwe = false) },
        op("commitmentSummary") { commitmentSummary(it) }, op("completeCommitment") { completeCommitment(it) },
        op("requestCompletion") { requestCompletion(it) }, op("fame") { fame(it) }, op("shame") { shame(it) },
        op("createTournament") { createTournament(it, NewTournament("Halo", 4, 10, "2099-11-10T00:00:00Z")) },
        op("tournament") { tournament(it) }, op("joinTournament") { joinTournament("ABCD1234", it, "k") },
        op("leaveTournament") { leaveTournament(it, GroupId) }, op("startTournament") { startTournament(it) },
        op("match") { match(it) }, op("confirmMatch") { confirmMatch(it) },
        op("disputeMatch") { disputeMatch(it, "Marcador falso") }, op("resolveMatch") { resolveMatch(ContractId, it, null) },
        op("reportMatch") { reportMatch(it, 2, 1, null, byteArrayOf(1), "image/jpeg") },
        op("parsePhrase") { parsePhrase(it, "Te reto") }, op("reportUser") { reportUser(it, OtherId, "spam") },
        op("blockUser") { blockUser(it) }, op("unblockUser") { unblockUser(it) },
        op("reportEvidence") { reportEvidence(GroupId, it, "otro") },
    )

    @Test fun PL_RULES_everyIdentifierMustBeA24CharLowercaseObjectId() = runBlocking {
        signedIn.restore()
        listOf("", "123", "../$ContractId", ContractId.replace('3', 'G'), "$ContractId?x=1", " $ContractId").forEach { bad ->
            byId.forEach { (name, call) ->
                val error = runCatching { play.call(bad) }.exceptionOrNull()
                assertTrue("$name aceptó '$bad'", error is IllegalArgumentException)
            }
        }
    }

    @Test fun PL_02_noticeWithoutReadFlagCountsAsUnread() {
        val notice = NoticeDto(OtherId, "contract_created", "Te retaron", "Halo", contractId = ContractId).toDomain()
        assertFalse(notice.read); assertEquals(ContractId, notice.contractId)
    }

    @Test fun PL_01_summaryAndResultKeepCountersAndRevealedVotes() {
        val summary = VoteSummaryDto(ContractId, "voting", jurorsAssigned = 3, votesCast = 1, votesRemaining = 2).toDomain()
        assertEquals(2, summary.votesRemaining); assertNull(summary.legitVotes)
        val result = VoteResultDto(ContractId, "cheating", cheatVotes = 2, votes = listOf(
            com.pactogg.app.feature.play.data.RevealedVoteDto("cheat", "Editada"))).toDomain()
        assertEquals(listOf("cheat" to "Editada"), result.votes)
    }

    @Test fun PL_03_rankingCommitmentsFameAndCheatersMapNames() {
        val ranking = RankingDto(RankingEntryDto(1, OtherId, "Rey", 300), listOf(RankingEntryDto(2, UserId, "Yo"))).toDomain()
        assertEquals("Rey", ranking.king?.nickname); assertEquals(0, ranking.items.single().points)
        val commitment = CommitmentDto(OtherId, GroupId, PartyDto(UserId, "Yo"), PartyDto(OtherId, "Rival"),
            "Invitar pizzas", status = "pending").toDomain()
        assertEquals("Yo", commitment.debtor); assertEquals("Rival", commitment.creditor)
        assertNull(FameDto(ContractId).toDomain().executor)
        assertEquals("Rival", ShameDto(ContractId, "Tramposo", PersonDto(OtherId, "Rival")).toDomain().executor)
    }

    @Test fun PL_04_tournamentWithoutGroupsAndNextMatchMapSafely() {
        val match = PlayMatchDto(ContractId, OtherId, 1, 1, p1 = GroupId, status = "pending")
        val detail = TournamentDetailDto(OtherId, GroupId, UserId, "Halo", status = "open",
            bracket = BracketDto(2, 1, listOf(match))).toDomain()
        assertTrue(detail.groups.isEmpty()); assertEquals(1, detail.matches.size); assertEquals(1, detail.rounds)
        val next = NextMatchDto(match, GroupId).toDomain()
        assertEquals(GroupId, next.myGroupId); assertNull(next.opponentGroupId)
    }

    @Test fun PL_02_listsAndBulkActionsNeedSessionBeforeAnyRequest() = runBlocking {
        val signedOut = ApiPlayRepository(noNetwork<PlayApi>(), SessionManager(noNetwork<PactoApi>(), FixtureStore()))
        listOf<suspend () -> Unit>({ signedOut.pendingVotes() }, { signedOut.notices(true, null) },
            { signedOut.markAllRead() }, { signedOut.nextMatch() }).forEach { call ->
            val error = runCatching { call() }.exceptionOrNull()
            assertEquals(401, (error as com.pactogg.app.core.network.ApiFailure).status)
        }
    }
}
