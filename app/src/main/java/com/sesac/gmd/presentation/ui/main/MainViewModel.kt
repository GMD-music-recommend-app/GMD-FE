package com.sesac.gmd.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.data.model.Location

class MainViewModel: ViewModel() {
    private var _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> get() = _currentLocation
//
//    init {
//        // TODO: activity 생성될 때 중심점, 핀 리스트 미리 가져오기
//    }

    fun updateCurrentLocation(location: LatLng) {

    }
}