package com.pompa.android.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pompa.android.data.datastore.PompaFilterPrefs.FilterPreferencesKeys.FUEL_TYPE
import com.pompa.android.data.datastore.PompaFilterPrefs.FilterPreferencesKeys.SORT_DIRECTION
import com.pompa.android.features.sort.model.SortDirection
import com.pompa.android.model.FuelType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class HomeScreenFilterPreferences(
    val sortDirection: Int,
    val fuelType: Int
)

@Singleton
class PompaFilterPrefs @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val FILTER_PREFS_NAME = "pompa_filter_prefs"
        private const val SELECTED_DIRECTION = "sort_direction"
        private const val SELECTED_FUEL_TYPE = "fuel_type"
    }

    private val Context.pompaFiltersDataStore by preferencesDataStore(name = FILTER_PREFS_NAME)

    private val dataStore = context.pompaFiltersDataStore

    val filterPreferences = dataStore.data.catch { exception ->
        exception.printStackTrace()
        emit(emptyPreferences())
    }
        .map { preferences ->
            val direction = preferences[SORT_DIRECTION]
            val fuelType = preferences[FUEL_TYPE]
            HomeScreenFilterPreferences(
                sortDirection = direction ?: SortDirection.ASCENDING.value,
                fuelType = fuelType ?: FuelType.ALL.value
            )
        }

    suspend fun setSelectedSort(direction: Int?) {
        dataStore.edit { preferences ->
            preferences[SORT_DIRECTION] = direction ?: 0
        }
    }

    suspend fun setSelectedFuelType(type: Int) {
        dataStore.edit { preferences ->
            preferences[FUEL_TYPE] = type
        }
    }

    suspend fun getSelectedSortDirection(): Int {
        val prefs = dataStore.data.first()
        return prefs[SORT_DIRECTION] ?: SortDirection.ASCENDING.value
    }

    suspend fun getSelectedFuelType(): Int {
        val prefs = dataStore.data.first()
        return prefs[FUEL_TYPE] ?: FuelType.ALL.value
    }

    private object FilterPreferencesKeys {
        val SORT_DIRECTION = intPreferencesKey(SELECTED_DIRECTION)
        val FUEL_TYPE = intPreferencesKey(SELECTED_FUEL_TYPE)
    }
}