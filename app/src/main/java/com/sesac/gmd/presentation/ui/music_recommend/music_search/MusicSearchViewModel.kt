package com.sesac.gmd.presentation.ui.music_recommend.music_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.gmd.common.Logger
import com.sesac.gmd.data.model.MusicList
import com.sesac.gmd.data.repository.SearchRepository
import com.sesac.gmd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicSearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {
    // MARK: - LiveData
    private val _state = MutableLiveData<SearchStates>()
    val state: LiveData<SearchStates> get() = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.traceThrowable(throwable)
        // TODO: state.value = error
    }

//    override fun onError(error: Throwable) {
//        _state.value = SearchStates.Error(error)
//    }

    // MARK: - Functions
    fun searchMusic(keyword: String) {
        if (isValidatedKeyword(keyword)) {
            _state.value = SearchStates.Loading

            viewModelScope.launch(exceptionHandler) {
                // TODO: responseBody 로 어떻게 받아오는지 확인해보고 결과 flag 던져준다면 State(isSuccess) 등으로 분기처리
                val responseBody = repository.getMusic(keyword)
                val result = responseBody.string()
                Logger.e("gavi, music List : $result")
//                val songList: MusicList = result.parseXMLFromMania()

            }
        }
    }

    private fun isValidatedKeyword(text: String): Boolean {
        // TODO:
        return true
    }
}

// MARK: - State
sealed class SearchStates {
    data object Loading : SearchStates()
    data class Success(val result: MusicList) : SearchStates()
    data class Failed(val message: String) : SearchStates()
    data class Error(val error: Throwable) : SearchStates()
}