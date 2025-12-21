package com.pompa.android.data.repo.fuel

import android.util.Log
import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.fuel.FuelPriceProvider
import com.pompa.android.model.util.Resource
import com.pompa.android.network.service.fuel.FuelService
import com.pompa.android.network.util.ApiUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "FuelRepositoryImpl"
class FuelRepositoryImpl @Inject constructor(
    private val service: FuelService
) : FuelRepository {
    override fun fetchAllFuelPricesByCity(
        cityName: String,
        cityCode: String
    ): Flow<Resource<BaseApiResponse<List<FuelPriceProvider>>>> {
        return ApiUtils.fetchData {
            Log.i(TAG, "fetchAllFuelPricesByCity: here")
            service.fetchAllFuelPricesByCity(cityCode = cityCode, cityName = cityName)
        }
    }
}