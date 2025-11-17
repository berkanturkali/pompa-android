package com.pompa.android.data.repo.brand

import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.brands.Brand
import com.pompa.android.model.util.Resource
import kotlinx.coroutines.flow.Flow

interface BrandRepository {


    fun fetchFuelBrands(): Flow<Resource<BaseApiResponse<List<Brand>>>>
}