package com.pactogg.app.core.network

import com.pactogg.app.core.push.DeviceRequest
import com.pactogg.app.core.push.DeviceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

/** Registro del teléfono para push (DEV-01, DEV-02). */
interface DeviceApi {
    @PUT("api/v1/devices/me")
    suspend fun registerDevice(@Header("Authorization") bearer: String,
        @Body body: DeviceRequest): Response<Envelope<DeviceResponse>>

    @DELETE("api/v1/devices/me")
    suspend fun removeDevice(@Header("Authorization") bearer: String,
        @Query("fcm_token") token: String): Response<Unit>
}
