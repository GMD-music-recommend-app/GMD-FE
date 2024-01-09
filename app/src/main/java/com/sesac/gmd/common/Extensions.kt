package com.sesac.gmd.common

import android.content.Context
import android.util.TypedValue
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.model.Music

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

fun Music.isEqualMusicItem(targetMusic: Music): Boolean {
    return this.musicIdx == targetMusic.musicIdx
            && this.musicTitle == targetMusic.musicTitle
            && this.artist == targetMusic.artist
}