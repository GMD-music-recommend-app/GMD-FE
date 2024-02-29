package com.sesac.gmd.presentation.ui.music_recommend.music_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.gmd.common.Logger
import com.sesac.gmd.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicSearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {
    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> get() = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.traceThrowable(throwable)
    }

    fun searchMusic(keyword: String) {
        _state.value = SearchState.Loading

        viewModelScope.launch(exceptionHandler) {
            repository.getMusic(keyword).let { response ->
                if (response.isSuccessful) {
                    Logger.d("gavi, success!")
                    Logger.d("gavi, result: ${response.message()}")
                } else {
                    Logger.d("gavi, failed")
                    Logger.d("gavi, result: ${response.message()}")
                }
            }
        }
    }
}