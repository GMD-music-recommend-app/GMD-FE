/**
* Created by 조진수
* date : 22/11/21
*/
package com.sesac.gmd.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.google.android.libraries.places.api.Places
import com.kakao.sdk.common.KakaoSdk
import com.sesac.gmd.R
import java.util.*

/**
 * 앱 런처 아이콘을 터치하면 처음 실행되는 코드
 * App Scope 모든 코틀린 클래스/파일에서 호출할 수 있는 코드
 */
class GMDApplication : Application(){
    companion object {
        private lateinit var appInstance : GMDApplication
        fun getAppInstance() = appInstance
    }

    override fun onCreate() {
        super.onCreate()
        // 카카오 sdk 네이티브 앱 키 추가
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        appInstance = this
        settingScreenPortrait()
        initPlaceSDK()
    }

    /**
    * 앱의 화면을 수직으로 구성
    */
    private fun settingScreenPortrait() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            @SuppressLint("SourceLockedOrientationActivity")
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    /**
    * 앱 시작 시 구글맵 플레이스 초기화
    */
    private fun initPlaceSDK() {
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key), Locale.KOREA)
        }
    }
}