package com.pompa.android.data.repo.di

import com.pompa.android.data.repo.provider.ProviderRepository
import com.pompa.android.data.repo.provider.ProviderRepositoryImpl
import com.pompa.android.data.repo.fuel.FuelRepository
import com.pompa.android.data.repo.fuel.FuelRepositoryImpl
import com.pompa.android.data.repo.province.ProvinceRepo
import com.pompa.android.data.repo.province.ProvinceRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindProvinceRepo(
        provinceRepoImpl: ProvinceRepoImpl
    ): ProvinceRepo

    @Binds
    fun bindBrandRepository(
        brandRepositoryImpl: ProviderRepositoryImpl
    ): ProviderRepository


    @Binds
    fun bindFuelRepository(
        fuelRepositoryImpl: FuelRepositoryImpl
    ): FuelRepository

}