package com.pactogg.app.feature.groups.join

import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** MEM-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class JoinGroupValidationTest {
    @Test fun MEM_01_rejectsIncompleteInviteCode() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertTrue(runCatching { boundary.social.join("ABCDE") }.exceptionOrNull() is IllegalArgumentException)
    }
}
