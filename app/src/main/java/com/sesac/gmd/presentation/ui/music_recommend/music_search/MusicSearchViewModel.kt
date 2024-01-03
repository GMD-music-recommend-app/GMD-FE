package com.sesac.gmd.presentation.ui.music_recommend.music_search

import androidx.lifecycle.viewModelScope
import com.sesac.gmd.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class MusicSearchViewModel: BaseViewModel() {


    fun searchMusic(text: String) {
        if (isValidatedKeyword(text)) {

        }
    }

    private fun requestMusicSearchResult(text: String) {
        viewModelScope.launch(exceptionHandler) {
//            val responseBody =
        }
    }

    private fun isValidatedKeyword(text: String): Boolean {
        // TODO:
        return true
    }
}