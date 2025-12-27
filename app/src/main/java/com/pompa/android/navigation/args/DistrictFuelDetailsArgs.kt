package com.pompa.android.navigation.args

import com.pompa.android.features.home.model.FuelPriceUiModel
import kotlinx.serialization.Serializable

@Serializable
data class DistrictFuelDetailsArgs(
    val isFavoriteProvider: Boolean,
    val providerName: String,
    val providerLogo: String,
    val districtName: String,
    val fuelPrices: List<FuelPriceUiModel>
)