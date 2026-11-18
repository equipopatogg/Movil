package com.pactogg.app.feature.play.presentation

import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.play.domain.VoteSummary
import com.pactogg.app.testing.UnitChecks
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-01 RF7: quien no puede votar ve el motivo en vez de una pantalla sin botón. */
@Category(UnitChecks::class)
class VoteBlockedReasonTest {
    private val contract = Challenge("c", "g", "Halo", "20 kills", "Pizza", "2099-01-01T00:00:00Z", 25, "voting",
        challengerId = "retador", targetType = "member", targetId = "retado", acceptedBy = "retado")
    private fun summary(juror: Boolean = false, voted: Boolean = false, jurors: Int = 3) =
        VoteSummary("c", "voting", jurors, 0, jurors, null, juror, voted, false, false, null, null)

    @Test fun PL_01_partiesAreToldTheJuryDecides() {
        listOf("retador", "retado").forEach { who ->
            assertTrue(voteBlockedReason(contract, summary(), who)!!.startsWith("Eres parte de este reto"))
        }
    }

    @Test fun PL_01_otherReasonsAreExplained() {
        assertTrue(voteBlockedReason(contract, summary(), "otro")!!.contains("No te tocó"))
        assertTrue(voteBlockedReason(contract, summary(jurors = 0), "otro")!!.contains("administrador"))
        assertTrue(voteBlockedReason(contract.copy(status = "in_progress"), summary(), "otro")!!
            .contains("primera evidencia"))
        assertTrue(voteBlockedReason(contract, null, "otro")!!.contains("Actualizar"))
        assertNull(voteBlockedReason(contract, summary(juror = true, voted = true), "otro"))
    }
}
