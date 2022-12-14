/*
* Created by gabriel
* date : 22/12/02
* */

package com.sesac.gmd.presentation.ui.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentOauthSignUpBinding

class OAuthSignUpFragment : Fragment() {
    companion object {
        fun newInstance() = OAuthSignUpFragment()
    }
    private lateinit var binding: FragmentOauthSignUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOauthSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 임시 작성 코드입니다. 추후 수정 필요
        binding.btnSignUp.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, SetNickNameFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}

//class OAuthSignUpFragment : BaseFragment<FragmentOauthSignUpBinding>(FragmentOauthSignUpBinding::inflate) {
//
//    companion object {
//        fun newInstance() = OAuthSignUpFragment()
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // TODO: 임시 작성 코드입니다. 추후 수정 필요
//        binding.btnSignUp.setOnClickListener {
//            val supportFragmentManager = parentFragmentManager
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, SetNickNameFragment.newInstance())
//                .addToBackStack(null)
//                .commit()
//        }
//    }
//}