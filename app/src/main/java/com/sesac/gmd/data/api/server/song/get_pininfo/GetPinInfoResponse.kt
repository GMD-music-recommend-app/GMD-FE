/*
* Created by gabriel
* date : 22/12/21
* */
package com.sesac.gmd.data.api.server.song.get_pininfo

// 핀 정보 반환

data class GetPinInfoResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetPinInfoResult
)

data class GetPinInfoResult(
    val albumImage: String,     // 앨범 이미지
    val albumTitle: String,     // 앨범 제목
    val artist: String,         // 가수
    val city: String,           // 생성 장소(군, 구)
    val comments: MutableList<Comment>, // 댓글
    val hashtag: String,        // 해시태그
    val isLiked: String,        //
    val isMade: String,         //
    val latitude: Double,       // 생성 장소(위도)
    val longitude: Double,      // 생성 장소(경도)
    val nickName: String,       // 생성한 유저 닉네임
    val pinIdx: Int,            // 핀 인덱스
    val reason: String,         // 사연
    val songIdx: Int,           // 음악 인덱스
    val songTitle: String,      // 곡 제목
    val state: String,          // 생성 장소(도, 시)
    val street: String,         // 생성 장소(읍, 면, 동)
    val userIdx: Int            // 생성한 유저 인덱스
)

// 핀에 생성된 댓글
data class Comment(
    val content: String,    // 댓글 내용
    val nickName: String,   // 댓글 작성자 닉네임
    val pinIdx: Int,        // 댓글 달린 핀 인덱스
    val userIdx: Int        // 댓글 작성자 인덱스
)