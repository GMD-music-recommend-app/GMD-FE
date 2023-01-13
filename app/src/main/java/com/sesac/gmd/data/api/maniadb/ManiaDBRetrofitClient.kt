/**
* Created by 조진수
* date : 22/12/06
*/
package com.sesac.gmd.data.api.maniadb

import com.sesac.gmd.common.util.MANIADB_BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class ManiaDBRetrofitClient {
    companion object {
        // TODO: null 일 때 1번만 생성하는 싱글턴 패턴으로 Retrofit 클래스 구현 필요

        private var okHttpClient: OkHttpClient

        private val maniaRetrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(MANIADB_BASE_URL)

        val maniaDBService: ManiaDBService
            get() = maniaRetrofitBuilder.build().create(ManiaDBService::class.java)


        // TODO: 디버그 일 때랑 아닐 때 분기해서 구현 필요 
        init {
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request()
                    val newRequest: Request = request.newBuilder().build()
                    chain.proceed(newRequest)
                })
                .addInterceptor(httpInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
            maniaRetrofitBuilder.client(okHttpClient)
        }

        // OkHttp Interceptor 로그 기록 용
        private fun httpInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BASIC
            return interceptor
        }
    }
}