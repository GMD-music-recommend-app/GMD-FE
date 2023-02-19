package com.sesac.gmd.presentation.ui.main.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.sesac.gmd.R
import com.sesac.gmd.common.util.*
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.ActivityMainBinding
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.fragment.chart.ChartFragment
import com.sesac.gmd.presentation.ui.main.fragment.home.HomeFragment
import com.sesac.gmd.presentation.ui.main.fragment.setting.SettingFragment
import com.sesac.gmd.presentation.ui.main.viewmodel.HomeChartViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeChartViewModel by viewModels { ViewModelFactory(Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // Splash 에서 받아온 위치 정보에 따라 HomeFragment 의 위치 정보 값 초기화
        setHomeFragmentState(savedInstanceState)
        // Listener 등록
        setListener()
    }

    // 보여줄 Fragment setting
    private fun setHomeFragmentState(savedInstanceState: Bundle?) {
        // 최초 실행 시 Fragment 초기화
        if (savedInstanceState == null) {
            // Splash 에서 넘겨 준 위치 정보 가져오기
            val getLat = intent.getDoubleExtra(LATITUDE, 0.0)
            val getLng = intent.getDoubleExtra(LONGITUDE, 0.0)

            with(supportFragmentManager.beginTransaction()){
                // Splash 에서 위치 정보 값을 받아온 경우
                if (getLat != 0.0 && getLng != 0.0) {
                    add(R.id.tabContent, HomeFragment.newInstance(getLat, getLng))
                }
                // Splash 에서 위치 정보 값을 넘겨주지 못한 경우
                else {
                    add(R.id.tabContent, HomeFragment.newInstance())
                }
                commit()
            }
            // 탭 focus 홈으로 가도록 설정
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(TAB_HOME))
        }
    }

    // Listener 초기화 함수
    private fun setListener() {
        // Bottom Tab Click Listener
        with(binding.tabLayout) {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val ft = supportFragmentManager.beginTransaction()
                    setFragment(ft, tab.position)
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

        // 뒤로가기 시 View Stack 없으면 앱 종료
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
}