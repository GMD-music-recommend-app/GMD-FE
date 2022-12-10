/*
* Created by gabriel
* date : 22/12/09
* */

package com.sesac.gmd.data.repository

import com.sesac.gmd.data.api.maniadb.ManiaDBRetrofitClient.Companion.maniaDBService
import okhttp3.ResponseBody

class CreateSongRepository {

    // 음악 검색
    suspend fun getSong(keyword: String) : ResponseBody {
        return maniaDBService.getSong(keyword)
    }
}