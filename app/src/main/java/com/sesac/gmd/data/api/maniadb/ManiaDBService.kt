/*
* Created by gabriel
* date : 22/12/06
* */

package com.sesac.gmd.data.api.maniadb

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

private const val MANIADB_SERVICE_KEY = "gmd6to30@gmail.com"

/*
* keyword : 컴색어
* sr : 검색 조건
* display : 검색 결과 갯수
* key : API Key(= 본인 이메일)
* v : API 버전(default 0.5)
* */

interface ManiaDBService {
    // 곡 제목 검색
    @GET("{keyword}/?sr=song&display=100&key=${MANIADB_SERVICE_KEY}&v=0.5")
    suspend fun getSong(@Path("keyword") keyword: String) : ResponseBody

    /*// 가수 검색
    @GET("{keyword}/?sr=artist&display=10&key=${SERVICE_KEY}&v=0.5")
    fun getArtist(@Path("keyword") keyword: String) : Call<ResponseBody>

    // 앨범 검색
    @GET("{keyword}/?sr=album&display=10&key=${SERVICE_KEY}&v=0.5")
    fun getAlbum(@Path("keyword") keyword: String) : Call<ResponseBody>*/
}