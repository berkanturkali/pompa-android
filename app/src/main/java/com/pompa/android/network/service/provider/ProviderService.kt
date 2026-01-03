package com.pompa.android.network.service.provider

import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.brands.Provider
import retrofit2.http.GET

interface ProviderService {


    @GET(Endpoints.BRANDS_ENDPOINT)
    suspend fun fetchProviders(): retrofit2.Response<BaseApiResponse<List<Provider>>>
}