package com.sesac.gmd.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.sesac.gmd.R
import com.sesac.gmd.common.SPLASH_LOGO_SHOWING_TIME
import com.sesac.gmd.common.Utils.PERMISSIONS
import com.sesac.gmd.common.Utils.isNetworkConnected
import com.sesac.gmd.presentation.common.AlertDialogFragment
import com.sesac.gmd.presentation.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(SPLASH_LOGO_SHOWING_TIME)
            checkPermissions()
        }
    }

    // TODO: TEDPermission 빼고 ActivityCompat 으로 바꾸기 
    private fun checkPermissions() {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("앱을 이용하기 위해서는 위치 정보 접근 권한이 필요합니다.")
            .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 권한을 허용해주세요.")
            .setPermissions(*PERMISSIONS)
            .check()
    }

    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            goToMain()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            AlertDialog.Builder(this@SplashActivity)
                .setMessage("권한 거절로 인해 앱이 종료됩니다.")
                .setPositiveButton("권한 설정하러 가기") { _, _ ->
                    try {
                        val nextPage = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        startActivity(nextPage)
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

    private fun goToMain() {
        // 네트워크 상태 확인 후 메인으로 이동
        if (isNetworkConnected(this@SplashActivity)) {
            val nextPage = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(nextPage)
            finish()
        } else {
            AlertDialogFragment("네트워크 연결 상태를 확인 후 다시 앱을 실행해주세요.").apply {
                positiveButton("확인") { finish() }
            }.show(supportFragmentManager, "dialog")
        }
    }
}