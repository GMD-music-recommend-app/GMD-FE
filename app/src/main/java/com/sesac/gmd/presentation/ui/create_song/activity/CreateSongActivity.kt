/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.sesac.gmd.R
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.CreateSongRepository
import com.sesac.gmd.databinding.ActivityCreateSongBinding
import com.sesac.gmd.presentation.ui.create_song.fragment.FindOtherPlaceFragment
import com.sesac.gmd.presentation.ui.create_song.fragment.SearchSongFragment

private const val TAG = "CreateSongActivity"

class CreateSongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateSongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSongBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        val getIntent = intent.getStringExtra("CREATE_PLACE")

        if (savedInstanceState == null) {
            with(supportFragmentManager.beginTransaction()) {
                // 노래 추천하기 버튼 클릭 시 첫 화면
                when(getIntent) {
                    "here" -> { // 여기에서 추천하기
                        add(R.id.container, SearchSongFragment.newInstance())
//                        addToBackStack(null)
                        commit()
                    }
                    "other" -> { // 다른 곳에서 추천하기
                        add(R.id.container, FindOtherPlaceFragment.newInstance())
//                        addToBackStack(null)
                        commit()
                    }
                    else -> {
                        toastMessage("예기치 못한 오류가 발생했습니다.")
                    }
                }
            }
        }

        // TODO: 코드 수정 필요
        // toolbar 뒤로가기 버튼 구현
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // TODO: 코드 수정 필요
    // toolbar 뒤로가기 버튼 클릭 시 동작 구현
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}