/*
* Created by gabriel
* date : 22/12/21
* */
package com.sesac.gmd.data.api.server.song.like_pin

// 핀 공감 & 공감 취소

data class InsertLikePinResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result : InsertLikePinResult
)

data class InsertLikePinResult(
    val pinIdx: Int,
    val userIdx: Int
)