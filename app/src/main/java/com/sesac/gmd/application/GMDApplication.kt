package com.sesac.gmd.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.google.android.libraries.places.api.Places
import com.sesac.gmd.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class GMDApplication : Application() {
    companion object {
        private lateinit var appInstance: GMDApplication
        fun getAppInstance() = appInstance
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        initGooglePlacesSDK()
//        setScreenPortrait()
    }

    private fun initGooglePlacesSDK() {
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.GOOGLE_API_KEY, Locale.KOREA)
        }
    }

    private fun setScreenPortrait() {
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
}