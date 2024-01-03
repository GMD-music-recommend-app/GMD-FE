package com.sesac.gmd.presentation.ui.music_recommend

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.common.LocationUtil
import com.sesac.gmd.data.model.Location

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private var _selectedLocation = MutableLiveData<Location>()
    val selectedLocation: LiveData<Location> get() = _selectedLocation


    fun setLocation(location: LatLng) {
        _selectedLocation.value = LocationUtil.geocoding(getApplication(), location)
    }

    fun isInitializedLocation(): Boolean {
        return true
    }
}