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

    /**
     * HomeFragment 에서 유저가 선택한 음악 추천 위치(현재 위치, 다른 위치)에 따라<br>
     * CreateSongActivity 에서 다른 위치 선택 화면과 음악 검색 화면 중<br>
     * 처음으로 보여 줄 Fragment 세팅
     */
    private fun setFirstFragment(savedInstanceState: Bundle?) {
        // 유저가 현재 위치에서 추가하기를 선택했는지 다른 위치에 추가하기를 선택했는지 확인
        val goToPageName = intent.getStringExtra(GO_TO_PAGE)

        if (savedInstanceState == null) {
            with(supportFragmentManager.beginTransaction()) {
                when(goToPageName) {
                    // 현재 위치에서 추천하기 버튼 클릭 시
                    CREATE_MUSIC_HERE -> {
                        add(R.id.container, SearchSongFragment.newInstance())
                        commit()
                    }
                    // 다른 곳에서 추천하기
                    SET_OTHER_PLACE -> {
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

    // 해당 Activity 의 Toolbar 설정
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

    // toolbar 뒤로가기 버튼 클릭 시 동작 구현 TODO: 코드 수정 필요
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