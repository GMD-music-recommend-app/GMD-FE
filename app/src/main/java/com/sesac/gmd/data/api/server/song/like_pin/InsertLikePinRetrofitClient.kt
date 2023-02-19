package com.sesac.gmd.data.api.server.song.like_pin

import com.sesac.gmd.common.util.GMD_BASE_URL
import com.sesac.gmd.common.util.REST_TIMEOUT
import com.sesac.gmd.common.util.TEMP_JWT
import com.sesac.gmd.common.util.X_ACCESS_TOKEN
import com.sesac.gmd.data.api.server.song.SongService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// 핀 공감 & 공감 취소
class InsertLikePinRetrofitClient {
    companion object {
        // TODO: null 일 때 1번만 생성하는 싱글턴 패턴으로 Retrofit 클래스 구현 필요

        private var okHttpClient: OkHttpClient

        private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GMD_BASE_URL)

        val insertLikePinService: SongService
            get() = retrofitBuilder.build().create(SongService::class.java)

        init {
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request()
                    val newRequest: Request = request
                        .newBuilder()
                        .addHeader(X_ACCESS_TOKEN, TEMP_JWT)
                        .build()
                    chain.proceed(newRequest)
                }).addInterceptor(httpInterceptor())
                .connectTimeout(REST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REST_TIMEOUT, TimeUnit.SECONDS)
                .build()
            retrofitBuilder.client(okHttpClient)
        }

        // OkHttp Interceptor 로그 기록 용
        private fun httpInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }
    }
}