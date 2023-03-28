package com.sesac.gmd.data.model.remote

/**
 * 핀 생성 Request 에 대한 Response
 */
data class CreatePinResponse (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result : CreatePinResult
    )

/**
 * 핀 생성 Request 에 대한 Response 중 생성 결과 내용(생성 된 핀 인덱스)
 */
data class CreatePinResult(val pinIdx: Int)
