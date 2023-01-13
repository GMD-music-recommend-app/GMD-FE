/**
* Created by 조진수
* date : 22/11/24
*/
package com.sesac.gmd.presentation.ui.main.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sesac.gmd.databinding.FragmentCreateSongBottomSheetBinding
import com.sesac.gmd.presentation.ui.create_song.activity.CreateSongActivity

/**
 * 음악 추가하기 버튼 클릭 시 표시되는 BottomSheetDialog
 * 여기에 추가하기, 다른 곳에 추가하기
 */
class CreateSongBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = CreateSongBottomSheetFragment()
    }
    private lateinit var binding: FragmentCreateSongBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateSongBottomSheetBinding.inflate(inflater, container, false)

        // Listener 등록
        setListener()

        return binding.root
    }

    // Listener 초기화 함수
    private fun setListener() {
        with(binding) {
            val nextPage = Intent(requireContext(), CreateSongActivity::class.java)

            // 여기서 추천하기 버튼 클릭
            btnCreateHere.setOnClickListener {
                nextPage.putExtra("GO_TO_PAGE", "CreateSongHere")
                dismiss()
                startActivity(nextPage)
            }
            // 다른 곳에서 추천하기 버튼 클릭
            btnCreateOtherPlace.setOnClickListener {
                nextPage.putExtra("GO_TO_PAGE", "SetOtherPlace")
                dismiss()
                startActivity(nextPage)
            }
            // 바텀시트 닫기
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}