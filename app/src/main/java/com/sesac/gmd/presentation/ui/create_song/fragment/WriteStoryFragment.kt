/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sesac.gmd.R
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

                // TODO: 유효성 검사 필요
                AlertDialog.Builder(context)
                    .setMessage(
                        "음악을 추가하시겠습니까?"
                    )
                    .setPositiveButton("예") { _, _ ->
                        viewModel.createPin(reason, hashtag)
                        startActivity(Intent(context, MainActivity::class.java))
                    }
                    .setNegativeButton("아니오") { _, _ -> }
                    .create()
                    .show()

//                val isSuccess = viewModel.createPin(reason, hashtag)
//
//                val intent = Intent(context, MainActivity::class.java)
//                intent.putExtra("CREATE_PIN", isSuccess)
//                Log.d(DEFAULT_TAG+TAG, "code : $isSuccess")
//                startActivity(intent)
            }
        }
    }

    // 버튼 상태 초기화 함수
    @SuppressLint("ResourceAsColor")
    private fun setButtonState() {
        with(binding) {
            // TODO: 사연 입력되지 않았을 때 버튼 비활성화 구현 필요
            if (edtStory.text?.isEmpty() == true) {
                btnFinishCreate.setBackgroundColor(R.color.teal_200)
                btnFinishCreate.isCheckable = false
            } else {
                btnFinishCreate.setBackgroundColor(R.color.main_color)
                btnFinishCreate.isCheckable = true
            }
        }
    }

    // TODO: 비동기로 구현 필요
    // 유효성 검사사
    private fun checkValidation(): Boolean {
        // TODO:  check location
        // TODO:  check JWT and userIdx
        // TODO:  check song
        // TODO:  check story isNull
        // TODO:  check story length
        // TODO:  check hashtag count

        return true
    }
}

//class WriteStoryFragment : BaseFragment<FragmentWriteStoryBinding>(FragmentWriteStoryBinding::inflate) {
//    companion object {
//        fun newInstance() = WriteStoryFragment()
//    }
//    private lateinit var viewModel: CreateSongViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        viewModel = ViewModelProvider(
//            requireActivity(), ViewModelFactory(CreateSongRepository())
//        )[CreateSongViewModel::class.java]
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // TODO: 사연 입력 여부 판별하는 유효성 검사 코드 추가 필요
//        // TODO: 사연 입력 여부에 따라 음악 추가하기 버튼 색상 변경 코드 추가 필요
//
//        viewModel.location.observe(viewLifecycleOwner) {
//            Log.d("TEST_CODE", "WSF location observer called!")
//
//            Log.d("TEST_CODE", "WSF lat : ${viewModel.location.value?.latitude}")
//            Log.d("TEST_CODE", "WSF lng : ${viewModel.location.value?.longitude}")
//            Log.d("TEST_CODE", "WSF state : ${viewModel.location.value?.state}")
//            Log.d("TEST_CODE", "WSF city : ${viewModel.location.value?.city}")
//            Log.d("TEST_CODE", "WSF street : ${viewModel.location.value?.street}")
//        }
//
//        // TODO: 추후 삭제 예정
//        tempCode()
//    }
//
//    // TODO: 임시 작성 코드. 추후 삭제 예정
//    private fun tempCode() {
//        with(binding) {
//            btnFinishCreate.setOnClickListener {
//                startActivity(Intent(requireContext(), MainActivity::class.java))
//            }
//        }
//    }
//
//    // TODO: 비동기로 구현 필요
//    private fun checkValidation(): Boolean {
//
//        // TODO: 사연 입력 했는지 체크(MainThread 에서)
//        // TODO: 중복 곡 인지 체크(background thread 에서)
//        return true
//    }
//}