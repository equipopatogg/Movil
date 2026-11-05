package com.pactogg.app.feature.auth.logout

import com.pactogg.app.feature.auth.data.LogoutRequest
import com.pactogg.app.testing.UnitChecks
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category

/** AUTH-04: explicit operation cases; shared helpers only prepare test infrastructure/data. */
@Category(UnitChecks::class)
class LogoutValidationTest {
    @Test fun AUTH_04_serializesRefreshTokenWithoutPassword() = runBlocking {
        val request = Json.encodeToString(LogoutRequest.serializer(), LogoutRequest("synthetic"))
        assertTrue(request.contains("refresh_token"))
        assertFalse(request.contains("password"))
    }
}
