/*
* Created by gabriel
* date : 22/12/01
* */

package com.sesac.gmd.presentation.ui.create_song.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sesac.gmd.databinding.FragmentFindOtherPlaceBottomSheetBinding

class FindOtherPlaceBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = FindOtherPlaceBottomSheetFragment()
    }
    private lateinit var binding: FragmentFindOtherPlaceBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFindOtherPlaceBottomSheetBinding.inflate(inflater, container, false)

        binding.btnOk.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}