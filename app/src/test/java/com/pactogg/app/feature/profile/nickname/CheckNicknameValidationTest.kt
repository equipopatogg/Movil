package com.pactogg.app.feature.profile.nickname

import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** USR-06: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class CheckNicknameValidationTest {
    @Test fun USR_06_rejectsNicknameWithSpacesBeforeRequest() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertTrue(runCatching { boundary.social.nickname("bad nickname") }.exceptionOrNull() is IllegalArgumentException)
    }
}
