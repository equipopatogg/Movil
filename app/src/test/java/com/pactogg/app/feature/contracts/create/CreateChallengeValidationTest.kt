package com.pactogg.app.feature.contracts.create

import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.GroupFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-01: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class CreateChallengeValidationTest {
    @Test fun CON_01_rejectsPastDeadlineAndMonetaryReward() = runBlocking {
        assertFalse(InputRules.challenge(GroupFixture, ChallengeDraft("Halo", "Win", "Choose game", "2000-01-01T00:00:00Z", 25), 1000L*1000*1000*1000))
        assertFalse(InputRules.challenge(GroupFixture, ChallengeDraft("Halo", "Win", "100 USD", "2099-11-10T23:59:00Z", 25), 1000))
    }
}
