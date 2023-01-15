/**
* Created by 조진수
* date : 22/12/13
*/
package com.sesac.gmd.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.ui.main.viewmodel.ChartViewModel
import com.sesac.gmd.presentation.ui.main.viewmodel.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CreateSongViewModel::class.java)) {
            CreateSongViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ChartViewModel::class.java)) {
            ChartViewModel(repository) as T
        } else {
            throw IllegalArgumentException("해당 ViewModel 을 찾을 수 없습니다.")
        }
    }
}