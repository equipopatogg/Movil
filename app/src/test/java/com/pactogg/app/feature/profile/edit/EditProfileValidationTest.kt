package com.pactogg.app.feature.profile.edit

import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ConsumerBoundaryFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** USR-03: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class EditProfileValidationTest {
    @Test fun USR_03_rejectsOversizedNicknameBeforeUpdate() = runBlocking {
        val boundary = ConsumerBoundaryFixture()
        assertTrue(runCatching { boundary.social.updateProfile("a".repeat(25)) }.exceptionOrNull() is IllegalArgumentException)
    }
}
