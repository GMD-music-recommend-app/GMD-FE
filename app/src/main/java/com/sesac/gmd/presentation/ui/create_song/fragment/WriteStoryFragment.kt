/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sesac.gmd.R
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.Utils.Companion.setAlertDialog
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.CreateSongRepository
import com.sesac.gmd.databinding.FragmentWriteStoryBinding
import com.sesac.gmd.presentation.main.MainActivity
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.ui.factory.ViewModelFactory

class WriteStoryFragment : Fragment() {
    companion object {
        fun newInstance() = WriteStoryFragment()
        const val TAG = "WriteStoryFragment"
    }
    private lateinit var binding: FragmentWriteStoryBinding
    private lateinit var viewModel: CreateSongViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWriteStoryBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(CreateSongRepository())
        )[CreateSongViewModel::class.java]


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener 등록
        setListener()
        // 생성하기 버튼 초기화
        setButtonState()
    }

    // Listener 초기화 함수
    private fun setListener() {
        with(binding) {
            // 생성하기 버튼
            btnFinishCreate.setOnClickListener {
                val reason = edtStory.text.toString()
                val hashtag = txtHashtag.text.toString()

                if(checkValidation()) {
                    setAlertDialog(requireContext(), null, "음악을 추가하시겠습니까?",
                        posFunc = {
                            viewModel.createPin(reason, hashtag)
                            //startActivity(Intent(context, MainActivity::class.java))
                        },
                        negFunc = {})
                }
            }
        }
    }

    // 버튼 상태 초기화 함수
    private fun setButtonState() {
        with(binding) {
            // TODO: 사연 입력되지 않았을 때 버튼 비활성화 구현 필요
            edtStory.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.isEmpty()) {
                        btnFinishCreate.setBackgroundResource(R.drawable.bg_btn_gray)
                        btnFinishCreate.isEnabled = false
                    } else {
                        btnFinishCreate.setBackgroundResource(R.drawable.bg_btn_main_color)
                        btnFinishCreate.isEnabled = true
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
        }
    }

    // TODO: 비동기로 구현 필요
    // 유효성 검사
    private fun checkValidation(): Boolean {
        var flag = true

        // Check Song
        if (viewModel.selectedSong == null) {
            Log.d(DEFAULT_TAG+TAG, "selectSong : ${viewModel.selectedSong}")
            toastMessage("음악이 선택되지 않았습니다.")
            flag = false
        }
        with(binding) {
            // Check Story isNull
            if (edtStory.text!!.isEmpty()) {
                Log.d(DEFAULT_TAG+TAG, "story : ${edtStory.text}")
                toastMessage("사연을 입력해주세요.")
                flag = false
            // Check Story Length
            } else if (edtStory.text!!.length > 140) {
                Log.d(DEFAULT_TAG+TAG, "story length : ${edtStory.text!!.length}")
                toastMessage("사연은 최대 140자 까지 입력 가능합니다.")
                flag = false
            }
            // Check Hashtag Count
            if (edtHashtag.text!!.count{it == '#'} > 3) {
                Log.d(DEFAULT_TAG+TAG, "hashtag count : ${edtHashtag.text!!.count{it == '#'}}")
                toastMessage("해시태그는 최대 3개까지 입력 가능합니다.")
                flag = false
            // Check Hashtag First Character
            } else if (edtHashtag.text!![0] != '#') {
                Log.d(DEFAULT_TAG+TAG, "hashtag first Character : ${edtHashtag.text!![0]}")
                toastMessage("해시태그가 잘못 입력되었습니다.")
                flag = false
            // Check Hashtag Content
            } else if (edtHashtag.text!![0] == '#' && edtHashtag.text!![1] in "!@#$%^&*()_-~`+><,./?") {
                Log.d(DEFAULT_TAG+TAG, "hashtag second Character : ${edtHashtag.text!![1]}")
                toastMessage("해시태그가 잘못 입력되었습니다.")
                flag = false
            }
        }
        return flag
    }
}