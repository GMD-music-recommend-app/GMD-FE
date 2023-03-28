package com.sesac.gmd.data.model.remote

/**
 * Chart Request 에 대한 Response
 */
data class GetChartResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: MutableList<GetChartResult>
)

/**
 * Chart Request 에 대한 Response 중 인기 차트 값
 */
data class GetChartResult(
    val albumImage: String, // 앨범 이미지
    val artist: String,     // 가수
    val city: String,       // 생성 장소(군, 구)
    val likeCount: Int,     // 공감 횟수
    val pinIdx: Int,        // 핀 인덱스
    val songRank: Int,      // 노래 순위
    val songTitle: String,  // 곡 제목
    val state: String,      // 생성 장소(도, 시)
)