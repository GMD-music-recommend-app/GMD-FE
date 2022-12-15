/*
* Created by gabriel
* date : 22/12/15
* */

package com.sesac.gmd.data.model.response

data class CreateSongResponse (
    val response: BaseResponse,
    val result : Result
    )

data class Result(val pinIdx: Int)
