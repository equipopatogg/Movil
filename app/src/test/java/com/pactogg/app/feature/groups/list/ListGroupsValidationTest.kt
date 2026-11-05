package com.pactogg.app.feature.groups.list

import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** GRP-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class ListGroupsValidationTest {
    @Test fun GRP_02_rejectsOversizedGroupSearch() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertTrue(runCatching { boundary.social.groups("q".repeat(41), null) }.exceptionOrNull() is IllegalArgumentException)
    }
}
