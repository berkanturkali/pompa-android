package com.pompa.android.network.service.brand

import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.brands.Brand
import retrofit2.http.GET

interface BrandService {


    @GET(Endpoints.BRANDS_ENDPOINT)
    suspend fun fetchFuelBrands(): retrofit2.Response<BaseApiResponse<List<Brand>>>
}