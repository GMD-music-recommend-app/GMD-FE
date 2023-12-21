package com.sesac.gmd.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.data.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

object LocationUtil {
    // Geocoding(위/경도 -> 행정 구역 변환) 함수
    fun geocoding(context: Context, latLng: LatLng) : Location {
        // TODO: applicationContext -> Hilt 적용
//        val geocoder = Geocoder(mContext, Locale.getDefault())

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
                    Logger.e("Geocoder occurred error : $errorMessage!!")

                    super.onError(errorMessage)
                    // FIXME: 에러 발생 시 Toast -> Dialog
                    Utils.toastMessage("예기치 못한 문제가 발생했습니다.")
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
                Logger.traceException(e)
                // FIXME: 에러 발생 시 Toast -> Dialog
//                Utils.displayToastExceptions(e)
            }
        }
        return userLocation
    }

    // 유저의 현재 위치(위도, 경도)를 가져와 LatLng Class 로 반환하는 함수
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): LatLng = withContext(Dispatchers.IO) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        try {
            /**
             * 유저의 정확한 현재 위치 요청<br>
             * fusedLocationClient.lasLocation 함수가 비동기적으로 동작하기 때문에<br>
             * await 을 통해 결과 값을 기다림으로서 동기적으로 동작하도록 작성
             */
            val location = fusedLocationClient.lastLocation.await()

            // fusedLocationClient 가 현재 위치를 파악하지 못하는 경우
            if (location == null) {
                throw Exception("사용자의 위치 정보를 가져오는 데 실패했습니다.")
            }
            // 받아온 현재 위치를 LatLng Class 에 담아 반환
            else {
                LatLng(location.latitude, location.longitude)
            }
        } catch (e: Exception) {
            Logger.traceException(e)
            /**
             * 현재 위치를 가져오지 못했다면 Exception throw
             * 이후 상위 블럭에서 현재 위치를 LatLng(0.0, 0.0)으로 초기화)
             */
            throw e
        }
    }
}