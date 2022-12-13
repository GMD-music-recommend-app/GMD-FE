/*
* Created by gabriel
* date : 22/12/10
* */

package com.sesac.gmd.data.model

data class Location(
    var latitude : Double? = null,   // 위도
    var longitude : Double? = null,  // 경도
    var state : String? = null, // 도, 시
    var city : String? = null,  // 군, 구
    var street : String? = null // 읍, 면, 동
)