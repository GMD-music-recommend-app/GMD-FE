package com.sesac.gmd.presentation.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayout
import com.sesac.gmd.R
import com.sesac.gmd.common.LOCATION_UPDATE_INTERVAL_TIME
import com.sesac.gmd.common.Logger
import com.sesac.gmd.common.TAB_CHART
import com.sesac.gmd.common.TAB_HOME
import com.sesac.gmd.common.TAB_SETTING
import com.sesac.gmd.databinding.ActivityMainBinding
import com.sesac.gmd.presentation.ui.main.chart.ChartFragment
import com.sesac.gmd.presentation.ui.main.home.HomeFragment
import com.sesac.gmd.presentation.ui.main.setting.SettingFragment

// TODO: Permission check before loading google map
// TODO: tabLayout focus on center content

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: LocationViewModel by viewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        initViews(savedInstanceState)
        initFusedLocationClient()
        setListener()
    }

    private fun initViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.ft_container, HomeFragment())
                .commit()
        }
    }

    private fun initFusedLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setListener() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                replaceFragment(tab.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun replaceFragment(tabPosition: Int) {
        val fragment: Fragment? = when (tabPosition) {
            TAB_CHART -> ChartFragment()
            TAB_HOME -> HomeFragment()
            TAB_SETTING -> SettingFragment()
            else -> null
        }

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.ft_container,
                fragment ?: throw IllegalStateException("Unexpected value : $tabPosition")
            )
            .commit()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdate()
    }

    private fun startLocationUpdate() {
        if (::fusedLocationProviderClient.isInitialized) {
            refreshUserLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun refreshUserLocation() {
        Logger.d("try to update location...")

        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_UPDATE_INTERVAL_TIME)
                .apply {
                    setIntervalMillis(LOCATION_UPDATE_INTERVAL_TIME)        // 위치 획득 후 업데이트 되는 최소 주기
                    setMaxUpdateDelayMillis(LOCATION_UPDATE_INTERVAL_TIME)  // 위치 업데이트 최대 지연 시간
                    setWaitForAccurateLocation(true)                        // 정밀한 위치를 받기 위해 대기(wait) 가능
                }.build()
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    /**
     * 위치 정보가 update 됐을 때 호출되는 콜백
     * 위치 정보가 update 될 때 현재 위치 정보를 Geocoding 하여 LiveData<Location>에 저장
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Logger.d("""Success to update location!
                Latitude : ${locationResult.lastLocation?.latitude}
                Longitude : ${locationResult.lastLocation?.longitude}
            """.trimIndent()
            )

            viewModel.updateCurrentLocation(
                LatLng(
                    locationResult.lastLocation!!.latitude,
                    locationResult.lastLocation!!.longitude
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        // 위치 업데이트 중지
        stopLocationUpdate()
    }

    private fun stopLocationUpdate() {
        try {
            // FusedLocation Callback 해제
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO: 예외 처리 필요
        }
    }
}