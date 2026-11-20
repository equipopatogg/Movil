package com.pactogg.app.feature.play

import com.pactogg.app.feature.auth.data.VerifyEmailRequest
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.ContractId
import com.pactogg.app.testing.shared.GroupId
import com.pactogg.app.testing.shared.OtherId
import com.pactogg.app.testing.shared.UserId
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** Rutas de avisos, compromisos, partidas, torneos y correo que no tenían prueba propia contra el servidor simulado. */
@Category(IntegrationChecks::class)
class PlayRoutesApiTest {
    private val notice = """{"id":"$OtherId","type":"contract_created","title":"Te retaron","body":"Halo","read":true}"""
    private val commitment = """{"id":"$OtherId","group_id":"$GroupId","debtor":{"user_id":"$UserId","nickname":"Yo"},""" +
        """"creditor":{"user_id":"$OtherId","nickname":"Rival"},"description":"Invitar pizzas","status":"completed"}"""
    private fun match(status: String, extra: String = "") = """{"id":"$ContractId","tournament_id":"$OtherId",""" +
        """"round":1,"position":1,"p1_group_id":"$GroupId","p2_group_id":"$UserId","p1_score":3,"p2_score":1,""" +
        """"status":"$status"$extra}"""
    private val tournament = """{"id":"$OtherId","group_id":"$GroupId","organizer_id":"$UserId","game":"Halo",""" +
        """"status":"in_progress","bracket":{"size":2,"rounds":1,"matches":[${match("pending")}]}}"""

    private fun RecordedRequest.assertRoute(method: String, path: String) {
        assertEquals(method, this.method)
        assertEquals(path, this.path?.substringBefore('?'))
        assertEquals("Bearer $Access", getHeader("Authorization"))
    }

    private fun ok(data: String) = ApiTestFixture.json(ApiTestFixture.envelope(data))
    private val empty = MockResponse().setResponseCode(204)

