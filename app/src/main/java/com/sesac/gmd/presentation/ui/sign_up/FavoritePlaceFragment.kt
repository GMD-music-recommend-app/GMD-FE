/*
* Created by gabriel
* date : 22/12/02
* */

package com.sesac.gmd.presentation.ui.sign_up

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.databinding.FragmentFavoritePlaceBinding
import com.sesac.gmd.presentation.main.MainActivity

private const val TAG = "SetNickNameFragment"

class FavoritePlaceFragment : Fragment() {
    private var _binding: FragmentFavoritePlaceBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = FavoritePlaceFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritePlaceBinding.inflate(inflater, container, false)

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
            btnOk.setOnClickListener {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                toastMessage("가입을 축하드립니다!\n로그인하시기 바랍니다.")
            }
        }
    }
}