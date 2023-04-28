package com.sesac.gmd.data.repository.remote

import com.google.gson.Gson
import com.sesac.gmd.data.datasource.remote.maniadb.ManiaDBRetrofitClient.Companion.maniaDBService
import com.sesac.gmd.data.model.remote.GetChartResponse
import com.sesac.gmd.data.datasource.remote.server.chart.GetChartRetrofitClient.Companion.getChartService
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.model.remote.CreatePinResponse
import com.sesac.gmd.data.datasource.remote.server.song.CreatePinRetrofitClient.Companion.createPinService
import com.sesac.gmd.data.model.remote.GetPinInfoResponse
import com.sesac.gmd.data.datasource.remote.server.song.GetPinInfoRetrofitClient.Companion.getPinInfoService
import com.sesac.gmd.data.model.remote.GetPinListResponse
import com.sesac.gmd.data.datasource.remote.server.song.GetPinListRetrofitClient.Companion.getPinListService
import com.sesac.gmd.data.model.remote.InsertLikePinResponse
import com.sesac.gmd.data.datasource.remote.server.song.InsertLikePinRetrofitClient.Companion.insertLikePinService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class RemoteRepository {
    // 반경 내 핀 리스트 가져오기
    suspend fun getPinList(lat: Double, lng: Double, radius: Int) : Response<GetPinListResponse> {
        return getPinListService.getPinList(lat, lng, radius)
    }

    // 핀 정보 가져오기(핀 클릭 시 bottomSheet 화면)
    suspend fun getSongInfo(pinIdx: Int, userIdx: Int) : Response<GetPinInfoResponse> {
        return getPinInfoService.getSongInfo(pinIdx, userIdx)
    }

    // 핀 공감하기
    suspend fun insertLikePin(pinIdx: Int, userIdx: Int) : Response<InsertLikePinResponse> {
        val likedPinData = mutableMapOf<String, Int>()
        likedPinData["pinIdx"] = pinIdx
        likedPinData["userIdx"] = userIdx

        val requestBody = Gson()
            .toJson(likedPinData)
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return insertLikePinService.insertLike(requestBody)
    }

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

    // 현재 유저의 위치 기준 지역 내 인기 차트 반환
    suspend fun getChartList(city: String) : Response<GetChartResponse> {
        return getChartService.getChart(city)
    }
}