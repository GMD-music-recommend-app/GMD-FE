/*
* Created by gabriel
* date : 22/11/26
* */

package com.sesac.gmd.presentation.ui.create_song.activity

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.sesac.gmd.R
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.databinding.ActivityCreateSongBinding
import com.sesac.gmd.presentation.ui.create_song.fragment.FindOtherPlaceFragment
import com.sesac.gmd.presentation.ui.create_song.fragment.SearchSongFragment

class CreateSongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateSongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSongBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val getIntent = intent.getStringExtra("GO_TO_PAGE")
        if (savedInstanceState == null) {
            with(supportFragmentManager.beginTransaction()) {
                // 노래 추천하기 버튼 클릭 시 첫 화면
                when(getIntent) {
                    "CreateSongHere" -> { // 여기에서 추천하기
                        add(R.id.container, SearchSongFragment.newInstance())
                        commit()
                    }
                    "SetOtherPlace" -> { // 다른 곳에서 추천하기
                        add(R.id.container, FindOtherPlaceFragment.newInstance())
                        commit()
                    }
                    else -> {
                        toastMessage("예기치 못한 오류가 발생했습니다.")
                    }
                }
            }
        }

        // toolbar 뒤로가기 버튼
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

    // 외부 터치 시 키보드 내리기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev?.x!!.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}