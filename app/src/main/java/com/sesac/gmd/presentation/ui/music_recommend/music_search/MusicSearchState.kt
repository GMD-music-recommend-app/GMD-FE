package com.sesac.gmd.presentation.ui.music_recommend.music_search

import com.sesac.gmd.data.model.MusicList

sealed class SearchState {
    data object Loading : SearchState()
    data class Success(val result: MusicList) : SearchState()
    data class Failed(val message: String) : SearchState()
    data class Error(val error: Throwable) : SearchState()
}