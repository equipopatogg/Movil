package com.pactogg.app.feature.account.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.pactogg.app.feature.connected.data.ProfileStatsDto

@Serializable
class TokenRequest(val token: String)
@Serializable
class EmailRequest(val email: String)
@Serializable
class ResetRequest(val token: String, @SerialName("new_password") val password: String)
@Serializable
class PasswordRequest(@SerialName("current_password") val current: String,
    @SerialName("new_password") val password: String)
@Serializable
class GoogleRequest(@SerialName("id_token") val token: String, val nickname: String? = null,
    @SerialName("accept_privacy") val accept: Boolean? = null,
    @SerialName("privacy_version") val version: String? = null)
@Serializable
class ConsentRequest(val version: String)
@Serializable
class VerifiedResponse(@SerialName("email_verified") val verified: Boolean)
@Serializable
class ResetResponse(@SerialName("password_reset") val reset: Boolean)
@Serializable
class ConsentResponse(val version: String, @SerialName("accepted_at") val acceptedAt: String)
@Serializable
class PublicProfileDto(val id: String, val nickname: String,
    @SerialName("avatar_id") val avatar: String, val stats: ProfileStatsDto)
@Serializable
class EditContractRequest(val version: Int, val game: String, val condition: String, val reward: String)
@Serializable
class DeviceRequest(@SerialName("fcm_token") val token: String, val platform: String = "android",
    @SerialName("app_version") val version: String? = null)
@Serializable
class DeviceResponse(val id: String, val platform: String,
    @SerialName("updated_at") val updatedAt: String)
@Serializable
class PreferencesDto(val retos: Boolean, val votos: Boolean, val torneos: Boolean, val compromisos: Boolean)
@Serializable
class AuditDto(val id: String, val action: String, @SerialName("actor_nickname") val actor: String? = null,
    @SerialName("created_at") val createdAt: String, val hash: String)
@Serializable
class ServiceDto(val service: String, val version: String)
