package com.pactogg.app.feature.tournaments.list

import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** TOR-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class ListTournamentsValidationTest {
    @Test fun TOR_02_rejectsUnknownTournamentStatus() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertTrue(runCatching { boundary.products.tournaments("not-a-status", null, null) }.exceptionOrNull() is IllegalArgumentException)
    }
}
