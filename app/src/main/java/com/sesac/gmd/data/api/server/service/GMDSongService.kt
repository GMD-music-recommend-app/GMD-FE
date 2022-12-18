/*
* Created by gabriel
* date : 22/12/08
* */

package com.sesac.gmd.data.api.server.service

import com.sesac.gmd.data.model.response.song.CreatePinResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface GMDSongService {

    // 반경 내 핀 리스트 반환
    @GET("/songs/info-list")
    fun getSongList()

    // 핀 정보 반환
    @GET("/songs/info/{pinIdx}")
    fun getSongInfo()

    // 핀 생성
    @POST("/songs")
    suspend fun createPin(@Body params: RequestBody): Response<CreatePinResponse>

    // 핀 댓글 작성
    @POST("/songs/comment/write")
    fun writeComment()

    // 핀 공감, 공감 취소
    @POST("/songs/like/{userIdx}/{pinIdx}")
    fun insertLike()
}