package com.pompa.android.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompa.android.data.datastore.PompaUserPrefs
import com.pompa.android.model.settings.SettingsMenuOption
import com.pompa.android.model.settings.SettingsMenuUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val pompaUserPrefs: PompaUserPrefs
) : ViewModel() {

    val menu: StateFlow<List<SettingsMenuUiItem>> =
        pompaUserPrefs.userPreferences.map { prefs ->
            SettingsMenuOption.entries.map { option ->
                SettingsMenuUiItem(
                    option = option,
                    selectedValue = when (option) {
                        SettingsMenuOption.CITY -> {
                            val (_, cityName) = prefs.selectedCity
                            cityName ?: ""
                        }

                        SettingsMenuOption.FAVORITE_PROVIDER -> {
                            val (_, providerName) = prefs.favoriteProvider
                            providerName ?: ""
                        }
                    }
                )
            }
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

}