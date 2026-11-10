package com.pactogg.app.core.network

import com.pactogg.app.feature.account.data.AuditDto
import com.pactogg.app.feature.account.data.DeviceRequest
import com.pactogg.app.feature.account.data.DeviceResponse
import com.pactogg.app.feature.account.data.EditContractRequest
import com.pactogg.app.feature.account.data.PreferencesDto
import com.pactogg.app.feature.account.data.PublicProfileDto
import com.pactogg.app.feature.account.data.ServiceDto
import com.pactogg.app.feature.connected.data.ContractDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MaintenanceApi : DeviceApi, ServiceApi {
    @GET("api/v1/users/{id}")
    suspend fun publicProfile(@Header("Authorization") bearer: String,
        @Path("id") id: String): Response<Envelope<PublicProfileDto>>
    @PATCH("api/v1/contracts/{id}")
    suspend fun editContract(@Header("Authorization") bearer: String, @Path("id") id: String,
        @Body body: EditContractRequest): Response<Envelope<ContractDto>>
    @DELETE("api/v1/contracts/{id}")
    suspend fun cancelContract(@Header("Authorization") bearer: String, @Path("id") id: String,
        @Query("version") version: Int): Response<Unit>
    @DELETE("api/v1/notifications/{id}")
    suspend fun deleteNotification(@Header("Authorization") bearer: String, @Path("id") id: String): Response<Unit>
    @GET("api/v1/groups/{gid}/audit")
    suspend fun audit(@Header("Authorization") bearer: String, @Path("gid") id: String,
        @Query("cursor") cursor: String?, @Query("limit") limit: Int = 20): Response<Envelope<List<AuditDto>>>
}

interface DeviceApi {
    @PUT("api/v1/devices/me")
    suspend fun registerDevice(@Header("Authorization") bearer: String,
        @Body body: DeviceRequest): Response<Envelope<DeviceResponse>>
    @DELETE("api/v1/devices/me")
    suspend fun removeDevice(@Header("Authorization") bearer: String, @Query("fcm_token") token: String): Response<Unit>
    @GET("api/v1/devices/me/preferences")
    suspend fun preferences(@Header("Authorization") bearer: String): Response<Envelope<PreferencesDto>>
    @PATCH("api/v1/devices/me/preferences")
    suspend fun preferences(@Header("Authorization") bearer: String,
        @Body body: PreferencesDto): Response<Envelope<PreferencesDto>>
}

interface ServiceApi {
    @GET("/")
    suspend fun service(): Response<Envelope<ServiceDto>>
    @GET("health")
    suspend fun health(): Response<ResponseBody>
}
