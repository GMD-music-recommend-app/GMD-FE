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
import com.sesac.gmd.databinding.FragmentSetNickNameBinding

class SetNickNameFragment : Fragment() {
    companion object {
        fun newInstance() = SetNickNameFragment()
    }
    private lateinit var binding: FragmentSetNickNameBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetNickNameBinding.inflate(inflater, container, false)

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
            btnNextPage.setOnClickListener {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, FavoritePlaceFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}

//class SetNickNameFragment : BaseFragment<FragmentSetNickNameBinding>(FragmentSetNickNameBinding::inflate) {
//
//    companion object {
//        fun newInstance() = SetNickNameFragment()
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
//            btnNextPage.setOnClickListener {
//                val supportFragmentManager = parentFragmentManager
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, FavoritePlaceFragment.newInstance())
//                    .addToBackStack(null)
//                    .commit()
//            }
//        }
//    }
//}