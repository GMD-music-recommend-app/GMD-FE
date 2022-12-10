/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.fragment

import android.os.Bundle
import android.view.View
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.data.repository.CreateSongRepository
import com.sesac.gmd.databinding.FragmentSearchSongBinding
import com.sesac.gmd.presentation.ui.create_song.adapter.SearchSongAdapter
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel

private const val TAG = "SearchSongFragment"

class SearchSongFragment : BaseFragment<FragmentSearchSongBinding>(FragmentSearchSongBinding::inflate) {
    private val viewModel = CreateSongViewModel(CreateSongRepository())

    companion object {
        fun newInstance() = SearchSongFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel Observer 등록
        setObserver()
        // Listener 등록
        setListener()
    }

    private fun setObserver(){
        viewModel.songList.observe(requireActivity()){
            with(binding) {
                run {
                    val recyclerAdapter = SearchSongAdapter(it)
                    rvResult.adapter = recyclerAdapter
                }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            btnSearchSong.setOnClickListener {
                viewModel.getSong(edtSearchSong.text.toString())
            }
        }
    }
}