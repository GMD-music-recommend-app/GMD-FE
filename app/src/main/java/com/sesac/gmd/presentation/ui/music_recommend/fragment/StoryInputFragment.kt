package com.sesac.gmd.presentation.ui.music_recommend.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentStoryInputBinding
import com.sesac.gmd.presentation.base.BaseFragment

class StoryInputFragment : BaseFragment<FragmentStoryInputBinding>() {
    override val layoutResourceId = R.layout.fragment_story_input

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = TODO("Not yet implemented")
}