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
import com.sesac.gmd.databinding.FragmentSetNickNameBinding

private const val TAG = "SetNickNameFragment"

class SetNickNameFragment : BaseFragment<FragmentSetNickNameBinding>(FragmentSetNickNameBinding::inflate) {

    companion object {
        fun newInstance() = SetNickNameFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Listener 초기화
        setListener()
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