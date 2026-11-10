package com.pactogg.app.core.network

import com.pactogg.app.feature.connected.data.ContractAction
import com.pactogg.app.feature.connected.data.ContractDto
import com.pactogg.app.feature.connected.data.GroupDto
import com.pactogg.app.feature.connected.data.JoinRequest
import com.pactogg.app.feature.connected.data.MemberDto
import com.pactogg.app.feature.connected.data.NicknameDto
import com.pactogg.app.feature.connected.data.ProfileDto
import com.pactogg.app.feature.connected.data.ProfilePatch
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ProfileApi {
    @GET("api/v1/users/me")
    suspend fun profile(@Header("Authorization") bearer: String): Response<Envelope<ProfileDto>>
    @PATCH("api/v1/users/me")
    suspend fun updateProfile(@Header("Authorization") bearer: String,
        @Body body: ProfilePatch): Response<Envelope<ProfileDto>>
    @GET("api/v1/users/check-nickname")
    suspend fun nickname(@Query("nickname") nickname: String): Response<Envelope<NicknameDto>>
}
interface GroupReadApi {
    @GET("api/v1/groups")
    suspend fun groups(@Header("Authorization") bearer: String, @Query("q") query: String?,
        @Query("cursor") cursor: String?, @Query("limit") limit: Int = 20): Response<Envelope<List<GroupDto>>>
    @GET("api/v1/groups/{id}")
    suspend fun getGroup(@Header("Authorization") bearer: String,
        @Path("id") id: String): Response<Envelope<GroupDto>>
    @POST("api/v1/groups/join")
    suspend fun join(@Header("Authorization") bearer: String,
        @Body body: JoinRequest): Response<Envelope<GroupDto>>
    @GET("api/v1/groups/{id}/members")
    suspend fun members(@Header("Authorization") bearer: String, @Path("id") id: String,
        @Query("q") query: String?, @Query("cursor") cursor: String?,
        @Query("limit") limit: Int = 20): Response<Envelope<List<MemberDto>>>
}
interface ContractActionsApi {
    @GET("api/v1/groups/{gid}/contracts")
    suspend fun contracts(@Header("Authorization") bearer: String, @Path("gid") id: String,
        @QueryMap filters: Map<String, String>, @Query("cursor") cursor: String?,
        @Query("limit") limit: Int = 20): Response<Envelope<List<ContractDto>>>
    @POST("api/v1/contracts/{id}/accept")
    suspend fun accept(@Header("Authorization") bearer: String, @Path("id") id: String,
        @Body body: ContractAction,
        @Header("Idempotency-Key") operationKey: String): Response<Envelope<ContractDto>>
    @POST("api/v1/contracts/{id}/reject")
    suspend fun reject(@Header("Authorization") bearer: String, @Path("id") id: String,
        @Body body: ContractAction): Response<Envelope<ContractDto>>
}
