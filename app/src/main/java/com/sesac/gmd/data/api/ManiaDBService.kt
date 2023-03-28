package com.sesac.gmd.data.api

import com.sesac.gmd.common.util.MANIADB_API_VERSION
import com.sesac.gmd.common.util.MANIA_DB_SERVICE_KEY
import com.sesac.gmd.common.util.MUSIC_SEARCH_RESULT_COUNT
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * * keyword : 검색어
 * * sr : 검색 조건
 * * display : 검색 결과 갯수
 * * key : API Key(= 본인 이메일)
 * * v : API 버전(default 0.5)
 */
interface ManiaDBService {
    // 곡 제목 검색(검색 결과 20(=display)개 표시)
    @GET("{keyword}/?sr=song&display=${MUSIC_SEARCH_RESULT_COUNT}&key=${MANIA_DB_SERVICE_KEY}&v=${MANIADB_API_VERSION}")
    suspend fun getSong(@Path("keyword") keyword: String) : ResponseBody
}