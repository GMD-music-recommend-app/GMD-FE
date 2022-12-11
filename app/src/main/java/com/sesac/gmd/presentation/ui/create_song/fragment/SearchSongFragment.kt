/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.fragment

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.constraintlayout.motion.widget.Debug.getLocation
import com.sesac.gmd.R
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.common.util.Utils.Companion.hideKeyBoard
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.CreateSongRepository
import com.sesac.gmd.databinding.FragmentSearchSongBinding
import com.sesac.gmd.presentation.ui.create_song.adapter.SearchSongAdapter
import com.sesac.gmd.presentation.ui.create_song.adapter.SearchSongDecoration
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel

private const val TAG = "SearchSongFragment"

class SearchSongFragment : BaseFragment<FragmentSearchSongBinding>(FragmentSearchSongBinding::inflate) {
    private val viewModel = CreateSongViewModel(CreateSongRepository())
    private lateinit var recyclerAdapter: SearchSongAdapter

    companion object {
        fun newInstance() = SearchSongFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.location.observe(requireActivity()) {
            Log.d(TAG, "${it.latitude}")
            Log.d(TAG, "${it.longitude}")
            if (it == null) {
                viewModel.getCurrentLocation(requireContext())
            } else {
                Log.d(TAG, "Location is already initialized!")
            }
        }

        binding.rvResult.addItemDecoration(SearchSongDecoration())

        // ViewModel Observer 등록
        setObserver()
        // Listener 등록
        setListener()
    }

    private fun setObserver(){
        with(viewModel) {
            songList.observe(requireActivity()){
                with(binding) {
                    // 검색 결과 없을 때
                    if (it.size == 0) {
                        txtEmptyResult.visibility = View.VISIBLE
                    } else {
                        recyclerAdapter = SearchSongAdapter(it)
                        rvResult.adapter = recyclerAdapter
                    }
                }
            }
            isLoading.observe(requireActivity()) {
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
                    if (txtEmptyResult.visibility == View.VISIBLE) {
                        txtEmptyResult.visibility = View.GONE
                    }
                    //
                    viewModel.getCurrentLocation(requireContext())
                    //
                    viewModel.getSong(edtSearchSong.text.toString())
                    hideKeyBoard(requireActivity())
                    true
                } else {
                    false
                }
            }
            btnNextPage.setOnClickListener {
                val supportMapFragment = parentFragmentManager
                supportMapFragment
                    .beginTransaction()
                    .replace(R.id.container, WriteStoryFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}