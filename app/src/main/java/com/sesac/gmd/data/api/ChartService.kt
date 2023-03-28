package com.sesac.gmd.data.api

import com.sesac.gmd.data.model.remote.GetChartResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * CharService REST API
 */
interface ChartService {

    // 지역 내 인기차트 반환
    @GET("/chart/{city}")
    suspend fun getChart(@Path("city") city: String) : Response<GetChartResponse>
}