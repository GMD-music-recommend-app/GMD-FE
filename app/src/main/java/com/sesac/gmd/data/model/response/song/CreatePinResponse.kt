/*
* Created by gabriel
* date : 22/12/15
* */

package com.sesac.gmd.data.model.response.song

data class CreatePinResponse (
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result : CreatePinResult
    )

data class CreatePinResult(val pinIdx: Int)
