/**
* Created by 조진수
* date : 22/12/06
*/
package com.sesac.gmd.data.api.maniadb

import com.sesac.gmd.common.util.MANIA_DB_SERVICE_KEY
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
    // 곡 제목 검색(검색 결과 10(=display)개 표시)
    @GET("{keyword}/?sr=song&display=10&key=${MANIA_DB_SERVICE_KEY}&v=0.5")
    suspend fun getSong(@Path("keyword") keyword: String) : ResponseBody

    /*// 가수 검색
    @GET("{keyword}/?sr=artist&display=10&key=${SERVICE_KEY}&v=0.5")
    fun getArtist(@Path("keyword") keyword: String) : Call<ResponseBody>

    // 앨범 검색
    @GET("{keyword}/?sr=album&display=10&key=${SERVICE_KEY}&v=0.5")
    fun getAlbum(@Path("keyword") keyword: String) : Call<ResponseBody>*/
}