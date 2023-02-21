package com.sesac.gmd.common.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.R
import com.sesac.gmd.data.model.Location
import java.util.*

/**
* Location(위도, 경도) 값을 통해 행정 구역을 반환하는 Geocoder
*/
object GeoUtil {
    private val TAG = GeoUtil::class.simpleName

    // Geocoding(위/경도 -> 행정 구역 변환) 함수
    @Suppress("DEPRECATION")
    fun geocoding(context: Context, latLng: LatLng) : Location {
        val userLocation = Location(latLng.latitude, latLng.longitude)
        val geocoder = Geocoder(context, Locale.getDefault())

        // Over Android API 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1, object: Geocoder.GeocodeListener {
                // 제대로 Geocoding 성공했을 경우
                override fun onGeocode(address: MutableList<Address>) {
                    userLocation.state = if (address[0].adminArea == null) address[0].subAdminArea else address[0].adminArea
                    userLocation.city = if (address[0].locality == null) address[0].subLocality else address[0].locality
                    userLocation.street = if (address[0].thoroughfare == null) address[0].subThoroughfare else address[0].thoroughfare
                }
                // Geocoding 실패했을 경우
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    Utils.toastMessage(context.getString(R.string.unexpected_error))
                    Log.d(DEFAULT_TAG + TAG, "Geocoder occurred error : $errorMessage!!")
                }
            })
        } else {
            // Under Android API 33
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            try {
                if (address != null) {
                    if (address.isNotEmpty()) {
                        userLocation.state = if (address[0].adminArea == null) address[0].subAdminArea else address[0].adminArea
                        userLocation.city = if (address[0].locality == null) address[0].subLocality else address[0].locality
                        userLocation.street = if (address[0].thoroughfare == null) address[0].subThoroughfare else address[0].thoroughfare
                    }
                }
            } catch (e: Exception) {
                Utils.displayToastExceptions(e)
                Log.d(DEFAULT_TAG + TAG, "Geocoder occurred error : ${e.message}!!")
            }
        }
        return userLocation
    }
}