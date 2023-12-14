package com.sesac.gmd.common

import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.data.model.Location

// FIXME:
fun Location.isinitialized(): Boolean {
    return this.state != ""
}

fun Location.asLatLng() : LatLng {
    return LatLng(this.latitude, this.longitude)
}