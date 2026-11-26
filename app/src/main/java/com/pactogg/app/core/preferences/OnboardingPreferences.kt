package com.pactogg.app.core.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.onboardingDataStore by preferencesDataStore(name = "onboarding")

/** PL-15: si este teléfono ya vio el tutorial. No es un dato sensible y no se borra al cerrar sesión. */
interface TutorialStore {
    val seen: Flow<Boolean>
    suspend fun markSeen()
}

@Singleton
class OnboardingPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : TutorialStore {
    private val seenKey = booleanPreferencesKey("tutorial_v1_seen")
    override val seen = context.onboardingDataStore.data.map { it[seenKey] == true }

    override suspend fun markSeen() {
        context.onboardingDataStore.edit { it[seenKey] = true }
    }
}
