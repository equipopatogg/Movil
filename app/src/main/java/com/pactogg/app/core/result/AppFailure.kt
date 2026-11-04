package com.pactogg.app.core.result

enum class FailureKind { UNAUTHENTICATED, FORBIDDEN, CONFLICT, RATE_LIMITED, UNAVAILABLE, UNKNOWN }
data class AppFailure(val kind: FailureKind, val requestId: String? = null)
