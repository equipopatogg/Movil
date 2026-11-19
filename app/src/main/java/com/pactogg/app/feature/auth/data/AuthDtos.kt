package com.pactogg.app.feature.auth.data

import com.pactogg.app.feature.auth.domain.Account
import com.pactogg.app.feature.auth.domain.Session
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginRequest(val email: String, val password: String)

@Serializable
class RegisterRequest(
    val email: String,
    val password: String,
    val nickname: String,
    @SerialName("accept_privacy") val acceptPrivacy: Boolean,
    @SerialName("privacy_version") val privacyVersion: String,
)

/** AUTH-02: el backend valida el ID token; apodo y consentimiento solo van al crear la cuenta. */
@Serializable
class GoogleRequest(
    @SerialName("id_token") val idToken: String,
    val nickname: String? = null,
    @SerialName("accept_privacy") val acceptPrivacy: Boolean? = null,
    @SerialName("privacy_version") val privacyVersion: String? = null,
)

/** AUTH-05: token del enlace o código de 6 dígitos del correo. */
@Serializable
class VerifyEmailRequest(val token: String)

@Serializable
class VerifyEmailResponse(@SerialName("email_verified") val emailVerified: Boolean = false)

@Serializable
class RefreshRequest(@SerialName("refresh_token") val refreshToken: String)

@Serializable
class LogoutRequest(@SerialName("refresh_token") val refreshToken: String, val all: Boolean = false)

@Serializable
class UserDto(val id: String, val email: String, val nickname: String)

@Serializable
class SessionDto(
    val user: UserDto,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Long,
) {
    fun toDomain(now: Long): Session {
        require(user.id.matches(Regex("[a-fA-F0-9]{24}")))
        require(user.email.isNotBlank() && user.nickname.isNotBlank())
        require(accessToken.isNotBlank() && refreshToken.isNotBlank())
        require(!accessToken.any(Char::isWhitespace) && !refreshToken.any(Char::isWhitespace))
        require(tokenType.equals("Bearer", ignoreCase = true) && expiresIn in 1..86400)
        return Session(Account(user.id, user.email, user.nickname), accessToken, refreshToken,
            now + expiresIn * 1000)
    }
}
