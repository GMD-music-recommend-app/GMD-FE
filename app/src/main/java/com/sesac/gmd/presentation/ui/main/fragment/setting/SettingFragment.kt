/**
* Created by gabriel
* date : 22/11/21
*/
package com.sesac.gmd.presentation.ui.main.fragment.setting

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sesac.gmd.R
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    companion object {
        fun newInstance() = SettingFragment()
    }
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 앱 현재 버전 가져오기
        getVersion(requireContext())

        // Listener 등록
        setListener()
    }

    // 앱 현재 버전 가져오기
    private fun getVersion(context: Context) {
        var versionName = ""
        try {
            val pInfo = context.packageManager.getPackageInfoCompat(context.packageName, 0)
            versionName = "v " + pInfo.versionName + ""
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        binding.appVersion.text = versionName
    }

    // getPackageInfo deprecated 대응
    private fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0)
    : PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
        }

    // 임시 작성 코드
    private fun setListener() {
        with(binding) {
            containerSettingLogin.setOnClickListener {
                toastMessage(getString(R.string.alert_service_ready))
            }
            containerSettingMyHistory.setOnClickListener {
                toastMessage(getString(R.string.alert_service_ready))
            }
            containerUserNotification.setOnClickListener {
                toastMessage(getString(R.string.alert_service_ready))
            }
            containerSettingAppInfo.setOnClickListener {
                toastMessage(getString(R.string.alert_service_ready))
            }
        }
    }
}