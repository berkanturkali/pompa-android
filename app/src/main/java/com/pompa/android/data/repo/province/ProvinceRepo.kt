package com.pompa.android.data.repo.province

import com.pompa.android.model.provinces.Province
import com.pompa.android.model.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProvinceRepo {

    fun getProvinces(): Flow<Resource<List<Province>>>
}