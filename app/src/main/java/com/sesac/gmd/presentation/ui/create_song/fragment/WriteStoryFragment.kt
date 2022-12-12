/*
* Created by gabriel
* date : 22/11/26
* */


package com.sesac.gmd.presentation.ui.create_song.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.data.repository.CreateSongRepository
import com.sesac.gmd.databinding.FragmentWriteStoryBinding
import com.sesac.gmd.presentation.main.MainActivity
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel

private const val TAG = "WriteStoryFragment"

class WriteStoryFragment : BaseFragment<FragmentWriteStoryBinding>(FragmentWriteStoryBinding::inflate) {
    private val viewModel = CreateSongViewModel(CreateSongRepository())

    companion object {
        fun newInstance() = WriteStoryFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: 사연 입력 여부 판별하는 유효성 검사 코드 추가 필요
        // TODO: 사연 입력 여부에 따라 음악 추가하기 버튼 색상 변경 코드 추가 필요

        // TODO: 추후 삭제 예정
        tempCode()
    }

    // TODO: 임시 작성 코드. 추후 삭제 예정
    private fun tempCode() {
        with(binding) {
            btnFinishCreate.setOnClickListener {
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }
    }
}