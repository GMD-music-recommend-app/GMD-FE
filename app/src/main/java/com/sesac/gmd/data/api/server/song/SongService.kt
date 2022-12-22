/*
* Created by gabriel
* date : 22/12/08
* */
package com.sesac.gmd.data.api.server.song

import com.sesac.gmd.data.api.server.song.create_pin.CreatePinResponse
import com.sesac.gmd.data.api.server.song.get_pininfo.GetPinInfoResponse
import com.sesac.gmd.data.api.server.song.like_pin.InsertLikePinResponse
import com.sesac.gmd.data.api.server.song.write_comment.WriteCommentResponse
import com.sesac.gmd.data.api.server.song.get_pinlist.GetPinListResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

// Swagger : http://yourplaylistgmd.shop/swagger-ui/index.html#/

interface SongService {
    // 반경 내 핀 리스트 반환
    @GET("/songs/info-list")
    suspend fun getPinList(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int    // 검색 반경(단위: m)
    ) : Response<GetPinListResponse>

    // 핀 정보 반환
    @GET("/songs/info/{pinIdx}")
    suspend fun getSongInfo(
        @Path("pinIdx") pinIdx: Int,
        @Query("userIdx") userIdx: Int
    ) : Response<GetPinInfoResponse>

    // 핀 생성
    @POST("/songs")
    suspend fun createPin(@Body params: RequestBody): Response<CreatePinResponse>

    // 핀 댓글 작성
    @POST("/songs/comment/write")
    suspend fun writeComment(@Body params: RequestBody) : Response<WriteCommentResponse>

    // 핀 공감, 공감 취소
    @POST("/songs/like/{userIdx}/{pinIdx}")
    suspend fun insertLike(
        @Path("pinIdx") pinIdx: Int,
        @Path("userIdx") userIdx: Int
    ) : Response<InsertLikePinResponse>
}