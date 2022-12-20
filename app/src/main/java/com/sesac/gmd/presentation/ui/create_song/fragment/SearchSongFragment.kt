/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sesac.gmd.R
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.Utils.Companion.hideKeyBoard
import com.sesac.gmd.common.util.Utils.Companion.setAlertDialog
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentSearchSongBinding
import com.sesac.gmd.presentation.main.MainActivity
import com.sesac.gmd.presentation.ui.create_song.adapter.SearchSongAdapter
import com.sesac.gmd.presentation.ui.create_song.adapter.SearchSongDecoration
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.ui.factory.ViewModelFactory

class SearchSongFragment : Fragment() {
    companion object {
        fun newInstance() = SearchSongFragment()
        const val TAG = "SearchSongFragment"
    }
    private lateinit var binding: FragmentSearchSongBinding
    private lateinit var viewModel: CreateSongViewModel
    private lateinit var recyclerAdapter: SearchSongAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchSongBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(Repository())
        )[CreateSongViewModel::class.java]

        // 사용자 위치 정보 초기화
        initUserLocation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel Observer 등록
        setObserver()
        // Listener 등록
        setListener()
    }

    // 사용자 위치 정보 초기화
    private fun initUserLocation() {
        with(viewModel) {
            if (location.value == null) {
                getCurrentLocation(requireActivity())
            }
        }
    }

    // Observer Set
    private fun setObserver(){
        with(viewModel) {
            location.observe(viewLifecycleOwner) {
                // 위치 정보가 저장되어 있지 않다면 현재 사용자의 위치 정보를 저장
                if (it.latitude == 0.0 && it.state == null) {
                    setAlertDialog(requireContext(),
                        "위치 정보를 가져오는 데 실패했습니다.",
                        "위치 지정 페이지로 이동하시겠습니까?",
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
            // progressBar status
            isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                }
                else {
                    binding.progressBar.visibility = View.GONE
                }
            }
            // 음악 검색 결과 리스트
            songList.observe(viewLifecycleOwner){
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
                                        viewModel.addSong(it)
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
    }

    // Listener 초기화 함수
    private fun setListener() {
        with(binding) {
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
    }

    // 음악 검색
    private fun searchSong() {
        with(binding) {
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
                toastMessage("검색어를 입력해주세요!")
            } else if (edtSearchSong.text!!.length > 200) {
                toastMessage("검색어는 200자 까지 입력 가능합니다.")
            } else {
                if (!validate()) {
                    setAlertDialog(requireContext(), null,
                        "위치가 지정되지 않았습니다. 위치 지정 페이지로 이동하시겠습니까?",
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
                    viewModel.getSong(edtSearchSong.text.toString())
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