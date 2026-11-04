package com.pactogg.app.core.environment

data class AppEnvironment(val version: String, val apiConfigured: Boolean)

interface EnvironmentRepository {
    fun current(): AppEnvironment
}
