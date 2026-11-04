package com.pactogg.app.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Proposed envelope; align field names with the approved OpenAPI contract. */
@Serializable
data class ApiErrorDto(
    val code: String? = null,
    @SerialName("request_id") val requestId: String? = null,
)
