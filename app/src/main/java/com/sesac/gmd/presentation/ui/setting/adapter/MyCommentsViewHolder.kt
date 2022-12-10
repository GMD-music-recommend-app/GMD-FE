package com.sesac.gmd.presentation.ui.setting.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sesac.gmd.databinding.ViewholderMyCommentsItemBinding

class MyCommentsViewHolder(
    private val binding: ViewholderMyCommentsItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(myComments: MyComments) = with(binding) {
        myCommentsLocation.text = myComments.location
        myCommentsSingerTextView.text = myComments.singerName
        myCommentsTitleTextView.text = myComments.title
        myCommentsComments.text = myComments.comments
    }

    data class MyComments(
        val id: Long,
        val location: String,
        val singerName: String,
        val title: String,
        val comments: String,
    )
}