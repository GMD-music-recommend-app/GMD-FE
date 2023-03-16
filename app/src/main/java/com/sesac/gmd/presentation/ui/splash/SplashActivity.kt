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
import com.google.android.gms.maps.model.LatLng
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.sesac.gmd.R
import com.sesac.gmd.common.util.*
import com.sesac.gmd.common.util.Utils.Companion.displayToastExceptions
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getString(R.string.alert_need_location_permission))
                .setDeniedMessage(getString(R.string.alert_reject_location_permission))
                .setPermissions(*PERMISSIONS)
                .check()
        } else {
            goToMainActivity()
        }
    }

    private val permissionListener = object : PermissionListener {
        // 권한 허가시 실행 할 내용
       override fun onPermissionGranted() {
            goToMainActivity()
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

    /**
     * 유저의 현재 위치를 가져와 Intent 에 실어 MainActivity 로 이동<br>
     * 메인 화면에서 유저의 현재 위치를 중심으로 지도 화면 표시
     */
    private fun goToMainActivity() {
        CoroutineScope(Dispatchers.IO).launch {
            val currentUserLocation = withContext(Dispatchers.IO) {
                getUserLocation()
            }

            // 유저의 현재 위치(위/경도) 값을 intent 에 실어 MainActivity 로 전송
            val nextPage = Intent(this@SplashActivity, MainActivity::class.java).apply {
                putExtra(LATITUDE, currentUserLocation.latitude)
                putExtra(LONGITUDE, currentUserLocation.longitude)
            }
            startActivity(nextPage)
            finish()
        }
    }

    // 유저의 현재 위치 정보 가져오기
    private suspend fun getUserLocation(): LatLng {

        return try {
            GetLocationUtil.getCurrentLocation(this@SplashActivity)
        } catch (e: Exception) {
            // 만약 현재 위치를 가져오지 못했으면(위/경도 0.0으로 반환) Toast 출력
            displayToastExceptions(e)
            LatLng(0.0, 0.0)
        }
    }
}