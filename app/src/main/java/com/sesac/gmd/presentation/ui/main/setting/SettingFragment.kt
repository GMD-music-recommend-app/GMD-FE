/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.presentation.ui.main.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    companion object {
        fun newInstance() = SettingFragment()
    }
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        interestLocation.setOnClickListener {}

        recommendMusic.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .add(R.id.tabContent, MyAddMusicFragment(), MyAddMusicFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }
}