/**
* Created by 조진수
* date : 22/12/10
*/
package com.sesac.gmd.data.model

/**
 * 사용자, 핀의 위치 정보를 담는 Data Class
 */
data class Location(
    var latitude : Double = 0.0,    // 위도
    var longitude : Double = 0.0,   // 경도
    var state : String? = null,     // 도, 시
    var city : String? = null,      // 군, 구
    var street : String? = null     // 읍, 면, 동
)