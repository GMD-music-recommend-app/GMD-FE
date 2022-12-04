/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.create

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.gmd.R
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.databinding.FragmentSearchSongBinding

private const val TAG = "SearchSongFragment"

class SearchSongFragment : BaseFragment<FragmentSearchSongBinding>(FragmentSearchSongBinding::inflate) {

    companion object {
        fun newInstance() = SearchSongFragment()

        // TODO: 찾은 노래를 객체로 만들어 다음 페이지로 전달하도록 코드 수정 필요
        fun newInstance(args: String): Fragment {
            val bundle = Bundle()
            bundle.putString("Song", args)

            return SearchSongFragment().also {
                it.arguments = bundle
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 노래 추가 여부에 따라 버튼 형태 변경되도록 코드 수정 필요
        // 노래 선택이 완료되면 다음으로 버튼이 빨간 색으로 변경
        with(binding) {
            edtSearchSong.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().length > start) {
                        btnNextPage.setBackgroundColor(R.color.main_color)
                        btnNextPage.isCheckable = true
                    } else {
                        btnNextPage.isCheckable = false
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        // TODO: 노래 추가 여부에 따라 다음 페이지로 이동할 수 있도록 코드 수정 필요
        val supportFragmentManager = parentFragmentManager

        with(binding) {
            btnNextPage.setOnClickListener {
                // 노래를 추가한 상태라면 다음 페이지로 이동
                if (edtSearchSong.text.toString() != "제목, 가수, 앨범을 입력하세요.") {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.container, WriteStoryFragment().apply {
                            arguments = Bundle().apply {
                                putString("SONG", edtSearchSong.text.toString())  // <- 대충 이런 방식
                            }
                        })
                        .addToBackStack(null)
                        .commit()
                }

                // 노래를 추가하지 않았다면 Toast 출력
                else {
                    toastMessage("노래를 선택하지 않으셨습니다.")
                }
            }
        }
    }
}