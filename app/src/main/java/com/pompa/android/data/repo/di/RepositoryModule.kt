package com.pompa.android.data.repo.di

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

}