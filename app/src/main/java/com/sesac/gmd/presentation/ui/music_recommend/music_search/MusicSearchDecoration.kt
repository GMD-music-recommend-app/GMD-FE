package com.sesac.gmd.presentation.ui.music_recommend.music_search

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class MusicSearchDecoration(private val context: Context): RecyclerView.ItemDecoration() {
    companion object {
        // Recycler View Item Decoration Offsets
        private const val RECYCLER_ITEM_SEARCH_MUSIC_OFFSET = 20f
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val density = context.resources.displayMetrics.density
        outRect.bottom = (RECYCLER_ITEM_SEARCH_MUSIC_OFFSET * density).roundToInt()
    }
}