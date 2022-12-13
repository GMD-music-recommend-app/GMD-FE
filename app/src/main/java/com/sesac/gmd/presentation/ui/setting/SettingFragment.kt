/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.presentation.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentSettingBinding
import com.sesac.gmd.presentation.main.MainActivity

class SettingFragment : Fragment() {
    companion object {
        fun newInstance() = SettingFragment()

        const val TAG = "SettingFragment"
    }
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity : MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "SettingFragment : onCreate() called!")
        activity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "SettingFragment : onCreateView() called!")
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        interestLocation.setOnClickListener {

        }
        recommendMusic.setOnClickListener {
            activity.supportFragmentManager.beginTransaction()
                .add(R.id.tabContent, MyAddMusicFragment(), MyAddMusicFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}