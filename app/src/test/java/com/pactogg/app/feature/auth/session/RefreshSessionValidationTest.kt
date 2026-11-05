package com.pactogg.app.feature.auth.session

import com.pactogg.app.feature.auth.data.SessionDto
import com.pactogg.app.feature.auth.data.UserDto
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.UserId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** AUTH-03: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class RefreshSessionValidationTest {
    @Test fun AUTH_03_rejectsInvalidSessionLifetime() = runBlocking {
        val bad = SessionDto(UserDto(UserId, "qa@example.com", "Player"), "access", "refresh", "Bearer", 0)
        assertTrue(runCatching { bad.toDomain(1000) }.exceptionOrNull() is IllegalArgumentException)
    }
}
