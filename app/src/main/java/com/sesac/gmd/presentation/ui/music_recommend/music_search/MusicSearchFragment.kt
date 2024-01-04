package com.sesac.gmd.presentation.ui.music_recommend.music_search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sesac.gmd.R
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.databinding.FragmentMusicSearchBinding
import com.sesac.gmd.presentation.base.BaseFragment
import com.sesac.gmd.presentation.common.AlertDialogFragment
import com.sesac.gmd.presentation.ui.main.MainActivity
import com.sesac.gmd.presentation.ui.music_recommend.SongViewModel

class MusicSearchFragment: BaseFragment<FragmentMusicSearchBinding>() {
    override val layoutResourceId = R.layout.fragment_music_search
    private val activityViewModel: SongViewModel by activityViewModels()
    private val viewModel: MusicSearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        setListener()
    }

    private fun setObserver() {
        activityViewModel.selectedLocation.observe(viewLifecycleOwner, this@MusicSearchFragment::checkLocationInitialized)
    }
    
    private fun checkLocationInitialized(location: Location?) {
        if (location == null) {
            AlertDialogFragment(
                message = "사용자의 위치 정보를 가져오는 데 실패했습니다.\n위치 지정 페이지로 이동하시겠습니까?"
            ).apply {
                positiveButton(
                    text = "확인",
                    action = {
                        // TODO: navigate.LocationFragment 
                    }
                )
                negativeButton(
                    text = "취소",
                    action = {
                        requireActivity().finish()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                )
            }.also {
                it.show(parentFragmentManager, "dialog")
            }
        }
    }

    private fun setListener() = with(binding) {
        // TextField 내 검색 버튼 클릭 시
        txtFieldSearchMusic.setStartIconOnClickListener { viewModel.searchMusic(edtSearchMusic.text.toString()) }

        // Soft Keyboard 내 검색 버튼 클릭 시
        edtSearchMusic.setOnKeyListener { _, keyCode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                viewModel.searchMusic(edtSearchMusic.text.toString())
                true
            } else {
                false
            }
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = TODO("Not yet implemented")
}