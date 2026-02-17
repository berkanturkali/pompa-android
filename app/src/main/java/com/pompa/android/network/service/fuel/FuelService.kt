package com.pompa.android.network.service.fuel

import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.fuel.FuelPriceProvider
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FuelService {

    @GET(Endpoints.FETCH_ALL_PRICES_BY_CITY_ENDPOINT)
    suspend fun fetchAllFuelPricesByCity(
        @Query("cityName") cityName: String?,
        @Query("cityCode") cityCode: String?,
        @Query("provider") provider: String?,
        @Query("sortDirection") sortDirection: Int,
        @Query("fuelType") type: Int,
        @Query("q") searchQuery: String
    ): Response<BaseApiResponse<List<FuelPriceProvider?>>>

}