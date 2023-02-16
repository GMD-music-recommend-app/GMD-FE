/**
* Created by 조진수
* date : 22/12/21
*/
package com.sesac.gmd.data.api.server.song.like_pin

/**
 * 핀 공감 & 공감 취소 Request 에 대한 Response
 */
data class InsertLikePinResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result : InsertLikePinResult
)

/**
 * 핀 공감 & 공감 취소 결과<br>
 * 행위자의 유저 인덱스와 해당 핀의 인덱스를 반환
 */
data class InsertLikePinResult(
    val pinIdx: Int,
    val userIdx: Int
)