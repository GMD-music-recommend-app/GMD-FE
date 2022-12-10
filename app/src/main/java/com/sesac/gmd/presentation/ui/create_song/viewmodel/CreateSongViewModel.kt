/*
* Created by gabriel
* date : 22/12/06
* */

package com.sesac.gmd.presentation.ui.create_song.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesac.gmd.common.util.Utils.Companion.parseXMLFromMania
import com.sesac.gmd.data.model.Item
import com.sesac.gmd.data.model.SongInfo
import com.sesac.gmd.data.repository.CreateSongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "CreateSongViewModel"

class CreateSongViewModel(private val repository: CreateSongRepository) : ViewModel() {
    private val _song: SongInfo? = null
    val song get() = _song!!

    val songList = MutableLiveData<MutableList<Item>>()

    // 음악 검색
    fun getSong(keyword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val responseBody = repository.getSong(keyword)

            val result = parseXMLFromMania(responseBody.string())
            songList.postValue(result.items)
        }
    }

    // 다음 페이지로 이동
    fun moveToNextPage() {

    }

    // 핀 생성하기
    fun createPin() {

    }

    // TODO: 노래 추가 여부에 따라 버튼 형태 변경되도록 코드 수정 필요
}