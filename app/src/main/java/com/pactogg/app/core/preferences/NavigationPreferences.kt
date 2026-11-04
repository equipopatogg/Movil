package com.pactogg.app.core.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.map

private val Context.navigationDataStore by preferencesDataStore(name = "navigation")

/** Non-sensitive preferences only. DataStore does not encrypt tokens by itself. */
@Singleton
class NavigationPreferences @Inject constructor(@param:ApplicationContext private val context: Context) {
    private val lastTabKey = stringPreferencesKey("last_tab")
    val lastTab = context.navigationDataStore.data.map { it[lastTabKey] }

    suspend fun saveLastTab(route: String) {
        context.navigationDataStore.edit { it[lastTabKey] = route }
    }

    suspend fun clear() { context.navigationDataStore.edit { it.clear() } }
}
