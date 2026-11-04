package com.pactogg.app.core.network.mapper

import com.pactogg.app.core.network.dto.ApiErrorDto
import com.pactogg.app.core.result.AppFailure
import com.pactogg.app.core.result.FailureKind

fun ApiErrorDto.toFailure(httpStatus: Int): AppFailure = AppFailure(
    kind = when (httpStatus) {
        401 -> FailureKind.UNAUTHENTICATED
        403 -> FailureKind.FORBIDDEN
        409 -> FailureKind.CONFLICT
        429 -> FailureKind.RATE_LIMITED
        in 500..599 -> FailureKind.UNAVAILABLE
        else -> FailureKind.UNKNOWN
    },
    // Limit correlation IDs to a safe display/log alphabet; never surface arbitrary API text.
    requestId = requestId?.takeIf { it.matches(Regex("[A-Za-z0-9_-]{1,64}")) },
)
