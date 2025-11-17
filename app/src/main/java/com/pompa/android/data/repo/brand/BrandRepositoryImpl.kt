package com.pompa.android.data.repo.brand

import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.brands.Brand
import com.pompa.android.model.util.Resource
import com.pompa.android.network.service.brand.BrandService
import com.pompa.android.network.util.ApiUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BrandRepositoryImpl @Inject constructor(
    private val brandService: BrandService
) : BrandRepository {
    override fun fetchFuelBrands(): Flow<Resource<BaseApiResponse<List<Brand>>>> {
        return ApiUtils.fetchData {
            brandService.fetchFuelBrands()
        }
    }
}