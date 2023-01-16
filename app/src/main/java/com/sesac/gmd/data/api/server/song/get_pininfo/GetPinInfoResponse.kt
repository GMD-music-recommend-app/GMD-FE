/**
* Created by 조진수
* date : 22/12/21
*/
package com.sesac.gmd.data.api.server.song.get_pininfo

/**
 * 핀 클릭 시 받아올 해당 핀 정보 Request 에 대한 Response
 */
data class GetPinInfoResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetPinInfoResult
)

/**
 * Request 결과로 받아온 해당 핀 정보
 */
data class GetPinInfoResult(
    val pinIdx: Int,            // 핀 인덱스
    val userIdx: Int,           // 생성한 유저 인덱스
    val nickname: String,       // 생성한 유저 닉네임
    val songIdx: Int,           // 음악 인덱스
    val songTitle: String,      // 곡 제목
    val artist: String,         // 가수
    val albumTitle: String,     // 앨범 제목
    val albumImage: String,     // 앨범 이미지
    val reason: String,         // 사연
    val hashtag: String,        // 해시태그
    val isLiked: String,        // 공감 여부
    val isMade: String,         // 본인이 생성한 핀인지 확인
    val comments: MutableList<Comment>, // 댓글
    val latitude: Double,       // 생성 장소(위도)
    val longitude: Double,      // 생성 장소(경도)
    val state: String,          // 생성 장소(도, 시)
    val city: String,           // 생성 장소(군, 구)
    val street: String          // 생성 장소(읍, 면, 동)
)

// 핀에 생성된 댓글
data class Comment(
    val content: String,    // 댓글 내용
    val nickName: String,   // 댓글 작성자 닉네임
    val pinIdx: Int,        // 댓글 달린 핀 인덱스
    val userIdx: Int        // 댓글 작성자 인덱스
)