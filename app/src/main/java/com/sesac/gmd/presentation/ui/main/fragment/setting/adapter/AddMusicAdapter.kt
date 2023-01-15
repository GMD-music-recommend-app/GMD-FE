package com.sesac.gmd.presentation.ui.main.fragment.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sesac.gmd.databinding.ViewholderMyAddMusicItemBinding

class AddMusicAdapter: ListAdapter<AddMusicViewHolder.AddMusic, AddMusicViewHolder>(
    object : DiffUtil.ItemCallback<AddMusicViewHolder.AddMusic>() {
        override fun areContentsTheSame(oldItem: AddMusicViewHolder.AddMusic, newItem: AddMusicViewHolder.AddMusic) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: AddMusicViewHolder.AddMusic, newItem: AddMusicViewHolder.AddMusic) =
            oldItem.id == newItem.id
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddMusicViewHolder {
        return AddMusicViewHolder(
            ViewholderMyAddMusicItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddMusicViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}