package com.sesac.gmd.presentation.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sesac.gmd.R
import com.sesac.gmd.common.CREATE_MUSIC_HERE
import com.sesac.gmd.common.GO_TO_PAGE
import com.sesac.gmd.common.SET_OTHER_PLACE
import com.sesac.gmd.databinding.FragmentCreateSongBottomSheetBinding
import com.sesac.gmd.presentation.base.BaseBottomSheetFragment
import com.sesac.gmd.presentation.ui.create_song.CreateSongActivity

class CreateSongBottomSheetFragment :
    BaseBottomSheetFragment<FragmentCreateSongBottomSheetBinding>() {
    override val layoutResourceId = R.layout.fragment_create_song_bottom_sheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    private fun setListener() = with(binding) {
        btnClose.setOnClickListener { dismiss() }
        btnCreateHere.setOnClickListener { onCreateButtonClick(CREATE_MUSIC_HERE) }
        btnCreateOtherPlace.setOnClickListener { onCreateButtonClick(SET_OTHER_PLACE) }
    }

    private fun onCreateButtonClick(page: String) {
        val nextPage = Intent(requireContext(), CreateSongActivity::class.java)
        nextPage.putExtra(GO_TO_PAGE, page)
        startActivity(nextPage)
        dismiss()
    }
}