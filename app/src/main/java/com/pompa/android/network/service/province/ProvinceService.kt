package com.pompa.android.network.service.province

import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.provinces.Province
import retrofit2.Response
import retrofit2.http.GET

interface ProvinceService {

    @GET(Endpoints.PROVINCES)
    suspend fun getProvinces(): Response<BaseApiResponse<List<Province>>>

}