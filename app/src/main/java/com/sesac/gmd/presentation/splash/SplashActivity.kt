/*
* Created by gabriel
* date : 22/12/21
* */
package com.sesac.gmd.presentation.splash

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
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.presentation.ui.main.activity.MainActivity
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    companion object {
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
            delay(1_500L)
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
                .setRationaleMessage("앱을 이용하기 위해서는 위치 정보 접근 권한이 필요합니다")
                .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 권한을 허용해주세요.")
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
                .setMessage("권한 거절로 인해 앱이 종료됩니다.")
                .setPositiveButton("권한 설정하러 가기") { _, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }
                .setNegativeButton("종료") { _, _ ->
                    finish()
                }
                .show()
        }
    }

    // 현재 위치 정보 가져와 intent 에 실어서 MainActivity 로 이동
    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        // 사용자의 정확한 현재 위치 요청
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@SplashActivity)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it == null) {
                // fusedLocationClient 가 현재 위치를 파악하지 못하는 경우
                toastMessage("사용자의 현재 위치를 알 수 없습니다.")
                throw SecurityException("Location Data 를 얻지 못함")
            }
            else {
                intent.putExtra("latitude", it.latitude)
                intent.putExtra("longitude", it.longitude)
            }
            startActivity(intent)
            finish()
        }
    }
}