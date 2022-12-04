/*
* Created by gabriel
* date : 22/12/02
* */

package com.sesac.gmd.presentation.ui.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.databinding.FragmentFavoritePlaceBinding
import com.sesac.gmd.presentation.main.MainActivity

private const val TAG = "SetNickNameFragment"

class FavoritePlaceFragment : BaseFragment<FragmentFavoritePlaceBinding>(FragmentFavoritePlaceBinding::inflate) {

    companion object {
        fun newInstance() = FavoritePlaceFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Listener 초기화
        setListener()
    }

    private fun setListener() {
        with(binding) {
            btnOk.setOnClickListener {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                toastMessage("가입을 축하드립니다!\n로그인하시기 바랍니다.")
            }
        }
    }
}