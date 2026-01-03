package com.pompa.android.features.providers.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompa.android.data.repo.provider.ProviderRepository
import com.pompa.android.data.util.collectResource
import com.pompa.android.model.brands.Provider
import com.pompa.android.model.util.UIText
import com.pompa.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvidersScreenViewModel @Inject constructor(
    private val repo: ProviderRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    var isLoading = mutableStateOf(false)

    var errorMessage by mutableStateOf<UIText?>(null)

    var providers by mutableStateOf(emptyList<Provider>())

    var selectedProvider by mutableStateOf<Provider?>(null)

    var confirmButtonEnabled by mutableStateOf(false)

    init {
        fetchFuelBrands()
    }

    fun fetchFuelBrands() {
        viewModelScope.launch {
            repo.fetchProviders().collectResource(
                loadingState = isLoading,
                onError = {
                    errorMessage = it
                }
            ) {
                providers = it ?: emptyList()
            }
        }
    }

    fun saveSelectedBrand() {
        selectedProvider?.let {
            userPreferences.setFavoriteProviderId(it.id)
            userPreferences.setFavoriteProviderName(it.name)
        }
    }
}