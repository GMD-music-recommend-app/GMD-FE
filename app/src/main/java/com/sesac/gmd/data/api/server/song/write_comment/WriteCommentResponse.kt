/**
* Created by 조진수
* date : 22/12/21
*/
package com.sesac.gmd.data.api.server.song.write_comment

/**
 * 댓글 작성 Request 에 대한 Response
 */
data class WriteCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result : WriteCommentResult
)

/**
 * 댓글 작성에 대한 결과
 * 작성한 댓글의 인덱스를 반환
 */
data class WriteCommentResult(val commentIdx: Int)