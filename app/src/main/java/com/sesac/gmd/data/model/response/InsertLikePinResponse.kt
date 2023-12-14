package com.sesac.gmd.data.model.response

/**
 * 핀 공감 & 공감 취소 Request 에 대한 Response
 */
data class InsertLikePinResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result : String
)