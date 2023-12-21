package com.sesac.gmd.presentation.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.common.LocationUtil.geocoding
import com.sesac.gmd.common.Logger
import com.sesac.gmd.data.model.Location
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LocationViewModel(application: Application): AndroidViewModel(application) {
    private var _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> get() = _currentLocation

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.traceThrowable(throwable)
        // TODO: state.value = error
    }

    fun updateCurrentLocation(location: LatLng) {
        viewModelScope.launch(exceptionHandler) {

            _currentLocation.value = geocoding(getApplication(), location)
        }
    }
}