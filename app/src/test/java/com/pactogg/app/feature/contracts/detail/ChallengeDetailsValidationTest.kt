package com.pactogg.app.feature.contracts.detail

import com.pactogg.app.feature.connected.data.ContractDto
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.GroupId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-03: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class ChallengeDetailsValidationTest {
    @Test fun CON_03_rejectsMalformedChallengeIdentity() = runBlocking {
        assertTrue(runCatching { ContractDto("invalid-id", GroupId, "Halo", "Win", "Choose", "2099-11-10T00:00:00Z", 25, "open").toDomain() }.exceptionOrNull() is IllegalArgumentException)
    }
}
