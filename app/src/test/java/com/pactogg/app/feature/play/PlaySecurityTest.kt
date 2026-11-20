package com.pactogg.app.feature.play

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.play.data.ApiPlayRepository
import com.pactogg.app.feature.play.domain.AvatarIds
import com.pactogg.app.feature.play.domain.EvidenceReportReasons
import com.pactogg.app.feature.play.domain.NewTournament
import com.pactogg.app.feature.play.domain.ReportReasons
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.ContractId
import com.pactogg.app.testing.shared.GroupId
import com.pactogg.app.testing.shared.OtherId
import com.pactogg.app.testing.shared.Refresh
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.experimental.categories.Category

/**
 * PL-01..PL-13: seguridad de TODAS las operaciones del módulo de juego (jurado, avisos, ranking,
 * compromisos, fama/tramposos, torneos, partidas, voz, avatar, cuenta, reportes y bloqueos).
 * Cada operación se prueba sin sesión, con sesión revocada y con identificadores manipulados.
 */
@Category(SecurityChecks::class)
class PlaySecurityTest {
    private class Op(val name: String, val takesId: Boolean = true, val call: suspend ApiPlayRepository.(String) -> Unit)

    private val photo = byteArrayOf(1, 2, 3)
    private val ops = listOf(
        Op("PL-01 votos pendientes", takesId = false) { pendingVotes() },
        Op("PL-01 resumen de votos") { voteSummary(it) },
        Op("PL-01 votar") { castVote(it, "legit", null, "key-1") },
        Op("PL-01 resultado") { voteResult(it) },
        Op("PL-01 resolver votación") { resolveVoting(it, "fulfilled", "key-2") },
        Op("PL-02 avisos", takesId = false) { notices(unreadOnly = false, cursor = null) },
        Op("PL-02 marcar leído") { markRead(it) },
        Op("PL-02 marcar todos", takesId = false) { markAllRead() },
        Op("PL-02 borrar aviso") { deleteNotice(it) },
        Op("PL-03 ranking") { ranking(it) },
        Op("PL-03 compromisos") { commitments(it, iOwe = true) },
        Op("PL-03 resumen de compromisos") { commitmentSummary(it) },
        Op("PL-03 marcar cumplido") { completeCommitment(it) },
        Op("PL-03 ya cumplí") { requestCompletion(it) },
        Op("PL-03 fama") { fame(it) },
        Op("PL-03 los tramposos") { shame(it) },
        Op("PL-04 crear torneo") { createTournament(it, NewTournament("Halo", 4, 10, "2099-11-10T00:00:00Z")) },
        Op("PL-04 ver torneo") { tournament(it) },
        Op("PL-04 unirse a torneo") { joinTournament("ABCD1234", it, "key-3") },
        Op("PL-04 salir de torneo") { leaveTournament(OtherId, it) },
        Op("PL-04 iniciar torneo") { startTournament(it) },
        Op("PL-04 ver partida") { match(it) },
        Op("PL-04 siguiente partida", takesId = false) { nextMatch() },
        Op("PL-04 reportar resultado") { reportMatch(it, 3, 1, GroupId, photo, "image/jpeg") },
        Op("PL-04 confirmar resultado") { confirmMatch(it) },
        Op("PL-04 disputar resultado") { disputeMatch(it, "El marcador no es real") },
        Op("PL-04 resolver disputa") { resolveMatch(it, GroupId, "Revisé la captura") },
        Op("PL-05 reto por voz") { parsePhrase(it, "Te reto a ganar en Halo") },
        Op("PL-09 foto de perfil", takesId = false) { avatarPhoto(photo, "image/jpeg") },
        Op("PL-09 avatar", takesId = false) { avatar(AvatarIds.first()) },
        Op("PL-09 eliminar cuenta", takesId = false) { deleteAccount("frase sintética") },
        Op("PL-09 reportar usuario") { reportUser(GroupId, it, ReportReasons.first().second) },
        Op("PL-09 bloquear") { blockUser(it) },
        Op("PL-09 desbloquear") { unblockUser(it) },
        Op("PL-09 reportar evidencia") { reportEvidence(GroupId, it, EvidenceReportReasons.first().second) },
    )

