package com.pactogg.app.core.network

import com.pactogg.app.feature.account.data.ConsentRequest
import com.pactogg.app.feature.account.data.ConsentResponse
import com.pactogg.app.feature.account.data.EmailRequest
import com.pactogg.app.feature.account.data.GoogleRequest
import com.pactogg.app.feature.account.data.PasswordRequest
import com.pactogg.app.feature.account.data.ResetRequest
import com.pactogg.app.feature.account.data.ResetResponse
import com.pactogg.app.feature.account.data.TokenRequest
import com.pactogg.app.feature.account.data.VerifiedResponse
import com.pactogg.app.feature.auth.data.SessionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountApi {
    @POST("api/v1/auth/google")
    suspend fun google(@Body body: GoogleRequest): Response<Envelope<SessionDto>>
    @POST("api/v1/auth/verify-email")
    suspend fun verifyEmail(@Body body: TokenRequest): Response<Envelope<VerifiedResponse>>
    @POST("api/v1/auth/verify-email/resend")
    suspend fun resendEmail(@Header("Authorization") bearer: String): Response<Unit>
    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(@Body body: EmailRequest): Response<Unit>
    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body body: ResetRequest): Response<Envelope<ResetResponse>>
    @POST("api/v1/users/me/password")
    suspend fun changePassword(@Header("Authorization") bearer: String,
        @Body body: PasswordRequest): Response<Envelope<SessionDto>>
    @POST("api/v1/users/me/consent")
    suspend fun consent(@Header("Authorization") bearer: String,
        @Body body: ConsentRequest): Response<Envelope<ConsentResponse>>
}
