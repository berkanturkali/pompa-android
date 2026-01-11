package com.pompa.android.features.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompa.android.data.datastore.PompaFilterPrefs
import com.pompa.android.data.repo.fuel.FuelRepository
import com.pompa.android.data.util.collectResource
import com.pompa.android.model.fuel.FuelPriceProvider
import com.pompa.android.model.util.UIText
import com.pompa.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeScreenViewModel"

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val fuelRepo: FuelRepository,
    private val userPreferences: UserPreferences,
    val pompaFilterPrefs: PompaFilterPrefs,
) : ViewModel() {

    var isLoading = mutableStateOf(false)

    var errorMessage by mutableStateOf<UIText?>(null)

    var providers by mutableStateOf(emptyList<FuelPriceProvider>())

    var sortDirection: Int = 0

    var fuelType: Int = 0

    init {
        viewModelScope.launch {
            pompaFilterPrefs.filterPreferences.collect { filterPreferences ->
                sortDirection = filterPreferences.sortDirection
                fuelType = filterPreferences.fuelType
                fetchPrices()
            }
        }
    }

    fun fetchPrices() {
        isLoading.value = true
        providers = emptyList()
        viewModelScope.launch {
            fuelRepo.fetchAllFuelPricesByCity(
                cityCode = userPreferences.getSelectedProvinceCode()!!,
                cityName = userPreferences.getSelectedProvinceName()!!,
                provider = userPreferences.getFavoriteProviderName()!!,
                sortDirection = sortDirection,
                fuelType = fuelType

            ).collectResource(
                onError = {
                    errorMessage = it
                },
                loadingState = isLoading
            ) {
                providers = it?.filterNotNull() ?: emptyList()
            }
        }
    }

    fun setSelectedFuelType(type: Int) {
        viewModelScope.launch {
            pompaFilterPrefs.setSelectedFuelType(type)
        }
    }

    fun getSelectedProvider() = userPreferences.getFavoriteProviderName()

}