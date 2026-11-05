package com.pactogg.app.core.network

import org.junit.experimental.categories.Category
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.core.network.dto.ApiErrorDto
import com.pactogg.app.core.network.mapper.toFailure
import com.pactogg.app.core.result.FailureKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@Category(SecurityChecks::class)
class ApiErrorMapperTest {
    @Test
    fun mapsSecurityAndRetryStatesWithoutUsingServerText() {
        val expected = mapOf(401 to FailureKind.UNAUTHENTICATED, 403 to FailureKind.FORBIDDEN,
            409 to FailureKind.CONFLICT, 429 to FailureKind.RATE_LIMITED,
            503 to FailureKind.UNAVAILABLE, 418 to FailureKind.UNKNOWN)
        expected.forEach { (status, kind) -> assertEquals(kind, ApiErrorDto().toFailure(status).kind) }
    }

    @Test
    fun rejectsUnsafeCorrelationIds() {
        assertNull(ApiErrorDto(requestId = "email@example.com\nsecret").toFailure(500).requestId)
        assertNull(ApiErrorDto(requestId = "x".repeat(65)).toFailure(500).requestId)
        assertEquals("req_123-abc", ApiErrorDto(requestId = "req_123-abc").toFailure(500).requestId)
    }
}
