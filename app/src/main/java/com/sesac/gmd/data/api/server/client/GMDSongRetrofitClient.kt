/*
* Created by gabriel
* date : 22/12/09
* */

package com.sesac.gmd.data.api.server.client

import com.sesac.gmd.data.api.server.service.GMDSongService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://yourplaylistgmd.shop/"

// TODO: 임시 작성 코드. 추후 삭제 필요
// userIdx = 10's JWT
private const val TEMP_JWT = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VySWR4IjoxMCwiaWF0IjoxNjcwODM1MzY3LCJleHAiOjE2NzIzMDY1OTZ9.UuG_dhxswqK7S64NB9ahfHFk5-Ks0XDaPKCGehXetG4"

class GMDSongRetrofitClient {
    companion object {
        // TODO: null 일 때 1번만 생성하는 싱글턴 패턴으로 Retrofit 클래스 구현 필요

        private var okHttpClient: OkHttpClient

        private val gmdSongRetrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)

        val gmdSongService: GMDSongService
            get() = gmdSongRetrofitBuilder.build().create(GMDSongService::class.java)

        init {
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    // Request
                    val request = chain.request()
                    val newRequest: Request = request
                        .newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("JWT", TEMP_JWT)
                        .build()

                    // Response
                    chain.proceed(newRequest)
                }).addInterceptor(httpInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build()
            gmdSongRetrofitBuilder.client(okHttpClient)
        }

        // OkHttp Interceptor 로그 기록 용
        private fun httpInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }
    }
}