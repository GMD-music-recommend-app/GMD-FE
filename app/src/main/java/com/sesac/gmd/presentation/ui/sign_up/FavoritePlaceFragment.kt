/*
* Created by gabriel
* date : 22/12/02
* */

package com.sesac.gmd.presentation.ui.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.databinding.FragmentFavoritePlaceBinding
import com.sesac.gmd.presentation.ui.main.activity.MainActivity

class FavoritePlaceFragment : Fragment() {
    companion object {
        fun newInstance() = FavoritePlaceFragment()
    }
    private lateinit var binding: FragmentFavoritePlaceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoritePlaceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener 등록
        setListener()
    }

    // Listener 초기화 함수
    private fun setListener() {
        with(binding) {
            btnOk.setOnClickListener {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                toastMessage("가입을 축하드립니다!\n로그인하시기 바랍니다.")
            }
        }
    }
}

//class FavoritePlaceFragment : BaseFragment<FragmentFavoritePlaceBinding>(FragmentFavoritePlaceBinding::inflate) {
//
//    companion object {
//        fun newInstance() = FavoritePlaceFragment()
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // Listener 초기화
//        setListener()
//    }
//
//    private fun setListener() {
//        with(binding) {
//            btnOk.setOnClickListener {
//                startActivity(Intent(requireContext(), MainActivity::class.java))
//                toastMessage("가입을 축하드립니다!\n로그인하시기 바랍니다.")
//            }
//        }
//    }
//}