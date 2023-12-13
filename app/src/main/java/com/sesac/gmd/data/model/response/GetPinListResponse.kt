package com.sesac.gmd.data.model.response

/**
 * 지도 화면에서 반경 내 핀 리스트들을 가져오는 Request 에 대한 Response
 */
data class GetPinListResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result : MutableList<Pin>
)

/**
 * 지도 위에 띄워 질 핀 정보<br>
 * 핀 클릭 시 상세 정보를 띄우기 때문에 여기서는 상세 정보를 갖고 있지는 않음
 */
data class Pin(
    val albumImage: String,     // 앨범 이미지
    val city: String,           // 생성 위치(군, 구)
    val distance: Double,       // 검색 좌표로 부터의 거리
    val latitude: Double,       // 생성 위치(위도)
    val longitude: Double,      // 생성 위치(경도)
    val pinIdx: Int,            // 핀 인덱스
    val state: String,          // 생성 위치(도, 시)
    val street: String          // 생성 위치(읍, 면, 동)
)