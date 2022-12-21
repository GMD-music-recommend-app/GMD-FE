/*
* Created by gabriel
* date : 22/12/21
* */
package com.sesac.gmd.presentation.ui.home.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
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
            delay(1_000L)

            // Permissions Check
            checkPermissions()
        }
    }

    private  fun checkPermissions() {
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
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
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
}