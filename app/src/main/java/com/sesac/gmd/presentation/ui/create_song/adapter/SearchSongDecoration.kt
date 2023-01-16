/**
* Created by 조진수
* date : 22/12/08
*/
package com.sesac.gmd.presentation.ui.create_song.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 음악 검색에서 사용하는 RecyclerView 의 ItemDecoration
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
        val offset = 20
        outRect.bottom = offset
    }
}