    @Test fun NOT_03_markReadPatchesOneNotice() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(notice))
            f.play.markRead(OtherId)
            f.server.takeRequest().assertRoute("PATCH", "/api/v1/notifications/$OtherId/read")
        }
    }

    @Test fun NOT_04_markAllReadAcceptsNoContent() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(empty)
            f.play.markAllRead()
            f.server.takeRequest().assertRoute("POST", "/api/v1/notifications/read-all")
        }
    }

    @Test fun NOT_05_deleteNoticeUsesDelete() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(empty)
            f.play.deleteNotice(OtherId)
            f.server.takeRequest().assertRoute("DELETE", "/api/v1/notifications/$OtherId")
        }
    }

    @Test fun COM_03_creditorMarksCommitmentAsCompleted() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(commitment))
            f.play.completeCommitment(OtherId)
            f.server.takeRequest().assertRoute("POST", "/api/v1/commitments/$OtherId/complete")
        }
    }

    @Test fun COM_04_debtorRequestsCompletion() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(commitment.replace("completed", "requested")))
            f.play.requestCompletion(OtherId)
            f.server.takeRequest().assertRoute("POST", "/api/v1/commitments/$OtherId/request-complete")
        }
    }

    @Test fun MAT_04_confirmMapsFinishedMatch() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(match("confirmed", ""","winner_group_id":"$GroupId"""")))
            val result = f.play.confirmMatch(ContractId)
            f.server.takeRequest().assertRoute("POST", "/api/v1/matches/$ContractId/confirm")
            assertEquals("confirmed", result.status); assertEquals(GroupId, result.winner)
        }
    }

    @Test fun MAT_05_disputeSendsTrimmedReason() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(match("disputed", ""","dispute_reason":"Marcador falso"""")))
            val result = f.play.disputeMatch(ContractId, "  Marcador falso  ")
            val request = f.server.takeRequest()
            request.assertRoute("POST", "/api/v1/matches/$ContractId/dispute")
            assertEquals("""{"reason":"Marcador falso"}""", request.body.readUtf8())
            assertEquals("Marcador falso", result.disputeReason)
        }
    }

    @Test fun MAT_06_resolveSendsWinnerAndOmitsBlankReason() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(match("resolved", ""","winner_group_id":"$GroupId"""")))
            f.play.resolveMatch(ContractId, GroupId, "   ")
            val request = f.server.takeRequest()
            request.assertRoute("POST", "/api/v1/matches/$ContractId/resolve")
            val body = request.body.readUtf8()
            assertTrue(body.contains("\"winner_group_id\":\"$GroupId\""))
            assertTrue(body.contains("\"reason\":null") || !body.contains("reason"))
        }
    }

    @Test fun TOR_06_startReturnsBracket() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(tournament))
            val started = f.play.startTournament(OtherId)
            f.server.takeRequest().assertRoute("POST", "/api/v1/tournaments/$OtherId/start")
            assertEquals("in_progress", started.status)
            assertEquals(1, started.matches.size)
        }
    }

    @Test fun USR_08_resendVerificationUsesSessionToken() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(empty)
            val response = f.sessions.authorized { f.api.resendVerification(it) }
            f.server.takeRequest().assertRoute("POST", "/api/v1/auth/verify-email/resend")
            assertEquals(204, response.code())
        }
    }

    @Test fun VOT_05_resultRevealsVotesAfterClosing() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(voteResult))
            val result = f.play.voteResult(ContractId)
            f.server.takeRequest().assertRoute("GET", "/api/v1/contracts/$ContractId/result")
            assertEquals("cheating", result.outcome); assertEquals(2, result.cheatVotes)
        }
    }

    @Test fun VOT_06_adminResolutionSendsOutcomeAndIdempotencyKey() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(voteResult))
            f.play.resolveVoting(ContractId, "cheating", "resolve-key")
            val request = f.server.takeRequest()
            request.assertRoute("POST", "/api/v1/contracts/$ContractId/votes/resolve")
            assertEquals("resolve-key", request.getHeader("Idempotency-Key"))
            assertEquals("""{"outcome":"cheating"}""", request.body.readUtf8())
        }
    }

    @Test fun PTS_03_rankingMapsKingAndPositions() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ok("""{"king":{"position":1,"user_id":"$OtherId","nickname":"Rey","points_balance":300},""" +
                """"items":[{"position":2,"user_id":"$UserId","nickname":"Yo","points_balance":120}]}"""))
            val ranking = f.play.ranking(GroupId)
            f.server.takeRequest().assertRoute("GET", "/api/v1/groups/$GroupId/ranking")
            assertEquals("Rey", ranking.king?.nickname); assertEquals(2, ranking.items.single().position)
        }
    }

    @Test fun COM_02_summaryReturnsBothCounters() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok("""{"debo":2,"me_deben":5}"""))
            val (debo, meDeben) = f.play.commitmentSummary(GroupId)
            f.server.takeRequest().assertRoute("GET", "/api/v1/groups/$GroupId/commitments/summary")
            assertEquals(2, debo); assertEquals(5, meDeben)
        }
    }

    @Test fun REP_03_fameMapsExecutorAndPhotos() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.page("""{"contract_id":"$ContractId",""" +
                """"executor":{"user_id":"$UserId","nickname":"Yo"},"game":"Halo","condition":"Ganar",""" +
                """"photos":["https://cdn.example.com/a.jpg"]}""")))
            val entry = f.play.fame(GroupId).single()
            f.server.takeRequest().assertRoute("GET", "/api/v1/groups/$GroupId/fame")
            assertEquals("Yo", entry.executor); assertEquals(1, entry.photos.size)
        }
    }

    @Test fun REP_04_cheatersListUsesShameRoute() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.page("""{"contract_id":"$ContractId",""" +
                """"label":"Tramposo","executor":{"user_id":"$OtherId","nickname":"Rival"},"game":"Halo"}""")))
            val entry = f.play.shame(GroupId).single()
            f.server.takeRequest().assertRoute("GET", "/api/v1/groups/$GroupId/shame")
            assertEquals("Rival", entry.executor); assertEquals("Tramposo", entry.label)
        }
    }

    @Test fun TOR_03_detailMapsBracket() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(tournament))
            val detail = f.play.tournament(OtherId)
            f.server.takeRequest().assertRoute("GET", "/api/v1/tournaments/$OtherId")
            assertEquals(OtherId, detail.id); assertEquals(1, detail.rounds)
        }
    }

    @Test fun MAT_02_matchDetailMapsScores() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); f.server.enqueue(ok(match("reported")))
            val detail = f.play.match(ContractId)
            f.server.takeRequest().assertRoute("GET", "/api/v1/matches/$ContractId")
            assertEquals(3, detail.p1Score); assertEquals(1, detail.p2Score)
        }
    }

    @Test fun AUTH_05_verifyEmailSendsOnlyTheTokenWithoutSession() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(signedIn = false)
            f.server.enqueue(ok("""{"email_verified":true}"""))
            val response = f.api.verifyEmail(VerifyEmailRequest("v".repeat(43)))
            val request = f.server.takeRequest()
            assertEquals("/api/v1/auth/verify-email", request.path)
            assertEquals(null, request.getHeader("Authorization"))
            assertEquals(true, response.body()?.data?.emailVerified)
        }
    }

    private val voteResult = """{"contract_id":"$ContractId","outcome":"cheating","legit_votes":1,"cheat_votes":2,""" +
        """"bond_required":25,"votes":[{"vote":"cheat","comment":"Foto editada"}]}"""
}
