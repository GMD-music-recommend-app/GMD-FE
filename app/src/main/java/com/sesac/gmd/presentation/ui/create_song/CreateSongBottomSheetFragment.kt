/*
* Created by gabriel
* date : 22/11/24
* */

package com.sesac.gmd.presentation.ui.create_song

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sesac.gmd.databinding.FragmentCreateSongBottomSheetBinding

private const val TAG = "CreateSongBottomSheet"

class CreateSongBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentCreateSongBottomSheetBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = CreateSongBottomSheetFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateSongBottomSheetBinding.inflate(inflater, container, false)

        // Listener 초기화
        setListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // TODO: 임시 작성 코드. 추후 수정 필요
    private fun setListener() {
        with(binding) {
            val intent = Intent(requireContext(), CreateSongActivity::class.java)

            // 여기서 추천하기 버튼 클릭
            btnCreateHere.setOnClickListener {
                intent.putExtra("page", "here")
                dismiss()
                startActivity(intent)
            }

            // 다른 곳에서 추천하기 버튼 클릭
            btnCreateOtherPlace.setOnClickListener {
                intent.putExtra("page", "other")
                dismiss()
                startActivity(intent)
            }
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}