package com.pactogg.app.feature.contracts.list

import com.pactogg.app.feature.connected.domain.BoardFilter
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import com.pactogg.app.testing.shared.GroupId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class ListChallengesValidationTest {
    @Test fun CON_02_rejectsUnknownBoardStatus() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertTrue(runCatching { boundary.social.contracts(GroupId, BoardFilter(status="invalid"), null) }.exceptionOrNull() is IllegalArgumentException)
    }
}
