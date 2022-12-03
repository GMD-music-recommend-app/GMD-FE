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
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.databinding.FragmentOauthSignUpBinding

private const val TAG = "OAuthSignUpFragment"

class OAuthSignUpFragment : BaseFragment<FragmentOauthSignUpBinding>(FragmentOauthSignUpBinding::inflate) {

    companion object {
        fun newInstance() = OAuthSignUpFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 임시 작성 코드입니다. 추후 수정 필요
        binding.btnSignUp.setOnClickListener {
            val supportFragmentManager = parentFragmentManager
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SetNickNameFragment.newInstance())
                .commit()
        }
    }
}