package com.pompa.android.features.brands.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompa.android.data.repo.brand.BrandRepository
import com.pompa.android.data.util.collectResource
import com.pompa.android.model.brands.Brand
import com.pompa.android.model.util.UIText
import com.pompa.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FuelBrandsScreenViewModel @Inject constructor(
    private val repo: BrandRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    var isLoading = mutableStateOf(false)

    var errorMessage by mutableStateOf<UIText?>(null)

    var brands by mutableStateOf(emptyList<Brand>())

    var selectedBrand by mutableStateOf<Brand?>(null)

    var confirmButtonEnabled by mutableStateOf(false)

    init {
        fetchFuelBrands()
    }

    fun fetchFuelBrands() {
        viewModelScope.launch {
            repo.fetchFuelBrands().collectResource(
                loadingState = isLoading,
                onError = {
                    errorMessage = it
                }
            ) {
                brands = it ?: emptyList()
            }
        }
    }

    fun saveSelectedBrand() {
        selectedBrand?.let {
            userPreferences.setFavoriteProviderId(it.id)
            userPreferences.setFavoriteProviderName(it.name)
        }
    }
}