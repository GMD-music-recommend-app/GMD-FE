/**
* Created by 조진수
* date : 22/11/26
*/
package com.sesac.gmd.presentation.ui.create_song.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.sesac.gmd.R
import com.sesac.gmd.common.util.CREATE_MUSIC_HERE
import com.sesac.gmd.common.util.GO_TO_PAGE
import com.sesac.gmd.common.util.SET_OTHER_PLACE
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

        // 보여줄 Fragment set
        setFirstFragment(savedInstanceState)

        // toolbar 설정
        setToolbar()
    }

    // 보여줄 Fragment setting
    private fun setFirstFragment(savedInstanceState: Bundle?) {
        val goToPageName = intent.getStringExtra(GO_TO_PAGE)
        if (savedInstanceState == null) {
            with(supportFragmentManager.beginTransaction()) {
                // 노래 추천하기 버튼 클릭 시 첫 화면
                when(goToPageName) {
                    CREATE_MUSIC_HERE -> { // 여기에서 추천하기
                        add(R.id.container, SearchSongFragment.newInstance())
                        commit()
                    }
                    SET_OTHER_PLACE -> { // 다른 곳에서 추천하기
                        add(R.id.container, FindOtherPlaceFragment.newInstance())
                        commit()
                    }
                    else -> {
                        toastMessage(getString(R.string.unexpected_error))
                    }
                }
            }
        }
    }

    private fun setToolbar() {
        binding.toolbar.run {
            setSupportActionBar(this)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            this.title = ""
            this.subtitle = ""
            this.setNavigationIcon(R.drawable.ic_back)
        }
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