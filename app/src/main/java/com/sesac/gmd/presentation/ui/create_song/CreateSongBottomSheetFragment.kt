/*
* Created by gabriel
* date : 22/11/24
* */

package com.sesac.gmd.presentation.ui.create_song

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
            btnCreateHere.setOnClickListener {
                Log.d(TAG, "여기서 추천하기 Clicked!")
                startActivity(Intent(requireContext(), CreateSongActivity::class.java))
            }
            btnCreateOtherPlace.setOnClickListener {
                Log.d(TAG, "다른 곳에서 추천하기 Clicked!")
                startActivityForResult(Intent(requireContext(), CreateSongActivity::class.java), 123)
                activity?.setResult(RESULT_OK)
                dismiss()
                activity?.finish()
            }
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}