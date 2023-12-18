package com.sesac.gmd.common

import android.content.Context
import android.util.TypedValue
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.data.model.Location

// FIXME:
fun Location.isinitialized(): Boolean {
    return this.state != ""
}

fun Location.asLatLng() : LatLng {
    return LatLng(this.latitude, this.longitude)
}

fun Int.dpToPixels(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}