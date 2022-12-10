/*
* Created by gabriel
* date : 22/12/06
* */

package com.sesac.gmd.data.api.maniadb

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://www.maniadb.com/api/search/"

class ManiaDBRetrofitClient {
    companion object {
        // TODO: null 일 때 1번만 생성하는 싱글턴 패턴으로 Retrofit 클래스 구현 필요

        private var okHttpClient: OkHttpClient

        private val maniaRetrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)

        val maniaDBService: ManiaDBService
            get() = maniaRetrofitBuilder.build().create(ManiaDBService::class.java)

        init {
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request()
                    val newRequest: Request = request.newBuilder().build()
                    chain.proceed(newRequest)
                }).addInterceptor(httpInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS).build()
            maniaRetrofitBuilder.client(okHttpClient)
        }

        // OkHttp Interceptor 로그 기록 용
        private fun httpInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }
    }
}