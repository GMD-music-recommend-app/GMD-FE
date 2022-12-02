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
import com.sesac.gmd.databinding.FragmentOauthSignUpBinding

private const val TAG = "OAuthSignUpFragment"

class OAuthSignUpFragment : Fragment() {
    private var _binding: FragmentOauthSignUpBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = OAuthSignUpFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOauthSignUpBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener {
            val supportFragmentManager = parentFragmentManager
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SetNickNameFragment.newInstance())
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}