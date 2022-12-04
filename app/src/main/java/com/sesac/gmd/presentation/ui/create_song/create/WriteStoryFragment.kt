/*
* Created by gabriel
* date : 22/11/26
* */


package com.sesac.gmd.presentation.ui.create_song.create

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.databinding.FragmentWriteStoryBinding
import com.sesac.gmd.presentation.main.MainActivity

private const val TAG = "WriteStoryFragment"

class WriteStoryFragment : BaseFragment<FragmentWriteStoryBinding>(FragmentWriteStoryBinding::inflate) {

    companion object {
        fun newInstance() = WriteStoryFragment()

        fun newInstance(args: String): Fragment {
            val bundle = Bundle()
            bundle.putString("Story", args)

            return WriteStoryFragment().also {
                it.arguments = bundle
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: 사연 입력 여부 판별하는 유효성 검사 코드 추가 필요
        // TODO: 사연 입력 여부에 따라 음악 추가하기 버튼 색상 변경 코드 추가 필요

        // TODO: 추후 삭제 예정
        hardcode()
    }

    // TODO: 임시 작성 코드. 추후 삭제 예정
    private fun hardcode() {
        with(binding) {
            btnFinishCreate.setOnClickListener {
                Log.d(TAG, "in Bundle : ${arguments.toString()}")
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }
    }
}