/*
* Created by gabriel
* date : 22/12/02
* */

package com.sesac.gmd.presentation.ui.sign_up

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sesac.gmd.R
import com.sesac.gmd.databinding.ActivitySignUpBinding

private const val TAG = "SignUpActivity"

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // 최초 실행 시 Fragment 초기화
        if (savedInstanceState == null) {
            with(supportFragmentManager.beginTransaction()){
                add(R.id.container, OAuthSignUpFragment.newInstance())
                addToBackStack(null)
                commit()
            }
        }

        // TODO: 코드 수정 필요
        // toolbar 뒤로가기 버튼 구현
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}