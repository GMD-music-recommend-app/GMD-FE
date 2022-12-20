/*
* Created by gabriel
* date : 22/12/01
* */
package com.sesac.gmd.data.model
/*
* <필요 정보>
* songId : 곡 인덱스
* title : 노래 제목
* maniadb:album.title : 앨범 제목
* maniadb:album.image : 앨범 이미지
* maniadb:artist.name : 가수
* */
data class SongList(
    val songs: MutableList<Song> = mutableListOf()
)

data class Song(
    var songIdx: Int = 0,
    var songTitle: String = "",
    var album: ManiaDBAlbum = ManiaDBAlbum(),
    var artist: MutableList<String> = mutableListOf()
)

// <maniadb:album>
data class ManiaDBAlbum(
    var albumTitle: String = "",
    var albumImage: String = "",
)
