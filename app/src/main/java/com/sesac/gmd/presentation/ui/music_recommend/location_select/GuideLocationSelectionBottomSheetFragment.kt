package com.sesac.gmd.presentation.ui.music_recommend.location_select

import android.os.Bundle
import android.view.View
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentGuideLocationSelectionBottomSheetBinding
import com.sesac.gmd.presentation.base.BaseBottomSheetFragment

class GuideLocationSelectionBottomSheetFragment :
    BaseBottomSheetFragment<FragmentGuideLocationSelectionBottomSheetBinding>() {
    override val layoutResourceId = R.layout.fragment_guide_location_selection_bottom_sheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    private fun setListener() = with(binding) {
        btnConfirm.setOnClickListener { dismiss() }
        btnClose.setOnClickListener { dismiss() }
    }
}