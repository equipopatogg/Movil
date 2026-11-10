package com.pactogg.app.feature.account.data

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.checked
import javax.inject.Inject

class ServiceRepository @Inject constructor(private val api: PactoApi) {
    suspend fun describe(): String {
        val value = api.service().checked(200).data
        return "${value.service} · ${value.version}"
    }
    suspend fun healthy(): Boolean = api.health().checked(200).use { it.string() == "Healthy" }
}
