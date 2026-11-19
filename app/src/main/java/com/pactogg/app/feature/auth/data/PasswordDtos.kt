package com.pactogg.app.feature.auth.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ForgotPasswordRequest(val email: String)

@Serializable
class ResetPasswordRequest(val token: String, @SerialName("new_password") val newPassword: String)

@Serializable
class ResetPasswordResponse(@SerialName("password_reset") val passwordReset: Boolean = false)
