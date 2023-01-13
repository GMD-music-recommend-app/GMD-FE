/**
* Created by 조진수
* date : 22/12/01
*/
package com.sesac.gmd.data.model

/**
 * 필요 정보
 * songId : 곡 인덱스
 * title : 노래 제목
 * maniadb:album.title : 앨범 제목
 * maniadb:album.image : 앨범 이미지
 * maniadb:artist.name : 가수
 */

/**
 * 검색 결과로 받아 온 곡 리스트
 */
data class SongList(
    val songs: MutableList<Song> = mutableListOf()
)

/**
 * 곡에 대한 개별 정보
 */
data class Song(
    var songIdx: Int = 0,
    var songTitle: String = "",
    var album: ManiaDBAlbum = ManiaDBAlbum(),
    var artist: MutableList<String> = mutableListOf()
)

/**
 * 곡에 대한 개별 정보 중 앨범 정보
 * maniadb:album
 */
data class ManiaDBAlbum(
    var albumTitle: String = "",
    var albumImage: String = "",
)
