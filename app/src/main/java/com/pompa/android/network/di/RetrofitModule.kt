package com.pompa.android.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pompa.android.BuildConfig
import com.pompa.android.util.DeviceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private const val TIMEOUT_DURATION = 2L

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
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
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(if (DeviceManager.checkIfTheDeviceIsEmulator()) BuildConfig.EMULATOR_BASE_URL else BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

}