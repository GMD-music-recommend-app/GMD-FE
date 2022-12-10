/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.google.android.libraries.places.api.Places
import com.sesac.gmd.R
import java.util.*

// 앱 런처 아이콘을 터치하면 처음 실행되는 코드
// App Scope 모든 코틀린 클래스/파일에서 호출할 수 있는 코드
class GMDApplication : Application(){
    companion object {
        private lateinit var appInstance : GMDApplication
        fun getAppInstance() = appInstance
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        settingScreenPortrait()
        initPlaceSDK()
    }

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

    private fun initPlaceSDK() {
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_places_key), Locale.US);
        }
    }

}