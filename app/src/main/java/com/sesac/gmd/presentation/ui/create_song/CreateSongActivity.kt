/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.sesac.gmd.R
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.databinding.ActivityCreateSongBinding
import com.sesac.gmd.presentation.ui.create_song.create.SearchSongFragment
import com.sesac.gmd.presentation.ui.create_song.other_place.FindOtherPlaceFragment

private const val TAG = "CreateSongActivity"

class CreateSongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateSongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSongBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // TODO: 임시 작성 코드. 추후 수정 필요
        // 노래 추천하기 -> 여기에서 추천하기 or 다른 곳에서 추천하기
        val getIntent = intent.getStringExtra("page")

        if (savedInstanceState == null) {
            with(supportFragmentManager.beginTransaction()) {
                when(getIntent) {
                    "here" -> {
                        add(R.id.container, SearchSongFragment.newInstance())
                        commit()
                    }
                    "other" -> {
                        add(R.id.container, FindOtherPlaceFragment.newInstance())
                        commit()
                    }
                    else -> toastMessage("오류뜸")
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