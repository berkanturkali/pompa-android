package com.pompa.android.data.repo.fuel

import com.pompa.android.model.fuel.FuelPriceProvider
import com.pompa.android.model.util.Resource
import kotlinx.coroutines.flow.Flow

interface FuelRepository {

    fun fetchAllFuelPricesByCity(
        cityName: String?,
        cityCode: String?,
        provider: String?,
        sortDirection: Int,
        fuelType: Int,
        searchQuery: String
    ): Flow<Resource<List<FuelPriceProvider?>>>

}