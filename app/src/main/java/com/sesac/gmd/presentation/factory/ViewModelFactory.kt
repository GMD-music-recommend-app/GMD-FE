package com.sesac.gmd.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sesac.gmd.R
import com.sesac.gmd.application.GMDApplication
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.ui.main.viewmodel.HomeChartViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CreateSongViewModel::class.java)) {
            CreateSongViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(HomeChartViewModel::class.java)) {
            HomeChartViewModel(repository) as T
        } else {
            throw IllegalArgumentException(GMDApplication.getAppInstance().resources.getString(R.string.error_not_found_viewModel))
        }
    }
}