    @Test fun PL_SEC_everyOperationWithoutSessionFailsBeforeTouchingTheNetwork() = runBlocking {
        ops.forEach { op ->
            ApiTestFixture().use { f ->
                f.prepare(signedIn = false)
                val error = failure(op) { op.call(f.play, ContractId) }
                assertTrue("${op.name}: debe pedir sesión", error is ApiFailure && error.status == 401)
                assertEquals("${op.name}: no debe salir ninguna petición", 0, f.server.requestCount)
            }
        }
    }

    @Test fun PL_SEC_revokedSessionIsClearedAndTokensNeverLeak() = runBlocking {
        ops.forEach { op ->
            ApiTestFixture().use { f ->
                f.prepare()
                f.server.enqueue(ApiTestFixture.json(Revoked, 401))
                f.server.enqueue(ApiTestFixture.json(Revoked, 401))
                val error = failure(op) { op.call(f.play, ContractId) }
                assertTrue("${op.name}: debe terminar en 401", error is ApiFailure && error.status == 401)
                val first = f.server.takeRequest()
                assertEquals("${op.name}: el token va solo en la cabecera", "Bearer $Access",
                    first.getHeader("Authorization"))
                assertFalse("${op.name}: el token no va en la URL", first.path.orEmpty().contains(Access))
                assertEquals("${op.name}: intenta renovar una sola vez", "/api/v1/auth/refresh",
                    f.server.takeRequest().path)
                assertEquals("${op.name}: no reintenta en bucle", 2, f.server.requestCount)
                assertNull("${op.name}: borra la sesión guardada", f.store.saved)
                assertNull("${op.name}: cierra la sesión en memoria", f.sessions.current.value)
                val text = "${error.message} $error"
                assertFalse("${op.name}: el error no expone tokens", text.contains(Access) || text.contains(Refresh))
            }
        }
    }

    @Test fun PL_SEC_manipulatedIdentifiersAreRejectedOnTheDevice() = runBlocking {
        val attacks = listOf("../../admin", "$ContractId/../x", "{\"\$ne\":null}", "", "ABC", "GGGGGGGGGGGGGGGGGGGGGGGG")
        ops.filter { it.takesId }.forEach { op ->
            attacks.forEach { bad ->
                ApiTestFixture().use { f ->
                    f.prepare()
                    val error = failure(op) { op.call(f.play, bad) }
                    assertTrue("${op.name} con '$bad': se rechaza en el teléfono", error is IllegalArgumentException)
                    assertEquals("${op.name} con '$bad': no sale al servidor", 0, f.server.requestCount)
                }
            }
        }
    }

    @Test fun PL_SEC_writesAreNotReplayedAfterRenewingTheSession() = runBlocking {
        val writes = ops.filter { it.name in setOf("PL-01 votar", "PL-03 marcar cumplido", "PL-09 bloquear") }
        assertEquals(3, writes.size)
        writes.forEach { op ->
            ApiTestFixture().use { f ->
                f.prepare()
                f.server.enqueue(ApiTestFixture.json(Revoked, 401))
                f.server.enqueue(ApiTestFixture.json(ApiTestFixture.sessionJson))
                val error = failure(op) { op.call(f.play, ContractId) }
                assertTrue("${op.name}: avisa que se renovó la sesión", error is ApiFailure && error.status == 401)
                assertEquals("${op.name}: no repite la escritura por su cuenta", 2, f.server.requestCount)
                assertEquals("new-synthetic-access", f.store.saved?.accessToken)
            }
        }
    }

    private suspend fun failure(op: Op, block: suspend () -> Unit): Throwable {
        try {
            block()
        } catch (expected: Throwable) {
            return expected
        }
        fail("${op.name}: debía fallar")
        throw AssertionError()
    }

    private companion object {
        const val Revoked = """{"error":{"code":"UNAUTHORIZED","message":"Sesión inválida."}}"""
    }
}
