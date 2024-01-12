package com.sesac.gmd.data.datasource

import com.sesac.gmd.BuildConfig
import com.sesac.gmd.data.api.ManiaDbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val REST_TIMEOUT = 10L

    @Provides
    @Singleton
    fun provideBaseUrl() = "asdf"  // TODO: 이거 맞나..? 

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor) = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(REST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    } else {
        OkHttpClient.Builder()
            .connectTimeout(REST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory() = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ManiaDbApi {
        return retrofit.create(ManiaDbApi::class.java)
    }
}