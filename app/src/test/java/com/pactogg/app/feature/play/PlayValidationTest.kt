package com.pactogg.app.feature.play

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.play.data.ApiPlayRepository
import com.pactogg.app.feature.play.data.PlayApi
import com.pactogg.app.feature.play.domain.NewTournament
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ContractId
import com.pactogg.app.testing.shared.FixtureStore
import com.pactogg.app.testing.shared.GroupId
import java.lang.reflect.Proxy
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-01, PL-04 y PL-05: los datos inválidos se detienen antes de tocar la red. */
@Category(UnitChecks::class)
class PlayValidationTest {
    private inline fun <reified T> noNetwork(): T = Proxy.newProxyInstance(T::class.java.classLoader,
        arrayOf(T::class.java)) { _, _, _ -> throw AssertionError("No se esperaba red en una prueba unitaria") } as T

    private val play = ApiPlayRepository(noNetwork<PlayApi>(), SessionManager(noNetwork<PactoApi>(), FixtureStore()))

    private fun rejected(block: suspend () -> Unit) =
        assertTrue(runCatching { runBlocking { block() } }.exceptionOrNull() is IllegalArgumentException)

    @Test fun PL_01_unknownVoteOrOutcomeAndLongCommentAreRejected() {
        rejected { play.castVote(ContractId, "maybe", null, "k") }
        rejected { play.castVote(ContractId, "legit", "x".repeat(141), "k") }
        rejected { play.resolveVoting(ContractId, "draw", "k") }
    }

    @Test fun PL_04_tiesOversizedPhotosAndBadTournamentDataAreRejected() {
        rejected { play.reportMatch(ContractId, 2, 2, null, byteArrayOf(1), "image/jpeg") }
        rejected { play.reportMatch(ContractId, 2, 1, null, byteArrayOf(), "image/jpeg") }
        rejected { play.reportMatch(ContractId, 2, 1, null, byteArrayOf(1), "image/gif") }
        rejected { play.createTournament(GroupId, NewTournament("Halo", 17, 0, "2099-11-10T00:00:00Z")) }
        rejected { play.createTournament(GroupId, NewTournament("Halo", 6, 0, "2099-11-10T00:00:00Z")) }
        rejected { play.createTournament(GroupId, NewTournament("", 4, 0, "2099-11-10T00:00:00Z")) }
        rejected { play.joinTournament("ab", GroupId, "k") }
        rejected { play.disputeMatch(ContractId, "no") }
    }

    @Test fun PL_05_emptyOrTooLongPhraseIsRejected() {
        rejected { play.parsePhrase(GroupId, "   ") }
        rejected { play.parsePhrase(GroupId, "x".repeat(301)) }
    }

    @Test fun PL_09_unknownAvatarReasonOrOversizedPasswordAreRejected() {
        rejected { play.avatar("dorado") }
        rejected { play.reportUser(GroupId, ContractId, "porque sí") }
        rejected { play.deleteAccount("x".repeat(129)) }
        rejected { play.avatarPhoto(byteArrayOf(), "image/jpeg") }
        rejected { play.avatarPhoto(byteArrayOf(1), "image/gif") }
    }
}
