/*
* Created by gabriel
* date : 22/12/21
* */
package com.sesac.gmd.data.api.server.song.write_comment

// 핀 댓글 작성

data class WriteCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result : WriteCommentResult
)

data class WriteCommentResult(val commentIdx: Int)