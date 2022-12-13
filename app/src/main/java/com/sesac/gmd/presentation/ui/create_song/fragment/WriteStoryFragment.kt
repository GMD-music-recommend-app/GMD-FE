/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.fragment

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.CreateSongRepository
import com.sesac.gmd.databinding.FragmentWriteStoryBinding
import com.sesac.gmd.presentation.main.MainActivity
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.ui.factory.ViewModelFactory

private const val TAG = "WriteStoryFragment"

class WriteStoryFragment : BaseFragment<FragmentWriteStoryBinding>(FragmentWriteStoryBinding::inflate) {
    companion object {
        fun newInstance() = WriteStoryFragment()
    }
    private lateinit var viewModel: CreateSongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(CreateSongRepository())
        )[CreateSongViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: 사연 입력 여부 판별하는 유효성 검사 코드 추가 필요
        // TODO: 사연 입력 여부에 따라 음악 추가하기 버튼 색상 변경 코드 추가 필요

        viewModel.location.observe(viewLifecycleOwner) {
            Log.d("TEST_CODE", "WSF location observer called!")

            Log.d("TEST_CODE", "WSF lat : ${viewModel.location.value?.latitude}")
            Log.d("TEST_CODE", "WSF lng : ${viewModel.location.value?.longitude}")
            Log.d("TEST_CODE", "WSF state : ${viewModel.location.value?.state}")
            Log.d("TEST_CODE", "WSF city : ${viewModel.location.value?.city}")
            Log.d("TEST_CODE", "WSF street : ${viewModel.location.value?.street}")
        }

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

    // TODO: 비동기로 구현 필요
    private fun checkValidation(): Boolean {

        // TODO: 사연 입력 했는지 체크(MainThread 에서)
        // TODO: 중복 곡 인지 체크(background thread 에서)
        return true
    }
}