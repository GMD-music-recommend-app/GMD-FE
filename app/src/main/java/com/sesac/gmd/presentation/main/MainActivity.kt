/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.sesac.gmd.R
import com.sesac.gmd.common.util.*
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.databinding.ActivityMainBinding
import com.sesac.gmd.presentation.ui.chart.ChartFragment
import com.sesac.gmd.presentation.ui.home.HomeFragment
import com.sesac.gmd.presentation.ui.setting.SettingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        // 최초 실행 시 Fragment 초기화
        if (savedInstanceState == null) {
            with(supportFragmentManager.beginTransaction()){
                add(R.id.tabContent, HomeFragment.newInstance())
                commit()
            }
            // 탭 focus 홈으로 가도록 설정
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(TAB_HOME))
            Log.d(DEFAULT_TAG+"MainActivity", "code : ${intent.getIntExtra("CREATE_PIN", 0)}")
            // 음악 핀 추가 결과에 따른 Toast 출력
            when(intent.getIntExtra("CREATE_PIN", 0)) {
                SUCCESS -> toastMessage("생성 성공")
                FAILURE -> toastMessage("예기치 못한 오류가 발생했습니다.")
                -1 -> toastMessage("error : isSuccess not initialized!")
                else -> {}
            }
        }
        // Listener 등록
        setListener()
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
    private fun setFragment(ft: FragmentTransaction, tabPosition: Int) {
        when(tabPosition) {
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
}