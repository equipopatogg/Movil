package com.pactogg.app.feature.play

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.play.domain.NewTournament
import com.pactogg.app.testing.IntegrationChecks
import com.pactogg.app.testing.shared.Access
import com.pactogg.app.testing.shared.ApiTestFixture
import com.pactogg.app.testing.shared.ContractId
import com.pactogg.app.testing.shared.GroupId
import com.pactogg.app.testing.shared.OtherId
import com.pactogg.app.testing.shared.UserId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-01..PL-05: contrato de red de jurado, avisos, ranking, torneos y voz contra un servidor simulado. */
@Category(IntegrationChecks::class)
class PlayApiTest {
    private val match = """{"id":"$ContractId","tournament_id":"$OtherId","round":1,"position":1,""" +
        """"p1_group_id":"$GroupId","p2_group_id":"$UserId","p1_score":3,"p2_score":1,"status":"reported",""" +
        """"reported_by_group_id":"$GroupId"}"""
    private val tournament = """{"id":"$OtherId","group_id":"$GroupId","organizer_id":"$UserId","game":"Halo",""" +
        """"format":"single_elimination","max_groups":4,"entry_points":10,"prize_pool":20,"status":"open",""" +
        """"code":"ABCD1234","groups":[{"group_id":"$GroupId","name":"Equipo","is_organizer":true}],""" +
        """"bracket":{"size":0,"rounds":0,"matches":[]}}"""

