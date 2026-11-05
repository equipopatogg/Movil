package com.pactogg.app.feature.groups.members

import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import com.pactogg.app.testing.shared.GroupId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** MEM-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class ListMembersValidationTest {
    @Test fun MEM_02_rejectsOversizedMemberSearch() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertTrue(runCatching { boundary.social.members(GroupId, "q".repeat(25), null) }.exceptionOrNull() is IllegalArgumentException)
    }
}
