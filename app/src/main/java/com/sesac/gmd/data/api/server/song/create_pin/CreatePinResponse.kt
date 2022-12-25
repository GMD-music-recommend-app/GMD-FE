/*
* Created by gabriel
* date : 22/12/15
* */
package com.sesac.gmd.data.api.server.song.create_pin

// 핀 생성

data class CreatePinResponse (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result : CreatePinResult
    )

data class CreatePinResult(val pinIdx: Int)
