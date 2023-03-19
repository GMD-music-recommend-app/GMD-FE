package com.sesac.gmd.presentation.ui.main.activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.sesac.gmd.R
import com.sesac.gmd.common.util.*
import com.sesac.gmd.common.util.PermissionsUtil.Companion.PERMISSIONS
import com.sesac.gmd.common.util.Utils.Companion.displayToastExceptions
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.ActivityMainBinding
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.fragment.chart.ChartFragment
import com.sesac.gmd.presentation.ui.main.fragment.home.HomeFragment
import com.sesac.gmd.presentation.ui.main.fragment.setting.SettingFragment
import com.sesac.gmd.presentation.ui.main.viewmodel.HomeChartViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: HomeChartViewModel by viewModels { ViewModelFactory(Repository()) }
    private var isPermissionsGranted = false    // 필요 권한 동의 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        // HomeFragment 초기화
        initViews()

        // FusedLocation 객체 초기화
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // 필요한 권한 확인
        checkPermissions()

        // Listener 등록
        setListener()
    }

    // HomeFragment 초기화
    private fun initViews() {
        supportFragmentManager.beginTransaction()
            .add(R.id.tabContent, HomeFragment.newInstance())
            .commit()

        // Bottom Tab focus 홈(중앙)으로 가도록 설정
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(TAB_HOME))
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
            isPermissionsGranted = true // 권한 확인 완료
        }
    }

    private val permissionListener = object : PermissionListener {
        // 권한 허가시 실행 할 내용
        override fun onPermissionGranted() {
            isPermissionsGranted = true // 권한 확인 완료
        }

        // 권한 거부시 실행  할 내용
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            AlertDialog.Builder(this@MainActivity)
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

    // Listener 초기화 함수
    private fun setListener() {
        // Bottom Tab Click Listener
        with(binding.tabLayout) {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val ft = supportFragmentManager.beginTransaction()
                    try {
                        setFragment(ft, tab.position)
                    }
                    catch (e: Exception) {
                        displayToastExceptions(e)
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

        // 뒤로 가기 클릭 시 Stack 비어 있다면 앱 종료
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.backStackEntryCount == 0) {
                    finish()
                }
            }
        })
    }

    // Fragment 교체 함수
    private fun setFragment(ft: FragmentTransaction, tabPosition: Int) = when(tabPosition) {
        TAB_CHART -> {
            ft.replace(R.id.tabContent, ChartFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        TAB_HOME -> {
            ft.replace(R.id.tabContent, HomeFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        TAB_SETTING -> {
            ft.replace(R.id.tabContent, SettingFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        else -> throw IllegalStateException("Unexpected value : $tabPosition")
    }

    override fun onResume() {
        super.onResume()
        /**
         * 실시간 사용자 위치 업데이트
         * 권한 확인 및 FusedLocation 객체 초기화 된 경우에만 실행
         */
        if (isPermissionsGranted && ::fusedLocationProviderClient.isInitialized) {
            refreshUserLocation()
        }
    }

    // 주기적으로 일정 시간(5분)마다 사용자의 현재 위치 정보 업데이트하여 LiveData<Location>에 저장
    @SuppressLint("MissingPermission")
    private fun refreshUserLocation() {
        val intervalTime = 10 * 60 * 1_000L          // 위치 업데이트 주기 = 10분
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalTime).apply {
            setIntervalMillis(intervalTime)         // 위치 획득 후 업데이트 되는 최소 주기
            setMaxUpdateDelayMillis(intervalTime)   // 위치 업데이트 최대 지연 시간
            setWaitForAccurateLocation(true)        // 정밀한 위치를 받기 위해 대기(wait) 가능
        }.build()
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    /**
     * 위치 정보가 update 됐을 때 자동으로 호출되는 콜백 메소드
     * 위치 정보가 update 됐다면 update 된 위치 정보를 Geocoding 하여 LiveData<Location>에 저장
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            viewModel.saveCurrentLocation(
                this@MainActivity,
                LatLng(locationResult.lastLocation!!.latitude, locationResult.lastLocation!!.longitude)
            )
        }
    }

    override fun onPause() {
        super.onPause()
        // 위치 업데이트 중지
        stopLocationUpdates()
    }

    // FusedLocation 위치 Update 중지
    private fun stopLocationUpdates() {
        try {
            // FusedLocation Callback 해제
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            toastMessage(getString(R.string.unexpected_error))
        }
    }
}