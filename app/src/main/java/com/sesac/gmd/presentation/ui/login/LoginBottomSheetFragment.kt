/*
* Created by gabriel
* date : 22/11/24
* */

package com.sesac.gmd.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sesac.gmd.databinding.FragmentLoginBottomSheetBinding
import com.sesac.gmd.presentation.ui.sign_up.SignUpActivity

class LoginBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = LoginBottomSheetFragment()
    }
    private lateinit var binding: FragmentLoginBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 임시 생성 코드입니다. 추후 삭제 필요.
        with(binding) {
            btnLogin.setOnClickListener {
                // 액티비티 이동
                startActivity(Intent(requireContext(), SignUpActivity::class.java))
            }
            btnClose.setOnClickListener {
                // 바텀 시트 닫기
                dismiss()
            }
        }
    }
}