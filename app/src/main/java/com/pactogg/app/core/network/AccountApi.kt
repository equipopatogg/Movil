package com.pactogg.app.core.network

import com.pactogg.app.feature.auth.data.ForgotPasswordRequest
import com.pactogg.app.feature.auth.data.GoogleRequest
import com.pactogg.app.feature.auth.data.ResetPasswordRequest
import com.pactogg.app.feature.auth.data.ResetPasswordResponse
import com.pactogg.app.feature.auth.data.SessionDto
import com.pactogg.app.feature.auth.data.VerifyEmailRequest
import com.pactogg.app.feature.auth.data.VerifyEmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/** Acceso con Google (AUTH-02), verificación de correo (AUTH-05) y recuperar contraseña (AUTH-06, AUTH-07). */
interface AccountApi {
    @POST("api/v1/auth/google")
    suspend fun google(@Body body: GoogleRequest): Response<Envelope<SessionDto>>

    @POST("api/v1/auth/verify-email")
    suspend fun verifyEmail(@Body body: VerifyEmailRequest): Response<Envelope<VerifyEmailResponse>>

    @POST("api/v1/auth/verify-email/resend")
    suspend fun resendVerification(@Header("Authorization") bearer: String): Response<Unit>

    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): Response<Unit>

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): Response<Envelope<ResetPasswordResponse>>
}
