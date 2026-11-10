package com.pactogg.app.core.network

import org.junit.experimental.categories.Category
import com.pactogg.app.testing.SecurityChecks
import com.pactogg.app.core.network.dto.ApiErrorDto
import com.pactogg.app.core.network.mapper.toFailure
import com.pactogg.app.core.result.FailureKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import com.pactogg.app.feature.connected.presentation.userMessage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

@Category(SecurityChecks::class)
class ApiErrorMapperTest {
    @Test
    fun CON_01_smallGroupConflictExplainsMinimumWithoutExposingServerText() {
        val body = """{"code":"GROUP_TOO_SMALL","message":"private-server-detail"}"""
            .toResponseBody("application/json".toMediaType())
        val failure = Response.error<Unit>(409, body).failure()
        assertEquals("GROUP_TOO_SMALL", failure.code)
        assertEquals("El grupo necesita al menos 3 integrantes para crear retos. " +
            "Invita a más personas y vuelve a consultar el grupo.", failure.userMessage())
        assertNull(failure.displayMessage)
    }

    @Test
    fun CON_01_unknownConflictDoesNotClaimGroupIsTooSmall() {
        assertEquals("La información cambió. Actualiza la pantalla antes de continuar.",
            ApiFailure(409, "UNRECOGNIZED_CONFLICT").userMessage())
    }
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
