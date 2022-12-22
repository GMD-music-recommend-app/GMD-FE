/*
* Created by gabriel
* date : 22/12/09
* */
package com.sesac.gmd.data.repository

import com.google.gson.Gson
import com.sesac.gmd.data.api.maniadb.ManiaDBRetrofitClient.Companion.maniaDBService
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.api.server.song.create_pin.CreatePinResponse
import com.sesac.gmd.data.api.server.song.create_pin.CreatePinRetrofitClient.Companion.createPinService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class Repository {
    // 음악 검색
    suspend fun getSong(keyword: String) : ResponseBody {
        return maniaDBService.getSong(keyword)
    }

    // 음악 핀 생성하기
    suspend fun createPin(userID: Int, location: Location,
                          song: Song, reason: String, hashtag: String?): Response<CreatePinResponse> {
        val map = mutableMapOf<Any, Any?>()
        map["albumImage"] = song.album.albumImage
        map["albumTitle"] = song.album.albumTitle
        map["artist"] = song.artist.joinToString ( "," )
        map["city"] = location.city
        map["hashtag"] = hashtag
        map["latitude"] = location.latitude
        map["longitude"] = location.longitude
        map["reason"] = reason
        map["songIdx"] = song.songIdx
        map["songTitle"] = song.songTitle
        map["state"] = location.state
        map["street"] = location.street
        map["userIdx"] = userID

        val requestBody = Gson()
            .toJson(map)
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        return createPinService.createPin(requestBody)
    }
}