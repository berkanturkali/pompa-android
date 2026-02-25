package com.pompa.android.features.home.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompa.android.data.datastore.PompaFilterPrefs
import com.pompa.android.data.datastore.PompaUserPrefs
import com.pompa.android.data.repo.fuel.FuelRepository
import com.pompa.android.data.util.collectResource
import com.pompa.android.model.FuelFilterDataSource
import com.pompa.android.model.FuelFilterItem
import com.pompa.android.model.fuel.FuelPriceProvider
import com.pompa.android.model.util.UIText
import com.pompa.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val TAG = "HomeScreenViewModel"

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val fuelRepo: FuelRepository,
    private val userPreferences: UserPreferences,
    val pompaFilterPrefs: PompaFilterPrefs,
    private val pompaUserPrefs: PompaUserPrefs,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {

    var isLoading = mutableStateOf(false)

    var errorMessage by mutableStateOf<UIText?>(null)

    var results by mutableStateOf(emptyList<FuelPriceProvider>())
        private set

    var sortDirection: Int = 0

    var fuelType: Int = 0

    var cityCode: String? = null

    var cityName: String? = null

    var favProviderName: String? = null

    var fuelFilters = FuelFilterDataSource.getFilters(context)
    var selectedFuelFilter by mutableStateOf<FuelFilterItem?>(
        null
    )

    var date by mutableStateOf(getDate())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val debouncedSearchQuery =
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()

    init {
        viewModelScope.launch {
            combine(
                pompaFilterPrefs.filterPreferences,
                pompaUserPrefs.userPreferences,
            ) { filterPrefs, userPrefs ->
                Pair(filterPrefs, userPrefs)
            }.collectLatest { (filterPrefs, userPrefs) ->
                sortDirection = filterPrefs.sortDirection
                fuelType = filterPrefs.fuelType
                selectedFuelFilter = fuelFilters.first {
                    it.type.value == fuelType
                }
                val (cityCode, cityName) = userPrefs.selectedCity
                val (favoriteLogo, favoriteName) = userPrefs.favoriteProvider

                this@HomeScreenViewModel.apply {
                    this.cityName = cityName
                    this.cityCode = cityCode
                    this.favProviderName = favoriteName
                }
                fetchPrices(
                    cityCode = cityCode,
                    cityName = cityName,
                    provider = favoriteName,
                    sortDirection = sortDirection,
                    fuelType = fuelType
                )
            }
        }
    }

    fun fetchPrices(
        cityCode: String?,
        cityName: String?,
        provider: String?,
        sortDirection: Int,
        fuelType: Int,
    ) {
        isLoading.value = true
        results = emptyList()
        getDate()
        viewModelScope.launch {
            fuelRepo.fetchAllFuelPricesByCity(
                cityCode = cityCode,
                cityName = cityName,
                provider = provider,
                sortDirection = sortDirection,
                fuelType = fuelType,

                ).collectResource(
                onError = {
                    errorMessage = it
                },
                loadingState = isLoading
            ) {
                results = it?.filterNotNull() ?: emptyList()
            }
        }
    }

    fun setSelectedFuelType(type: Int) {
        viewModelScope.launch {
            pompaFilterPrefs.setSelectedFuelType(type)
        }
    }

    fun getSelectedProvider() = userPreferences.getFavoriteProviderName()

    fun navigateToGoogleMapsWithLocation(
        provider: String,
        districtName: String,
        cityName: String,
        zoom: Int = 18,
    ) {
        val query = Uri.encode("$provider $districtName ${cityName.lowercase()}")

        val geoUri = "geo:0,0".toUri().buildUpon()
            .appendQueryParameter("q", query)
            .appendQueryParameter("z", zoom.toString())
            .build()
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
            setPackage("com.google.android.apps.maps")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }


        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            val browserUri = ("https://www.google.com/maps/search/?" +
                    "api=1&query=${query}&zoom=$zoom").toUri()
            val browserIntent = Intent(Intent.ACTION_VIEW, browserUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(browserIntent)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }


    fun getFilteredResults(query: String): List<FuelPriceProvider> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return results

        val lower = trimmed.lowercase()

        return results
            .map { provider ->
                val filteredStations = provider.data.filter { station ->
                    val district = (station?.districtName ?: "").lowercase()
                    val name = (station?.brand ?: "").lowercase()
                    district.contains(lower) || name.contains(lower)
                }
                provider.copy(data = filteredStations)
            }
            .filter { it.data.isNotEmpty() }
    }


    fun getDate(
        pattern: String = "dd/MM/yyyy",
        date: Date = Date()
    ): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }

}