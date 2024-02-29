package com.sesac.gmd.data.api

import com.sesac.gmd.BuildConfig
import com.sesac.gmd.data.model.response.ManiaDBClientResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// TODO: mania db api version gradle 에서 정의 후 전역으로 사용할 수 있게 구현 필요

// 음악 검색 시 검색 결과 표시 갯수
const val MUSIC_SEARCH_RESULT_COUNT = 20
const val MANIA_DB_API_VERSION = 0.5

interface ManiaDbApi {
    /**
     * API 문서: https://www.maniadb.com/api/
     * @param keyword 검색 키워드
     * @return MUSIC_SEARCH_RESULT_COUNT 갯수 만큼 검색 결과 XML로 리턴
     */
    @GET("{keyword}/?sr=song&display=${MUSIC_SEARCH_RESULT_COUNT}&key=${BuildConfig.MANIA_DB_API_KEY}&v=${MANIA_DB_API_VERSION}")
    suspend fun getSong(@Path("keyword") keyword: String) : Response<ManiaDBClientResponse>
}