/*
* Created by gabriel
* date : 22/11/24
* */

package com.sesac.gmd.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sesac.gmd.R
import com.sesac.gmd.presentation.ui.sign_up.SignUpActivity

private const val TAG = "LoginBottomSheet"

class LoginBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = LoginBottomSheetFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_login_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 임시 생성 코드입니다. 추후 삭제 필요.
        val btnLogin : Button = view.findViewById(R.id.btn_login)
        btnLogin.setOnClickListener {
            // 액티비티 이동
            startActivity(Intent(requireContext(), SignUpActivity::class.java))

        }
        val btnBack : ImageButton = view.findViewById(R.id.btn_close)
        btnBack.setOnClickListener {
            // 바텀 시트 닫기
            Log.d(TAG, "Bottom Sheet Close!")
            dismiss()
        }
    }
}