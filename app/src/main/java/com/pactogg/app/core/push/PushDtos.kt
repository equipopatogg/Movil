package com.pactogg.app.core.push

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DeviceRequest(@SerialName("fcm_token") val token: String, val platform: String = "android",
    @SerialName("app_version") val version: String? = null)

@Serializable
class DeviceResponse(val id: String, val platform: String,
    @SerialName("updated_at") val updatedAt: String? = null)
