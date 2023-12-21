package com.sesac.gmd.presentation.ui.main.my_page

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentMyPageBinding
import com.sesac.gmd.presentation.base.BaseFragment

class MyPageFragment : BaseFragment<FragmentMyPageBinding>() {
    override val layoutResourceId = R.layout.fragment_my_page

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = TODO("Not yet implemented")
}