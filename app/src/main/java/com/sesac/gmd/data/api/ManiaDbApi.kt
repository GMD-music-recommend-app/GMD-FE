package com.sesac.gmd.data.api

import com.sesac.gmd.BuildConfig
import com.squareup.okhttp.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

// TODO: mania db api version gradle 에서 정의 후 전역으로 사용할 수 있게 구현 필요

// 음악 검색 시 검색 결과 표시 갯수
const val MUSIC_SEARCH_RESULT_COUNT = 20
const val MANIA_DB_API_VERSION = 0.5

interface ManiaDbApi {
    // 곡 제목 검색(검색 결과 20(=display)개 표시)
    @GET("{keyword}/?sr=song&display=${MUSIC_SEARCH_RESULT_COUNT}&key=${BuildConfig.MANIA_DB_API_KEY}&v=${MANIA_DB_API_VERSION}")
    suspend fun getMusic(@Path("keyword") keyword: String) : ResponseBody
}