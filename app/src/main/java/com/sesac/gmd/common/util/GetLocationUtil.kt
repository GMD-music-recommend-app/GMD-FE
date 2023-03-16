package com.sesac.gmd.common.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * 유저의 현재 위치(위도, 경도)를 가져와 LatLng Class 로 반환하는 함수
 */
object GetLocationUtil {
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
                throw Exception(context.getString(R.string.error_not_found_user_location))
            }
            // 받아온 현재 위치를 LatLng Class 에 담아 반환
            else {
                LatLng(location.latitude, location.longitude)
            }
        } catch (e: Exception) {
            /**
             * 현재 위치를 가져오지 못했다면 Exception throw
             * 이후 상위 블럭에서 현재 위치를 LatLng(0.0, 0.0)으로 초기화)
             */
            throw e
        }
    }
}