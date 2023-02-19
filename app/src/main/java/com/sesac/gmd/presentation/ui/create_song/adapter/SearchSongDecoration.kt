package com.sesac.gmd.presentation.ui.create_song.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sesac.gmd.common.util.RECYCLER_ITEM_SEARCH_MUSIC_OFFSET

/**
 * 음악 검색에서 사용하는 RecyclerView 의 ItemDecoration<br>
 * RecyclerView Item 의 간격 설정
 */
class SearchSongDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val offset = RECYCLER_ITEM_SEARCH_MUSIC_OFFSET
        outRect.bottom = offset
    }
}