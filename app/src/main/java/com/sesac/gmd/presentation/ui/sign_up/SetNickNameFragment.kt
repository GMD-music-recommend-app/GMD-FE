/*
* Created by gabriel
* date : 22/12/02
* */

package com.sesac.gmd.presentation.ui.sign_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentSetNickNameBinding

private const val TAG = "SetNickNameFragment"

class SetNickNameFragment : Fragment() {
    private var _binding: FragmentSetNickNameBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SetNickNameFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSetNickNameBinding.inflate(inflater, container, false)

        // Listener 초기화
        setListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListener() {
        with(binding) {
            btnNextPage.setOnClickListener {
                val supportFragmentManager = parentFragmentManager
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, FavoritePlaceFragment.newInstance())
                    .commit()
            }
        }
    }
}