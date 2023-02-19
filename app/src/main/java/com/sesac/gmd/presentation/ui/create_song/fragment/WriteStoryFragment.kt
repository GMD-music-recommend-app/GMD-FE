package com.sesac.gmd.presentation.ui.create_song.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.sesac.gmd.R
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.Utils.Companion.displayToastExceptions
import com.sesac.gmd.common.util.Utils.Companion.setAlertDialog
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentWriteStoryBinding
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.activity.MainActivity

/**
 * 사연 입력, 해시태그 입력 후 음악(핀) 생성 Fragment
 */
class WriteStoryFragment : BaseFragment<FragmentWriteStoryBinding>(FragmentWriteStoryBinding::inflate) {
    companion object {
        private val TAG = WriteStoryFragment::class.simpleName
        fun newInstance() = WriteStoryFragment()
    }
    private val viewModel: CreateSongViewModel by activityViewModels { ViewModelFactory(Repository()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer 등록
        setObserver()
        // Listener 등록
        setListener()
        // 생성하기 버튼 초기화
        setButtonState()
    }

    // Observer Set
    private fun setObserver() = with(viewModel) {
        createSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                toastMessage(getString(R.string.success_to_create_pin))
                requireActivity().finish()
            }
        }
    }

    // Listener 초기화 함수
    private fun setListener() = with(binding) {
        // 음악 핀 생성하기 버튼
        btnFinishCreate.setOnClickListener {
            val reason = edtStory.text.toString()
            val hashtag = edtHashtag.text.toString()

            if (checkValidation()) {
                setAlertDialog(requireContext(), null, getString(R.string.alert_finish_pin_create),
                    posFunc = {
                        try {
                            viewModel.createPin(reason, hashtag)
                        } catch (e: Exception) {
                            displayToastExceptions(e)
                        }
                        startActivity(Intent(context, MainActivity::class.java))
                    },
                    negFunc = {})
            }
        }
    }

    // 버튼 상태 초기화 함수
    private fun setButtonState() = with(binding) {
        edtStory.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    btnFinishCreate.setBackgroundResource(R.drawable.bg_btn_gray_rectangle)
                    btnFinishCreate.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    btnFinishCreate.isEnabled = false
                } else {
                    btnFinishCreate.setBackgroundResource(R.drawable.bg_btn_main_color_rectangle)
                    btnFinishCreate.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    btnFinishCreate.isEnabled = true
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // 유효성 검사   TODO: 비동기로 구현 필요
    private fun checkValidation(): Boolean {
        var flag = true

        // Check Song
        if (viewModel.selectedSong.value == null) {
            toastMessage(getString(R.string.error_music_not_assigned))
            flag = false
        }

        with(binding) {
            // Check Story isNull
            if (edtStory.text!!.isEmpty()) {
                Log.d(DEFAULT_TAG+TAG, "story : ${edtStory.text}")
                toastMessage(getString(R.string.error_story_not_assigned))
                flag = false
            // Check Story Length
            } else if (edtStory.text!!.length > 140) {
                Log.d(DEFAULT_TAG+TAG, "story length : ${edtStory.text!!.length}")
                toastMessage(getString(R.string.error_story_length_exceeded))
                flag = false
            }
            // TODO: 유효성 검사 수정 필요
//            // Check Hashtag Count
//            if (edtHashtag.text!!.count{it == '#'} > 3) {
//                Log.d(DEFAULT_TAG+TAG, "hashtag count : ${edtHashtag.text!!.count{it == '#'}}")
//                toastMessage("해시태그는 최대 3개까지 입력 가능합니다.")
//                flag = false
//            // Check Hashtag First Character
//            } else if (edtHashtag.text!![0] != '#') {
//                Log.d(DEFAULT_TAG+TAG, "hashtag first Character : ${edtHashtag.text!![0]}")
//                toastMessage("해시태그가 잘못 입력되었습니다.")
//                flag = false
//            // Check Hashtag Content
//            } else if (edtHashtag.text!![0] == '#' && edtHashtag.text!![1] in "!@#$%^&*()_-~`+><,./?") {
//                Log.d(DEFAULT_TAG+TAG, "hashtag second Character : ${edtHashtag.text!![1]}")
//                toastMessage("해시태그가 잘못 입력되었습니다.")
//                flag = false
//            }
        }
        return flag
    }
}