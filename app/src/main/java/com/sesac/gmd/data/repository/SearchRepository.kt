package com.sesac.gmd.data.repository

import com.sesac.gmd.data.api.ManiaDbApi
import javax.inject.Inject

class SearchRepository @Inject constructor(private val service: ManiaDbApi) {
    suspend fun getMusic(keyword: String) = service.getMusic(keyword)
}