package com.pompa.android.features.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompa.android.data.repo.fuel.FuelRepository
import com.pompa.android.data.util.collectResource
import com.pompa.android.model.fuel.FuelPriceProvider
import com.pompa.android.model.util.UIText
import com.pompa.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val fuelRepo: FuelRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    var isLoading = mutableStateOf(false)

    var errorMessage by mutableStateOf<UIText?>(null)

    var providers by mutableStateOf(emptyList<FuelPriceProvider>())

    init {
        fetchPrices()
    }

    fun fetchPrices() {
        viewModelScope.launch {
            fuelRepo.fetchAllFuelPricesByCity(
                cityCode = userPreferences.getSelectedProvinceCode()!!,
                cityName = userPreferences.getSelectedProvinceName()!!,
                provider = userPreferences.getFavoriteProviderName()!!,
            ).collectResource(
                onError = {

                },
                loadingState = isLoading
            ) {
                providers = it ?: emptyList()
            }
        }
    }

    fun getSelectedProvince() = userPreferences.getSelectedProvinceName()

    fun getSelectedProvider() = userPreferences.getFavoriteProviderName()

}