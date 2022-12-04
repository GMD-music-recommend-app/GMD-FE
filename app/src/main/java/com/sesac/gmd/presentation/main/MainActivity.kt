/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.sesac.gmd.R
import com.sesac.gmd.common.util.TAB_CHART
import com.sesac.gmd.common.util.TAB_HOME
import com.sesac.gmd.common.util.TAB_SETTING
import com.sesac.gmd.databinding.ActivityMainBinding
import com.sesac.gmd.presentation.ui.chart.ChartFragment
import com.sesac.gmd.presentation.ui.home.HomeFragment
import com.sesac.gmd.presentation.ui.setting.SettingFragment

private const val TAG = "MainActivity"

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
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(TAB_HOME))
        }
        // Listener 초기화
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
                    .commit()
            }
            TAB_HOME -> {
                ft.replace(R.id.tabContent, HomeFragment.newInstance())
                    .commit()
            }
            TAB_SETTING -> {
                ft.replace(R.id.tabContent, SettingFragment.newInstance())
                    .commit()
            }
            else -> throw IllegalStateException("Unexpected value : $tabPosition")
        }
    }

}