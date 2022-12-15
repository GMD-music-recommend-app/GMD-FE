/*
* Created by gabriel
* date : 22/12/15
* */

package com.sesac.gmd.data.model.response

data class BaseResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
)