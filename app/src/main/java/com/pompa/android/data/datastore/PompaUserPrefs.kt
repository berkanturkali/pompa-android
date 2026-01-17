package com.pompa.android.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pompa.android.data.datastore.PompaUserPrefs.UserPrefsKeys.CITY_CODE
import com.pompa.android.data.datastore.PompaUserPrefs.UserPrefsKeys.CITY_NAME
import com.pompa.android.data.datastore.PompaUserPrefs.UserPrefsKeys.FUEL_PROVIDER_LOGO
import com.pompa.android.data.datastore.PompaUserPrefs.UserPrefsKeys.FUEL_PROVIDER_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


data class UserPreferences(
    val selectedCity: Pair<String?, String?>,
    val favoriteProvider: Pair<String?, String?>
)

@Singleton
class PompaUserPrefs @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val USER_PREFS_NAME = "pompa_user_prefs"
        private const val SELECTED_CITY_NAME = "selected_city_name"
        private const val SELECTED_CITY_CODE = "selected_city_code"
        private const val FAVORITE_FUEL_PROVIDER_NAME = "favorite_fuel_provider_name"
        private const val FAVORITE_FUEL_PROVIDER_LOGO = "favorite_fuel_provider_logo"
    }

    private val Context.pompaUserPrefsDataStore by preferencesDataStore(name = USER_PREFS_NAME)

    private val dataStore = context.pompaUserPrefsDataStore

    val userPreferences = dataStore.data.catch {
        it.printStackTrace()
        emit(emptyPreferences())
    }
        .map { preferences ->
            val cityName = preferences[CITY_NAME]
            val cityCode = preferences[CITY_CODE]
            val favoriteProviderName = preferences[FUEL_PROVIDER_NAME]
            val favoriteProviderLogo = preferences[FUEL_PROVIDER_LOGO]
            UserPreferences(
                selectedCity = Pair(cityCode, cityName),
                favoriteProvider = Pair(favoriteProviderLogo, favoriteProviderName)
            )
        }

    suspend fun setSelectedCity(cityCode: String, cityName: String) {
        dataStore.edit { preferences ->
            preferences[CITY_NAME] = cityName
            preferences[CITY_CODE] = cityCode
        }
    }

    suspend fun setSelectedProvider(providerName: String, providerLogo: String) {
        dataStore.edit { preferences ->
            preferences[FUEL_PROVIDER_NAME] = providerName
            preferences[FUEL_PROVIDER_LOGO] = providerLogo
        }
    }


    private object UserPrefsKeys {
        val CITY_NAME = stringPreferencesKey(SELECTED_CITY_NAME)
        val CITY_CODE = stringPreferencesKey(SELECTED_CITY_CODE)
        val FUEL_PROVIDER_NAME = stringPreferencesKey(FAVORITE_FUEL_PROVIDER_NAME)
        val FUEL_PROVIDER_LOGO = stringPreferencesKey(FAVORITE_FUEL_PROVIDER_LOGO)
    }
}