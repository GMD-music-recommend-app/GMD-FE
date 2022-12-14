/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.fragment

import android.app.AlertDialog
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
import com.sesac.gmd.common.util.Utils.Companion.hideKeyBoard
import com.sesac.gmd.data.repository.CreateSongRepository
import com.sesac.gmd.databinding.FragmentSearchSongBinding
import com.sesac.gmd.presentation.main.MainActivity
import com.sesac.gmd.presentation.ui.create_song.adapter.SearchSongAdapter
import com.sesac.gmd.presentation.ui.create_song.adapter.SearchSongDecoration
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.ui.factory.ViewModelFactory

class SearchSongFragment : Fragment() {
    companion object {
        fun newInstance() = SearchSongFragment()
    }
    private lateinit var binding: FragmentSearchSongBinding
    private lateinit var viewModel: CreateSongViewModel
    private lateinit var recyclerAdapter: SearchSongAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchSongBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(CreateSongRepository())
        )[CreateSongViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 사용자 위치 정보 초기화
        viewModel.getCurrentLocation(requireActivity())

        // ViewModel Observer 등록
        setObserver()
        // Listener 등록
        setListener()
    }

    private fun setObserver(){
        with(viewModel) {
            location.observe(viewLifecycleOwner) {
                // 위치 정보가 저장되어 있지 않다면 현재 사용자의 위치 정보를 저장
                if (it == null) {
                    AlertDialog.Builder(context)
                        .setTitle("위치 정보를 가져오는 데 실패했습니다.")
                        .setMessage("위치 지정 페이지로 이동하시겠습니까?")
                        .setPositiveButton("예") { _, _ ->
                            // 위치 지정 페이지(FindOtherPlaceFragment)로 이동
                            parentFragmentManager
                                .beginTransaction()
                                .replace(R.id.container, FindOtherPlaceFragment.newInstance())
                                .commit()
                        }
                        .setNegativeButton("아니오") { _, _ ->
                            requireActivity().finish()
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                        }
                        .create()
                        .show()
                } else {
                    Log.d("TEST_CODE", "Location is already initialized!")
                }
            }
            // 음악 검색 결과 리스트
            songList.observe(viewLifecycleOwner){
                with(binding) {
                    // 검색 결과 없을 때 검색 결과 없음 안내하는 view 표시
                    if (it.size == 0) {
                        txtEmptyResult.visibility = View.VISIBLE
                    } else {
                        recyclerAdapter = SearchSongAdapter(it)
                        rvResult.adapter = recyclerAdapter
                        rvResult.addItemDecoration(SearchSongDecoration())
                    }
                }
            }
            // progressBar status
            isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            // 검색버튼 클릭 시
            edtSearchSong.setOnKeyListener { _, keyCode, event ->
                if ((event.action== KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // 검색 결과 없음 표시하는 view 가 이미 존재 한다면 view 제거
                    if (txtEmptyResult.visibility == View.VISIBLE) {
                        txtEmptyResult.visibility = View.GONE
                    }
                    // 음악 검색
                    if (edtSearchSong.text!!.isNotEmpty()) {
                        viewModel.getSong(edtSearchSong.text.toString())
                    }
                    hideKeyBoard(requireActivity())
                    true
                } else {
                    false
                }
            }
            // 다음 페이지로 이동
            btnNextPage.setOnClickListener {
                if (edtSearchSong.text!!.isNotEmpty()) {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, WriteStoryFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                } else {
                    it.isClickable = false
                }
            }
        }
    }
}

//class SearchSongFragment : BaseFragment<FragmentSearchSongBinding>(FragmentSearchSongBinding::inflate) {
//    companion object {
//        fun newInstance() = SearchSongFragment()
//    }
//    private lateinit var viewModel: CreateSongViewModel
//    private lateinit var recyclerAdapter: SearchSongAdapter
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
//
//        // ViewModel Observer 등록
//        setObserver()
//        // Listener 등록
//        setListener()
//    }
//
//    private fun setObserver(){
//        with(viewModel) {
//            location.observe(viewLifecycleOwner) {
//                // 위치 정보가 저장되어 있지 않다면 현재 사용자의 위치 정보를 저장
//                if (it == null) {
//                    Log.d("TEST_CODE", "Location is not initialized!")
//                    val resultStatusGetLocation = getCurrentLocation(requireActivity())
//
//                    // 위치 정보를 가져오지 못했을 경우 Dialog 생성
//                    if (!resultStatusGetLocation) {
//                        AlertDialog.Builder(context)
//                            .setTitle("위치 정보를 가져오는 데 실패했습니다.")
//                            .setMessage("위치 지정 페이지로 이동하시겠습니까?")
//                            .setPositiveButton("예") { _, _ ->
//                                // 예 -> 위치 지정 페이지(FindOtherPlaceFragment)로 이동
//                                parentFragmentManager
//                                    .beginTransaction()
//                                    .replace(R.id.container, FindOtherPlaceFragment.newInstance())
//                                    .commit()
//                            }
//                            // 아니오 -> 홈 화면(MainActivity)로 이동
//                            .setNegativeButton("아니오") { _, _ ->
//                                requireActivity().finish()
//                                startActivity(Intent(requireContext(), MainActivity::class.java))
//                            }
//                            .setCancelable(false)   // 화면 밖 터치 시 종료x
//                            .create()
//                            .show()
//                    }
//                } else {
//                    Log.d("TEST_CODE", "Location is already initialized!")
//                }
//            }
//            // 음악 검색 결과 리스트
//            songList.observe(viewLifecycleOwner){
//                with(binding) {
//                    // 검색 결과 없을 때 검색 결과 없음 안내하는 view 표시
//                    if (it.size == 0) {
//                        txtEmptyResult.visibility = View.VISIBLE
//                    } else {
//                        recyclerAdapter = SearchSongAdapter(it)
//                        rvResult.adapter = recyclerAdapter
//                        rvResult.addItemDecoration(SearchSongDecoration())
//                    }
//                }
//            }
//            // progressBar status
//            isLoading.observe(viewLifecycleOwner) {
//                if (it) {
//                    binding.progressBar.visibility = View.VISIBLE
//                } else {
//                    binding.progressBar.visibility = View.GONE
//                }
//            }
//        }
//    }
//
//    private fun setListener() {
//        with(binding) {
//            // 검색버튼 클릭 시
//            edtSearchSong.setOnKeyListener { _, keyCode, event ->
//                if ((event.action== KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    // 검색 결과 없음 표시하는 view 가 이미 존재 한다면 view 제거
//                    if (txtEmptyResult.visibility == View.VISIBLE) {
//                        txtEmptyResult.visibility = View.GONE
//                    }
//                    // 음악 검색
//                    if (edtSearchSong.text!!.isNotEmpty()) {
//                        viewModel.getSong(edtSearchSong.text.toString())
//                    }
//                    hideKeyBoard(requireActivity())
//                    true
//                } else {
//                    false
//                }
//            }
//            // 다음 페이지로 이동
//            btnNextPage.setOnClickListener {
//                if (edtSearchSong.text!!.isNotEmpty()) {
//                    parentFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.container, WriteStoryFragment.newInstance())
//                        .addToBackStack(null)
//                        .commit()
//                } else {
//                    it.isClickable = false
//                }
//            }
//        }
//    }
//}