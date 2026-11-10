package com.pactogg.app.feature.account

import com.pactogg.app.feature.account.presentation.ExtrasPage
import com.pactogg.app.feature.account.presentation.parseAccountLink
import com.pactogg.app.feature.community.data.RevealedVoteResponse
import com.pactogg.app.feature.community.data.toDomain
import com.pactogg.app.testing.UnitChecks
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(UnitChecks::class)
class AccountLinksTest {
    @Test fun supportedLinkParsesButForeignHostsAndAmbiguousQueriesDoNot() {
        val token = "synthetic-token-123456"
        assertEquals(ExtrasPage.RESET to token, parseAccountLink("pactogg://auth/reset-password?token=$token"))
        assertNull(parseAccountLink("https://auth/reset-password?token=$token"))
        assertNull(parseAccountLink("pactogg://other/reset-password?token=$token"))
        assertNull(parseAccountLink("pactogg://auth/reset-password?token=$token&token=$token"))
        assertNull(parseAccountLink("pactogg://auth/verify-email?token=short"))
    }
    @Test fun anonymousVoteDoesNotRequireIdentityAndLegacyIdentityNeverReachesDomain() {
        val vote = Json.decodeFromString<RevealedVoteResponse>(
            """{"vote":"legit","comment":"Correcto","created_at":"2026-11-06T00:00:00Z"}""")
        assertEquals("Correcto", vote.toDomain().comment)
        val legacy = vote.copy(jurorId = "private-id", nickname = "private-name").toDomain()
        assertEquals("", legacy.nickname)
        assertEquals("", legacy.jurorId)
    }
}
