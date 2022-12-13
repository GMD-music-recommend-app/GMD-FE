/*
* Created by gabriel
* date : 22/12/13
* */

package com.sesac.gmd.presentation.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sesac.gmd.data.repository.CreateSongRepository
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: CreateSongRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CreateSongViewModel::class.java)) {
            CreateSongViewModel(repository) as T
        } else {
            throw IllegalArgumentException("해당 ViewModel 을 찾을 수 없습니다.")
        }
    }
}