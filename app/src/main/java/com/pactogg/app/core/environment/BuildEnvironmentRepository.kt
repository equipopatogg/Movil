package com.pactogg.app.core.environment

import com.pactogg.app.BuildConfig
import com.pactogg.app.core.network.ApiClientFactory
import javax.inject.Inject

class BuildEnvironmentRepository @Inject constructor() : EnvironmentRepository {
    override fun current() = AppEnvironment(
        version = BuildConfig.VERSION_NAME,
        apiConfigured = ApiClientFactory.isValidBaseUrl(BuildConfig.API_BASE_URL),
    )
}
