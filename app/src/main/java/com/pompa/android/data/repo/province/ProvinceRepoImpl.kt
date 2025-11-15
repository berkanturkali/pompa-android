package com.pompa.android.data.repo.province

import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.provinces.Province
import com.pompa.android.model.util.Resource
import com.pompa.android.network.service.province.ProvinceService
import com.pompa.android.network.util.ApiUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProvinceRepoImpl @Inject constructor(
    private val provinceService: ProvinceService
) : ProvinceRepo {
    override fun getProvinces(): Flow<Resource<BaseApiResponse<List<Province>>>> {
        return ApiUtils.fetchData {
            provinceService.getProvinces()
        }
    }
}