    @Test fun PL_01_voteSendsIdempotencyKeyThenReadsSummary() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("""{"contract_id":"$ContractId",""" +
                """"vote":"legit","status":"voting","votes_cast":1,"jurors_assigned":3}""")))
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("""{"contract_id":"$ContractId",""" +
                """"status":"voting","jurors_assigned":3,"votes_cast":1,"votes_remaining":2,"i_am_juror":true,""" +
                """"i_voted":true}""")))
            val summary = f.play.castVote(ContractId, "legit", "Se ve real", "key-1")
            val vote = f.server.takeRequest()
            assertEquals("PUT", vote.method)
            assertEquals("/api/v1/contracts/$ContractId/votes", vote.path)
            assertEquals("key-1", vote.getHeader("Idempotency-Key"))
            assertEquals("Bearer $Access", vote.getHeader("Authorization"))
            assertTrue(vote.body.readUtf8().contains("\"vote\":\"legit\""))
            assertEquals("/api/v1/contracts/$ContractId/votes/summary", f.server.takeRequest().path)
            assertTrue(summary.iVoted); assertEquals(2, summary.votesRemaining)
            assertEquals(2, f.server.requestCount)
        }
    }

    @Test fun PL_01_pendingVotesMapContractAndDeadline() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.page("""{"contract":${ApiTestFixture.contractJson("voting")},""" +
                """"voting_ends_at":"2099-11-11T00:00:00Z"}""")))
            val pending = f.play.pendingVotes().single()
            assertEquals(ContractId, pending.contract.id)
            assertEquals("/api/v1/me/votes/pending", f.server.takeRequest().path?.substringBefore('?'))
        }
    }

    @Test fun PL_02_unreadNoticesUseFilterAndServerCounter() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":[{"id":"$OtherId","type":"contract_created",""" +
                """"title":"Te retaron","body":"Halo","contract_id":"$ContractId","group_id":"$GroupId"}],""" +
                """"meta":{"has_more":true,"next_cursor":"c1","unread_count":4}}"""))
            val page = f.play.notices(unreadOnly = true, cursor = null)
            val request = f.server.takeRequest()
            assertEquals("true", request.requestUrl?.queryParameter("unread"))
            assertNull(request.requestUrl?.queryParameter("cursor"))
            assertEquals(4, page.unread); assertTrue(page.hasMore); assertEquals("c1", page.nextCursor)
            assertFalse(page.items.single().read)
        }
    }

    @Test fun PL_03_commitmentsSendServerRoleAndMapBothParties() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.page("""{"id":"$OtherId","group_id":"$GroupId",""" +
                """"debtor":{"user_id":"$UserId","nickname":"Yo"},"creditor":{"user_id":"$OtherId",""" +
                """"nickname":"Rival"},"description":"Invitar pizzas","game":"Halo","status":"pending"}""")))
            val item = f.play.commitments(GroupId, iOwe = true).single()
            val request = f.server.takeRequest()
            assertEquals("/api/v1/groups/$GroupId/commitments", request.path?.substringBefore('?'))
            assertEquals("debo", request.requestUrl?.queryParameter("role"))
            assertEquals("Yo", item.debtor); assertEquals("Rival", item.creditor)
        }
    }

    @Test fun PL_04_createTournamentSendsSingleEliminationAndReturnsCode() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope(tournament), 201))
            val created = f.play.createTournament(GroupId, NewTournament("Halo", 4, 10, "2099-11-10T00:00:00Z"))
            val request = f.server.takeRequest()
            assertEquals("POST", request.method)
            assertEquals("/api/v1/groups/$GroupId/tournaments", request.path)
            val body = request.body.readUtf8()
            assertTrue(body.contains("\"format\":\"single_elimination\"")); assertTrue(body.contains("\"max_groups\":4"))
            assertEquals("ABCD1234", created.code); assertEquals("Equipo", created.groupName(GroupId))
        }
    }

    @Test fun PL_04_joinByCodeSendsKeyGroupAndCode() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope(tournament)))
            f.play.joinTournament("ABCD1234", GroupId, "join-key")
            val request = f.server.takeRequest()
            assertEquals("/api/v1/tournaments/ABCD1234/join", request.path)
            assertEquals("join-key", request.getHeader("Idempotency-Key"))
            val body = request.body.readUtf8()
            assertTrue(body.contains("\"group_id\":\"$GroupId\"")); assertTrue(body.contains("\"code\":\"ABCD1234\""))
        }
    }

    @Test fun PL_04_nullNextMatchMeansNoMatch() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"data":null}"""))
            assertNull(f.play.nextMatch())
            assertEquals("/api/v1/me/matches/next", f.server.takeRequest().path)
        }
    }

    @Test fun PL_04_reportIsMultipartWithScoresPhotoAndGroup() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope(match)))
            val updated = f.play.reportMatch(ContractId, 3, 1, GroupId, byteArrayOf(1, 2, 3), "image/jpeg")
            val request = f.server.takeRequest()
            assertEquals("/api/v1/matches/$ContractId/report", request.path)
            assertTrue(request.getHeader("Content-Type").orEmpty().startsWith("multipart/form-data"))
            val body = request.body.readUtf8()
            listOf("name=\"file\"", "name=\"p1_score\"", "name=\"p2_score\"", "name=\"group_id\"", "image/jpeg")
                .forEach { assertTrue(it, body.contains(it)) }
            assertEquals("reported", updated.status); assertEquals(GroupId, updated.reportedBy)
        }
    }

    @Test fun PL_04_matchErrorKeepsCodeWithoutRetryOrServerText() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare(); val before = f.store.saved
            f.server.enqueue(ApiTestFixture.json("""{"code":"CANNOT_CONFIRM_OWN_REPORT","message":"private-detail"}""",
                403))
            val error = runCatching { f.play.confirmMatch(ContractId) }.exceptionOrNull() as ApiFailure
            assertEquals(403, error.status); assertEquals("CANNOT_CONFIRM_OWN_REPORT", error.code)
            assertFalse(error.toString().contains("private-detail"))
            assertEquals(before, f.store.saved); assertEquals(1, f.server.requestCount)
        }
    }

    @Test fun PL_05_parseMapsDraftMissingFieldsAndMembers() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("""{"target_type":"group","game":"Halo",""" +
                """"condition":"Ganar 3","reward":"","missing_fields":["reward"],"rival_unmatched":true,""" +
                """"members":[{"id":"$OtherId","nickname":"Rival"}]}""")))
            val draft = f.play.parsePhrase(GroupId, "  Reto a Dani a ganar 3 de Halo  ")
            val request = f.server.takeRequest()
            assertEquals("/api/v1/groups/$GroupId/contracts/parse", request.path)
            assertTrue(request.body.readUtf8().contains("\"phrase\":\"Reto a Dani a ganar 3 de Halo\""))
            assertNull(draft.targetId); assertEquals(listOf("reward"), draft.missingFields)
            assertTrue(draft.rivalUnmatched); assertEquals("Rival", draft.members.single().nickname)
        }
    }

    @Test fun PL_05_unavailableAiIsReportedAs503WithoutRetry() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("""{"code":"AI_UNAVAILABLE","message":"x"}""", 503))
            val error = runCatching { f.play.parsePhrase(GroupId, "Reto") }.exceptionOrNull() as ApiFailure
            assertEquals(503, error.status); assertEquals("AI_UNAVAILABLE", error.code)
            assertEquals(1, f.server.requestCount)
        }
    }

    @Test fun PL_09_avatarPatchSendsOnlyAvatarAndMapsProfile() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope(
                ApiTestFixture.profileJson.replace("\"avatar_id\":\"default\"", "\"avatar_id\":\"cyan\""))))
            val profile = f.play.avatar("cyan")
            val request = f.server.takeRequest()
            assertEquals("PATCH", request.method); assertEquals("/api/v1/users/me", request.path)
            assertEquals("{\"avatar_id\":\"cyan\"}", request.body.readUtf8())
            assertEquals("cyan", profile.avatar)
        }
    }

    @Test fun PL_09_avatarPhotoUploadsMultipartAndMapsPhotoUrl() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            val url = "https://cdn.example/avatars/x.jpg"
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope(ApiTestFixture.profileJson
                .replace("\"avatar_id\":\"default\"", "\"avatar_id\":\"photo\",\"avatar_url\":\"$url\""))))
            val profile = f.play.avatarPhoto(byteArrayOf(1, 2, 3), "image/jpeg")
            val request = f.server.takeRequest()
            assertEquals("POST", request.method); assertEquals("/api/v1/users/me/avatar", request.path)
            assertTrue(request.getHeader("Content-Type").orEmpty().startsWith("multipart/form-data"))
            assertTrue(request.body.readUtf8().contains("name=\"file\"; filename=\"avatar.jpg\""))
            assertEquals("photo", profile.avatar); assertEquals(url, profile.avatarUrl)
        }
    }

    @Test fun PL_09_unblockAndReportEvidenceUseServerRoutes() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("", 204))
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("""{"id":"$OtherId"}"""), 201))
            f.play.unblockUser(OtherId)
            f.play.reportEvidence(GroupId, ContractId, "sexual")
            val unblock = f.server.takeRequest()
            assertEquals("DELETE", unblock.method); assertEquals("/api/v1/users/$OtherId/block", unblock.path)
            val report = f.server.takeRequest()
            assertEquals("/api/v1/reports", report.path)
            val body = report.body.readUtf8()
            listOf("\"target_type\":\"evidence\"", "\"target_id\":\"$ContractId\"", "\"reason\":\"sexual\"")
                .forEach { assertTrue(it, body.contains(it)) }
        }
    }

    @Test fun PL_09_deleteAccountSendsConfirmationInDeleteBody() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json("", 204))
            f.play.deleteAccount("clave-de-prueba-123")
            val request = f.server.takeRequest()
            assertEquals("DELETE", request.method); assertEquals("/api/v1/users/me", request.path)
            assertEquals("Bearer $Access", request.getHeader("Authorization"))
            val body = request.body.readUtf8()
            assertTrue(body.contains("\"confirm\":\"ELIMINAR\"")); assertTrue(body.contains("\"password\""))
            assertEquals(1, f.server.requestCount)
        }
    }

    @Test fun PL_09_reportAndBlockUseServerRoutesAndReasons() = runBlocking {
        ApiTestFixture().use { f ->
            f.prepare()
            f.server.enqueue(ApiTestFixture.json(ApiTestFixture.envelope("""{"id":"$OtherId"}"""), 201))
            f.server.enqueue(ApiTestFixture.json("", 204))
            f.play.reportUser(GroupId, OtherId, "trampa")
            f.play.blockUser(OtherId)
            val report = f.server.takeRequest()
            assertEquals("/api/v1/reports", report.path)
            val body = report.body.readUtf8()
            listOf("\"target_type\":\"user\"", "\"target_id\":\"$OtherId\"", "\"reason\":\"trampa\"")
                .forEach { assertTrue(it, body.contains(it)) }
            val block = f.server.takeRequest()
            assertEquals("POST", block.method); assertEquals("/api/v1/users/$OtherId/block", block.path)
        }
    }
}
