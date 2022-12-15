/*
* Created by gabriel
* date : 22/12/08
* */

package com.sesac.gmd.data.api.server.service

import com.sesac.gmd.data.model.response.CreateSongResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface GMDSongService {

    // 반경 내 핀 리스트 반환
    @GET("/songs/info-list")
    fun getSongList()

    // 핀 정보 반환
    @GET("/songs/info/{pinIdx}")
    fun getSongInfo()

    // 핀 생성
    @FormUrlEncoded
    @POST("/songs")
    suspend fun createSongs(
        @Field("albumCover") albumCover: String,
        @Field("albumTitle") albumTitle: String,
        @Field("artist") artist: String,
        @Field("city") city: String?,
        @Field("hashtag") hashtag: String?,
        @Field("latitude") latitude: Double,
        @Field("longitude")longitude: Double,
        @Field("reason") reason: String,
        @Field("songIdx") songIdx: Int,
        @Field("state") state: String?,
//        @Field("street") street: String?,
        @Field("title") title: String,
        @Field("userIdx") userIdx: Int
    ) : Response<CreateSongResponse>

    // 핀 댓글 작성
    @POST("/songs/comment/write")
    fun writeComment()

    // 핀 공감, 공감 취소
    @POST("/songs/like/{userIdx}/{pinIdx}")
    fun insertLike()
}