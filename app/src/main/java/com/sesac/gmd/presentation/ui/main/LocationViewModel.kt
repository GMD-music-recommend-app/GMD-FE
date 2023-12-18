package com.sesac.gmd.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.common.LocationUtil.geocoding
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class LocationViewModel: BaseViewModel() {
    private var _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> get() = _currentLocation

    fun updateCurrentLocation(location: LatLng) {
        viewModelScope.launch(exceptionHandler) {
            _currentLocation.value = geocoding(location)
        }
    }
}