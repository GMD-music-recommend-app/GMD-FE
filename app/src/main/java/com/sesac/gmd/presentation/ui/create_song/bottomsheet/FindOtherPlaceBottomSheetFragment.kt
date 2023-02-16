/**
* Created by 조진수
* date : 22/12/01
*/
package com.sesac.gmd.presentation.ui.create_song.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sesac.gmd.databinding.FragmentFindOtherPlaceBottomSheetBinding

/**
 * FindOtherPlaceFragment 에서 표시 될 BottomSheetDialog<br>
 * 해당 페이지의 사용 방법 안내
 */
class FindOtherPlaceBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = FindOtherPlaceBottomSheetFragment()
    }
    private lateinit var binding: FragmentFindOtherPlaceBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFindOtherPlaceBottomSheetBinding.inflate(inflater, container, false)

        // Listener 등록
        setListener()

        return binding.root
    }

    // Listener 초기화
    private fun setListener() = with(binding) {
        btnOk.setOnClickListener { dismiss() }
    }
}