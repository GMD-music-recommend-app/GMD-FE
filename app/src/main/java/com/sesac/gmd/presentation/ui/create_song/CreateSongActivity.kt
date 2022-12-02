/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.sesac.gmd.R
import com.sesac.gmd.databinding.ActivityCreateSongBinding
import com.sesac.gmd.presentation.ui.create_song.other_place.FindOtherPlaceFragment

private const val TAG = "CreateSongActivity"

class CreateSongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateSongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSongBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

//        // 최초 실행 시 Fragment 초기화
//        if (savedInstanceState == null) {
//            with(supportFragmentManager.beginTransaction()) {
//                add(R.id.container, SearchSongFragment.newInstance())
//                commit()
//            }
//        }
        // 최초 실행 시 Fragment 초기화
        if (savedInstanceState == null) {
            with(supportFragmentManager.beginTransaction()) {
                add(R.id.container, FindOtherPlaceFragment.newInstance())
                commit()
            }
        }

        // TODO: 코드 수정 필요
        // toolbar 뒤로가기 버튼 구현
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

//    // TODO: 임시 작성 코드. 추후 수정 필요
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        toastMessage(requestCode.toString())
//        if (requestCode == 123) {
//            with(supportFragmentManager.beginTransaction()) {
//                add(R.id.container, FindOtherPlaceFragment.newInstance())
//                commit()
//            }
//        } else {
//            with(supportFragmentManager.beginTransaction()) {
//                add(R.id.container, SearchSongFragment.newInstance())
//                commit()
//            }
//        }
//    }

    // TODO: 코드 수정 필요
    // toolbar 뒤로가기 버튼 클릭 시 동작 구현
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}