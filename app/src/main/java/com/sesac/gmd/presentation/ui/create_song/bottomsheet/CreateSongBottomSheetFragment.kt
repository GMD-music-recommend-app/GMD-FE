/*
* Created by gabriel
* date : 22/11/24
* */

package com.sesac.gmd.presentation.ui.create_song.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sesac.gmd.databinding.FragmentCreateSongBottomSheetBinding
import com.sesac.gmd.presentation.ui.create_song.activity.CreateSongActivity

class CreateSongBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = CreateSongBottomSheetFragment()
    }
    private var _binding: FragmentCreateSongBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateSongBottomSheetBinding.inflate(inflater, container, false)

        // Listener 등록
        setListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Listener 초기화 함수
    private fun setListener() {
        with(binding) {
            val intent = Intent(requireContext(), CreateSongActivity::class.java)

            // 여기서 추천하기 버튼 클릭
            btnCreateHere.setOnClickListener {
                intent.putExtra("GO_TO_PAGE", "CreateSongHere")
                dismiss()
                startActivity(intent)
            }
            // 다른 곳에서 추천하기 버튼 클릭
            btnCreateOtherPlace.setOnClickListener {
                intent.putExtra("GO_TO_PAGE", "SetOtherPlace")
                dismiss()
                startActivity(intent)
            }
            // 바텀시트 닫기
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}