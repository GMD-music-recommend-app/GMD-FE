package com.sesac.gmd.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.sesac.gmd.R
import com.sesac.gmd.common.util.*
import com.sesac.gmd.common.util.PermissionsUtil.Companion.PERMISSIONS
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.presentation.ui.main.activity.MainActivity
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
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
                .setRationaleMessage(getString(R.string.alert_need_location_permission))                      // 권한이 필요한 이유 설명
                .setDeniedMessage(getString(R.string.alert_reject_location_permission)) // 권한이 없을 때
                .setPermissions(*PERMISSIONS)                                                                 // 요청할 권한
                .check()
        } else {
            // 네트워크 연결 상태 확인 후 MainActivity로 이동
            if (isNetworkAvailable()) {
                goToMainActivity()
            } else {
                toastMessage(getString(R.string.error_network_no_connection))
                finish()
            }
        }
    }

    private val permissionListener = object : PermissionListener {
        // 권한 허가시 실행 할 내용
       override fun onPermissionGranted() {
            // 네트워크 연결 상태 확인 후 MainActivity 로 이동
            if (isNetworkAvailable()) {
                goToMainActivity()
            } else {
                toastMessage(getString(R.string.error_network_no_connection))
                finish()
            }
        }

        // 권한 거부시 실행 할 내용
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

    // 네트워크 연결 상태 확인
    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    // MainActivity 로 이동
    private fun goToMainActivity() {
        val nextPage = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(nextPage)
        finish()
    }
}