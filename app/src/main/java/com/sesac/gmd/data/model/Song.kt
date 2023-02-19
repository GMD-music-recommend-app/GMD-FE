package com.sesac.gmd.data.model

/**
 * 검색 결과로 받아 온 곡 리스트
 * * <곡 정보>
 * * songId : 곡 인덱스
 * * title : 노래 제목
 * * maniaDB:album.title : 앨범 제목
 * * maniaDB:album.image : 앨범 이미지
 * * maniaDB:artist.name : 가수
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
 * maniaDB:album
 */
data class ManiaDBAlbum(
    var albumTitle: String = "",
    var albumImage: String = "",
)
