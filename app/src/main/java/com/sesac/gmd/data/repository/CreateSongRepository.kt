/*
* Created by gabriel
* date : 22/12/09
* */

package com.sesac.gmd.data.repository

import com.sesac.gmd.data.api.maniadb.ManiaDBRetrofitClient.Companion.maniaDBService
import com.sesac.gmd.data.api.server.client.GMDSongRetrofitClient.Companion.gmdSongService
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.model.response.CreateSongResponse
import okhttp3.ResponseBody
import retrofit2.Response

class CreateSongRepository {

    // 음악 검색
    suspend fun getSong(keyword: String) : ResponseBody {
        return maniaDBService.getSong(keyword)
    }

    // 음악 핀 생성하기
    suspend fun createPin(userID: Int, location: Location,
                          song: Song, reason: String, hashtag: String?): Response<CreateSongResponse> {

        return gmdSongService.createSongs(
            song.album.albumImage,
            song.album.albumTitle,
            song.artist.joinToString ( "," ),
            location.city,
            hashtag,
            location.latitude,
            location.longitude,
            reason,
            song.songIdx,
            location.state,
//            location.street,
            song.songTitle,
            userID
        )
    }
}