package com.pompa.android.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pompa.android.data.datastore.PompaFilterPrefs.FilterPreferencesKeys.SORT_DIRECTION
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class HomeScreenFilterPreferences(
    val sortDirection: Int?
)

@Singleton
class PompaFilterPrefs @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val FILTER_PREFS_NAME = "pompa_filter_prefs"
        private const val SELECTED_DIRECTION = "sort_direction"
    }

    private val Context.pompaFiltersDataStore by preferencesDataStore(name = FILTER_PREFS_NAME)

    private val dataStore = context.pompaFiltersDataStore

    val filterPreferences = dataStore.data.catch { exception ->
        exception.printStackTrace()
        emit(emptyPreferences())
    }
        .map { preferences ->
            val direction = preferences[SORT_DIRECTION]
            HomeScreenFilterPreferences(
                sortDirection = direction
            )
        }

    suspend fun setSelectedSort(direction: Int?) {
        dataStore.edit { preferences ->
            preferences[SORT_DIRECTION] = direction ?: 0
        }
    }

    private object FilterPreferencesKeys {
        val SORT_DIRECTION = intPreferencesKey(SELECTED_DIRECTION)
    }
}