/**
* Created by 조진수
* date : 22/12/21
*/
package com.sesac.gmd.presentation.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.sesac.gmd.R
import com.sesac.gmd.common.util.LATITUDE
import com.sesac.gmd.common.util.LONGITUDE
import com.sesac.gmd.common.util.SPLASH_LOGO_SHOWING_TIME
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.presentation.ui.main.activity.MainActivity
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    companion object {

        // 해당 앱에서 요구하는 Permissions
        val PERMISSIONS = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            // Splash 화면 1.5 초 동안 표시
            delay(SPLASH_LOGO_SHOWING_TIME)
            // Permissions Check
            checkPermissions()
        }
    }

    // Permissions Check 함수
    private fun checkPermissions() {
        // 마시멜로(안드로이드 6.0) 이상 권한 체크
        if (Build.VERSION.SDK_INT >= 23) {
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getString(R.string.alert_need_location_permission))
                .setDeniedMessage(getString(R.string.alert_reject_location_permission))
                .setPermissions(*PERMISSIONS)
                .check()
        }
    }

    private var permissionListener: PermissionListener = object : PermissionListener {
        // 권한 허가시 실행 할 내용
       override fun onPermissionGranted() {
            // 현재 위치 정보 가져오기
            getCurrentLocation()
        }

        // 권한 거부시 실행  할 내용
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            AlertDialog.Builder(this@SplashActivity)
                .setMessage(getString(R.string.alert_app_finish_caused_by_rejected_permissions))
                .setPositiveButton(getString(R.string.alert_go_to_permission_setting)) { _, _ ->
                    try {
                        val nextPage = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        startActivity(nextPage)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }
                .setNegativeButton(getString(R.string.txt_finish)) { _, _ ->
                    finish()
                }
                .show()
        }
    }

    // 현재 위치 정보 가져와 intent 에 실어서 MainActivity 로 이동
    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        val nextPage = Intent(this@SplashActivity, MainActivity::class.java)
        // 사용자의 정확한 현재 위치 요청
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@SplashActivity)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it == null) {
                // fusedLocationClient 가 현재 위치를 파악하지 못하는 경우
                toastMessage(getString(R.string.error_not_found_user_location))
                throw SecurityException(getString(R.string.error_not_found_location_data))
            }
            else {
                nextPage.putExtra(LATITUDE, it.latitude)
                nextPage.putExtra(LONGITUDE, it.longitude)
            }
            startActivity(nextPage)
            finish()
        }
    }
}