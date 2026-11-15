package com.pactogg.app.core.network

import com.pactogg.app.feature.auth.data.GoogleRequest
import com.pactogg.app.feature.auth.data.SessionDto
import com.pactogg.app.feature.auth.data.VerifyEmailRequest
import com.pactogg.app.feature.auth.data.VerifyEmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/** Acceso con Google (AUTH-02) y verificación de correo (AUTH-05). */
interface AccountApi {
    @POST("api/v1/auth/google")
    suspend fun google(@Body body: GoogleRequest): Response<Envelope<SessionDto>>

    @POST("api/v1/auth/verify-email")
    suspend fun verifyEmail(@Body body: VerifyEmailRequest): Response<Envelope<VerifyEmailResponse>>

    @POST("api/v1/auth/verify-email/resend")
    suspend fun resendVerification(@Header("Authorization") bearer: String): Response<Unit>
}
