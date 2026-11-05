package com.pactogg.app.feature.profile.detail

import com.pactogg.app.feature.connected.data.ProfileDto
import com.pactogg.app.feature.connected.data.ProfileStatsDto
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.UserId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** USR-02: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class ProfileDetailsValidationTest {
    @Test fun USR_02_rejectsOutOfRangeProfileStatistics() = runBlocking {
        assertTrue(runCatching { ProfileDto(UserId, "qa@example.com", "Player", "default", false, ProfileStatsDto(0,0,0,0,101), 0).toDomain() }.exceptionOrNull() is IllegalArgumentException)
    }
}
