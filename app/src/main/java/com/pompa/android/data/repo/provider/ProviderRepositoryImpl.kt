package com.pompa.android.data.repo.provider

import com.pompa.android.model.base.BaseApiResponse
import com.pompa.android.model.brands.Provider
import com.pompa.android.model.util.Resource
import com.pompa.android.network.service.provider.ProviderService
import com.pompa.android.network.util.ApiUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProviderRepositoryImpl @Inject constructor(
    private val providerService: ProviderService
) : ProviderRepository {
    override fun fetchProviders(): Flow<Resource<BaseApiResponse<List<Provider>>>> {
        return ApiUtils.fetchData {
            providerService.fetchProviders()
        }
    }
}