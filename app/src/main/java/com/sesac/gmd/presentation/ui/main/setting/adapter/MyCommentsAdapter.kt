package com.sesac.gmd.presentation.ui.main.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sesac.gmd.databinding.ViewholderMyCommentsItemBinding

class MyCommentsAdapter: ListAdapter<MyCommentsViewHolder.MyComments, MyCommentsViewHolder>(
    object : DiffUtil.ItemCallback<MyCommentsViewHolder.MyComments>() {
        override fun areContentsTheSame(oldItem: MyCommentsViewHolder.MyComments, newItem: MyCommentsViewHolder.MyComments) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: MyCommentsViewHolder.MyComments, newItem: MyCommentsViewHolder.MyComments) =
            oldItem.id == newItem.id
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommentsViewHolder {
        return MyCommentsViewHolder(
            ViewholderMyCommentsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyCommentsViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}