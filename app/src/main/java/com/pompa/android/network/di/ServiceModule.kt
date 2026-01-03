package com.pompa.android.network.di

import com.pompa.android.network.service.provider.ProviderService
import com.pompa.android.network.service.fuel.FuelService
import com.pompa.android.network.service.province.ProvinceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @[Provides Singleton]
    fun provideProvinceService(
        retrofit: Retrofit
    ): ProvinceService {
        return retrofit.create()
    }

    @[Provides Singleton]
    fun provideBrandService(
        retrofit: Retrofit
    ): ProviderService {
        return retrofit.create()
    }

    @[Provides Singleton]
    fun provideFuelService(
        retrofit: Retrofit
    ): FuelService {
        return retrofit.create()
    }
}