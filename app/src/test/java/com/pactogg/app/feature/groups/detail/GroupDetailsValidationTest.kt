package com.pactogg.app.feature.groups.detail

import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** GRP-03: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class GroupDetailsValidationTest {
    @Test fun GRP_03_rejectsMalformedGroupIdentity() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertTrue(runCatching { boundary.social.group("invalid-id") }.exceptionOrNull() is IllegalArgumentException)
    }
}
