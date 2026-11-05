package com.pactogg.app.feature.contracts.reject

import com.pactogg.app.feature.connected.presentation.canReject
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.testing.shared.ContractFixture
import com.pactogg.app.testing.shared.OtherId
import com.pactogg.app.testing.shared.UserId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** CON-07: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class RejectChallengeValidationTest {
    @Test fun CON_07_rejectsGroupTargetAndAnotherTargetUser() = runBlocking {
        assertFalse(canReject(ContractFixture.copy(targetType="group"), UserId, false))
        assertFalse(canReject(ContractFixture, OtherId, false))
    }
}
