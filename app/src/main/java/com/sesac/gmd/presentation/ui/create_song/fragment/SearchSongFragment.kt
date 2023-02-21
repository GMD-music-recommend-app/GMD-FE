package com.sesac.gmd.presentation.ui.create_song.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.sesac.gmd.R
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.Utils.Companion.displayToastExceptions
import com.sesac.gmd.common.util.Utils.Companion.hideKeyBoard
import com.sesac.gmd.common.util.Utils.Companion.setAlertDialog
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentSearchSongBinding
import com.sesac.gmd.presentation.ui.create_song.adapter.SearchSongAdapter
import com.sesac.gmd.presentation.ui.create_song.adapter.SearchSongDecoration
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 음악 검색 Fragment
 */
class SearchSongFragment : BaseFragment<FragmentSearchSongBinding>(FragmentSearchSongBinding::inflate) {
    companion object {
        private val TAG = SearchSongFragment::class.simpleName
        fun newInstance() = SearchSongFragment()
    }
    private val viewModel: CreateSongViewModel by activityViewModels { ViewModelFactory(Repository()) }
    private lateinit var recyclerAdapter: SearchSongAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // 사용자 위치 정보 초기화
        CoroutineScope(Dispatchers.IO).launch {
            initUserLocation()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel Observer 등록
        setObserver()
        // Listener 등록
        setListener()
    }

    /**
     * 사용자 위치 정보 초기화<br>
     * 음악 추가하기를 클릭한 동안 유저의 위치가 바뀔 수 있기 때문에
     * HomeCharViewModel 에서 가지고 있던 위치 정보 값을 사용하는 것이 아닌
     * 유저의 위치 정보를 새로 초기화
     */
    private suspend fun initUserLocation() {
        with(viewModel) {
            if (location.value == null) {
                try {
                    setCurrentUserLocation(requireContext())
                } catch (e: Exception) {
                    toastMessage(getString(R.string.error_not_found_user_location))
                }
            }
        }
    }

    // Observer Set
    private fun setObserver() = with(viewModel) {
        // observing saved location
        location.observe(viewLifecycleOwner) {
            // 위치 정보가 저장되어 있지 않다면 현재 사용자의 위치 정보를 저장
            if (it.latitude == 0.0 && it.state == null) {
                setAlertDialog(requireContext(),
                    getString(R.string.error_not_found_user_location),
                    getString(R.string.alert_go_to_set_location_page),
                    posFunc = {
                        // 위치 지정 페이지(FindOtherPlaceFragment)로 이동
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, FindOtherPlaceFragment.newInstance())
                            .commit()
                    },
                    negFunc = {
                        requireActivity().finish()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    })
            } else {
                Log.d(DEFAULT_TAG + TAG, "Location is initialized!")
            }
        }

        // observing progressBar status
        isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        // observing 음악 검색 결과 리스트
        songList.observe(viewLifecycleOwner) {
            with(binding) {
                // 검색 결과 없을 때 검색 결과 없음 안내하는 view 표시
                if (it.songs.size == 0) {
                    txtEmptyResult.visibility = View.VISIBLE
                } else {
                    recyclerAdapter = SearchSongAdapter(it,
                        onClickItem = {
                            setAlertDialog(requireContext(), null,
                                "${it.artist.joinToString ( "," )}의 " +
                                        "${it.songTitle}(을/를) 선택하시겠습니까?",
                                posFunc = {
                                    try {
                                        viewModel.addSong(it)
                                    } catch (e : Exception) {
                                        displayToastExceptions(e)
                                    }

                                    parentFragmentManager
                                        .beginTransaction()
                                        .replace(R.id.container, WriteStoryFragment.newInstance())
                                        .addToBackStack(null)
                                        .commit()
                                          },
                                negFunc = {})
                        })
                    rvResult.adapter = recyclerAdapter
                    rvResult.addItemDecoration(SearchSongDecoration())
                }
            }
        }
    }

    // Listener 초기화 함수
    private fun setListener() = with(binding) {
        // TextField 내 검색 버튼 클릭 시
        txtFieldSearchSong.setStartIconOnClickListener { searchSong() }

        // 키보드 검색버튼 클릭 시
        edtSearchSong.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                searchSong()
                true
            } else {
                false
            }
        }
    }

    // 음악 검색
    private fun searchSong() = with(binding) {
        // 키보드 내리기
        hideKeyBoard(requireActivity())
        // 검색 결과가 이미 표시 되어있다면 제거
        if (rvResult.adapter != null) {
            rvResult.adapter = null
        }
        // 검색 결과 없음 표시하는 view 가 이미 존재 한다면 view 제거
        if (txtEmptyResult.visibility == View.VISIBLE) {
            txtEmptyResult.visibility = View.GONE
        }
        // 음악 검색
        if (edtSearchSong.text!!.isEmpty()) {
            toastMessage(getString(R.string.error_keyword_not_assigned))
        } else if (edtSearchSong.text!!.length > 200) {
            toastMessage(getString(R.string.error_keyword_length_exceeded))
        } else {
            if (!validate()) {
                setAlertDialog(requireContext(), null,
                    getString(R.string.error_location_not_assigned) + " " +
                            getString(R.string.alert_go_to_set_location_page),
                    posFunc = {
                        // 위치 지정 페이지(FindOtherPlaceFragment)로 이동
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, FindOtherPlaceFragment.newInstance())
                            .commit()
                              },
                    negFunc = {
                        requireActivity().finish()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    })
            } else {
                try {
                    viewModel.getSong(edtSearchSong.text.toString())
                } catch (e : Exception) {
                    displayToastExceptions(e) // 오류 내용에 따라 ToastMessage 출력
                    requireActivity().finish()
                    Intent(requireContext(), MainActivity::class.java)
                }
            }
        }
    }

    // 유효성 검사 Location
    private fun validate(): Boolean {
        // TODO:  check JWT and userIdx
        return viewModel.location.value != null
    }
}