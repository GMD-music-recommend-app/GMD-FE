/*
* Created by gabriel
* date : 22/12/21
* */
package com.sesac.gmd.data.api.server.song.get_pininfo

import com.sesac.gmd.common.util.GMD_BASE_URL
import com.sesac.gmd.data.api.server.song.SongService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// 핀 정보 반환

// TODO: 임시 작성 코드. 추후 삭제 필요
// userIdx = 5's JWT
private const val TEMP_JWT = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VySWR4Ijo1LCJpYXQiOjE2NzAwODQ3NzMsImV4cCI6MTY3MTU1NjAwMn0.SxLK8YxY6KJwNmuva0dC3YlhfYeBauA2IhS5F44mYRY"

class GetPinInfoRetrofitClient {
    companion object {
        // TODO: null 일 때 1번만 생성하는 싱글턴 패턴으로 Retrofit 클래스 구현 필요

        private var okHttpClient: OkHttpClient

        private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GMD_BASE_URL)

        val getPinInfoService: SongService
            get() = retrofitBuilder.build().create(SongService::class.java)

        init {
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request()
                    val newRequest: Request = request
                        .newBuilder()
//                        .addHeader("Accept", "application/json")
                        .addHeader("X-ACCESS-TOKEN", TEMP_JWT)
                        .build()
                    chain.proceed(newRequest)
                }).addInterceptor(httpInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
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