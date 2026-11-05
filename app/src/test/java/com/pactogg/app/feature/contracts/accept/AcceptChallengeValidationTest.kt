package com.pactogg.app.feature.contracts.accept

import com.pactogg.app.feature.connected.presentation.canAccept
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ContractFixture
import com.pactogg.app.testing.shared.GroupFixture
import com.pactogg.app.testing.shared.OtherId
import com.pactogg.app.testing.shared.UserId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-06: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class AcceptChallengeValidationTest {
    @Test fun CON_06_rejectsOwnChallengeAndInsufficientBalance() = runBlocking {
        assertFalse(canAccept(ContractFixture, OtherId, GroupFixture, false))
        assertFalse(canAccept(ContractFixture, UserId, GroupFixture.copy(points=0), false))
    }
}
