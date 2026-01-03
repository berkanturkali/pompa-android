package com.pompa.android.data.repo.provider

import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.brands.Provider
import com.pompa.android.model.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProviderRepository {


    fun fetchProviders(): Flow<Resource<BaseApiResponse<List<Provider>>>>
}