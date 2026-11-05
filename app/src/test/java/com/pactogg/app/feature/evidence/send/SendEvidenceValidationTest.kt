package com.pactogg.app.feature.evidence.send

import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import com.pactogg.app.testing.shared.ContractFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** EVI-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class SendEvidenceValidationTest {
    @Test fun EVI_02_rejectsLookalikeDomainAndEmptyPhoto() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertFalse(InputRules.evidenceLink("https://youtube.com.attacker.example/a"))
        assertTrue(runCatching { boundary.products.sendPhoto(ContractFixture, byteArrayOf(), "image/png") }.exceptionOrNull() is IllegalArgumentException)
    }
}
