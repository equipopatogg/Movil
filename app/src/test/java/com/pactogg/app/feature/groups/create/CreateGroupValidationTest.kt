package com.pactogg.app.feature.groups.create

import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** GRP-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class CreateGroupValidationTest {
    @Test fun GRP_01_rejectsOversizedGroupNameBeforeRequest() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertTrue(runCatching { boundary.products.createGroup("a".repeat(41)) }.exceptionOrNull() is IllegalArgumentException)
    }
}
