/*
* Created by gabriel
* date : 22/12/06
* */

package com.sesac.gmd.presentation.ui.create_song.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesac.gmd.common.util.Utils.Companion.parseXMLFromMania
import com.sesac.gmd.data.model.Item
import com.sesac.gmd.data.model.SongInfo
import com.sesac.gmd.data.repository.CreateSongRepository
import kotlinx.coroutines.*

private const val TAG = "CreateSongViewModel"

class CreateSongViewModel(private val repository: CreateSongRepository) : ViewModel() {
    // progressBar
    var isLoading = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<String>()

    // search result Song list
    val songList = MutableLiveData<MutableList<Item>>()

    // selected Song
    private val _selectedSong: SongInfo? = null
    val selectedSong get() = _selectedSong!!

    // 사연
    private val _comment = MutableLiveData<String>()
    private val comment: LiveData<String>
            get() = _comment

    // REST 처리 중 Coroutine 내에서 예외가 발생했을 때
    private val exceptionHandler = CoroutineExceptionHandler { _, thrownException ->
        onError("Coroutine 내 예외 : ${thrownException.localizedMessage}")
    }

    // 음악 검색
    fun getSong(keyword: String) {
        isLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
            val responseBody = repository.getSong(keyword)
            Log.d(TAG, responseBody.toString())
            val result = parseXMLFromMania(responseBody.string())
            songList.postValue(result.items)
            withContext(Dispatchers.Main) {
                isLoading.value = false
            }
        }
    }

    // Coroutine 내 REST 처리 중 에러 발생 시 호출됨
    private fun onError(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }

    // 다음 페이지로 이동
    fun moveToNextPage() {

    }

    // 핀 생성하기
    fun createPin() {

    }

    // TODO: 노래 추가 여부에 따라 버튼 형태 변경되도록 코드 수정 필요
}