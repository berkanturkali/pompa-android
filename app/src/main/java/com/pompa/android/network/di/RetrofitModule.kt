package com.pompa.android.network.di

import com.pompa.android.BuildConfig
import com.pompa.android.util.DeviceManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private const val TIMEOUT_DURATION = 2L

    @[Provides Singleton]
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @[Provides Singleton]
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        return interceptor
    }

    @[Provides Singleton]
    fun provideOkHttpClient(
        okHttpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_DURATION, TimeUnit.MINUTES)
            .readTimeout(TIMEOUT_DURATION, TimeUnit.MINUTES)
            .writeTimeout(TIMEOUT_DURATION, TimeUnit.MINUTES)
            .addInterceptor(okHttpLoggingInterceptor)
            .build()
    }

    @[Provides Singleton]
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(if (DeviceManager.checkIfTheDeviceIsEmulator()) BuildConfig.POMPA_EMULATOR_BASE_URL else BuildConfig.POMPA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(moshi).asLenient()
            )
            .build()
    }

}