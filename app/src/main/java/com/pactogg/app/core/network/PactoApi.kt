package com.pactogg.app.core.network

import com.pactogg.app.feature.auth.data.LoginRequest
import com.pactogg.app.feature.auth.data.LogoutRequest
import com.pactogg.app.feature.auth.data.RefreshRequest
import com.pactogg.app.feature.auth.data.RegisterRequest
import com.pactogg.app.feature.auth.data.SessionDto
import com.pactogg.app.feature.connected.data.ContractDto
import com.pactogg.app.feature.connected.data.ContractRequest
import com.pactogg.app.feature.connected.data.EvidenceDto
import com.pactogg.app.feature.connected.data.GroupDto
import com.pactogg.app.feature.connected.data.GroupRequest
import com.pactogg.app.feature.connected.data.LinkRequest
import com.pactogg.app.feature.connected.data.TournamentDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PactoApi : ProfileApi, GroupReadApi, ContractActionsApi, EvidenceReadApi {
    @POST("api/v1/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<Envelope<SessionDto>>

    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<Envelope<SessionDto>>

    @POST("api/v1/auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): Response<Envelope<SessionDto>>

    @POST("api/v1/auth/logout")
    suspend fun logout(@Header("Authorization") bearer: String, @Body body: LogoutRequest): Response<Unit>

    @POST("api/v1/groups")
    suspend fun group(@Header("Authorization") bearer: String,
        @Body body: GroupRequest): Response<Envelope<GroupDto>>

    @POST("api/v1/contracts")
    suspend fun contract(@Header("Authorization") bearer: String,
        @Body body: ContractRequest): Response<Envelope<ContractDto>>

    @GET("api/v1/contracts/{id}")
    suspend fun challenge(@Header("Authorization") bearer: String,
        @Path("id") id: String): Response<Envelope<ContractDto>>

    @POST("api/v1/contracts/{id}/evidence")
    suspend fun link(@Header("Authorization") bearer: String, @Path("id") id: String,
        @Body body: LinkRequest): Response<Envelope<EvidenceDto>>

    @Multipart
    @POST("api/v1/contracts/{id}/evidence")
    suspend fun photo(@Header("Authorization") bearer: String, @Path("id") id: String,
        @Part file: MultipartBody.Part, @Part("type") type: RequestBody): Response<Envelope<EvidenceDto>>

    @GET("api/v1/tournaments")
    suspend fun tournaments(@Header("Authorization") bearer: String, @Query("status") status: String?,
        @Query("game") game: String?, @Query("cursor") cursor: String?,
        @Query("limit") limit: Int = 20): Response<Envelope<List<TournamentDto>>>
}

interface EvidenceReadApi {
    @GET("api/v1/contracts/{id}/evidence")
    suspend fun evidence(@Header("Authorization") bearer: String,
        @Path("id") id: String): Response<Envelope<List<EvidenceDto>>>

}
