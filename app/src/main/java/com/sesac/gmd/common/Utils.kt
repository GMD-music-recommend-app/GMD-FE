package com.sesac.gmd.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import com.sesac.gmd.application.GMDApplication.Companion.getAppInstance
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

object Utils {
    val PERMISSIONS = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Toast 출력 함수
    fun toastMessage(message: String) {
        Toast.makeText(getAppInstance(), message, Toast.LENGTH_SHORT).show()
    }

    // Exception 에 따라 ToastMessage 출력
    fun displayToastExceptions(e: Exception) {
        Log.e("","error : ${e.message}")
        when (e) {
            is TimeoutException -> toastMessage("연결이 고르지 않습니다.\n다시 시도해주시기 바랍니다.")
            is SocketTimeoutException -> toastMessage("연결이 고르지 않습니다.\n다시 시도해주시기 바랍니다.")
            else -> toastMessage("예기치 못한 문제가 발생했습니다.")
        }
    }

    // Network 연결 상태 확인
    @SuppressLint("MissingPermission")
    fun isNetworkConnected(context: Context) : Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
//
//    // Glide 로 이미지 로드 시 로그 기록 용
//    fun setGlideLogger(
//        glideStartTime: Long,
//        name: String
//    ) : RequestListener<Drawable> {
//        return object : RequestListener<Drawable> {
//            override fun onLoadFailed(
//                e: GlideException?,
//                model: Any?,
//                target: Target<Drawable>?,
//                isFirstResource: Boolean
//            ): Boolean {
//                return false
//            }
//
//            override fun onResourceReady(
//                resource: Drawable?,
//                model: Any?,
//                target: Target<Drawable>?,
//                dataSource: DataSource?,
//                isFirstResource: Boolean
//            ): Boolean {
//                val glideEndTime: Long = System.currentTimeMillis()
//                Log.d("GlideLog", "$name Image Processing Time : ${(glideEndTime - glideStartTime) / 1_000.0} seconds")
//
//                if (resource is BitmapDrawable) {
//                    val bitmap = resource.bitmap
//                    Log.d("GlideLog",
//                        String.format("Ready %s bitmap %,d bytes, size: %d x %d",
//                            name,
//                            bitmap.byteCount,
//                            bitmap.width,
//                            bitmap.height)
//                    )
//                }
//                return false
//            }
//        }
//    }
}