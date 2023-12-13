package com.sesac.gmd.presentation.ui.main.setting

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentSettingBinding
import com.sesac.gmd.presentation.base.BaseFragment

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override val layoutResourceId = R.layout.fragment_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = TODO("Not yet implemented")
}