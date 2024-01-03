package com.sesac.gmd.presentation.ui.music_recommend.music_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sesac.gmd.data.model.Music
import com.sesac.gmd.databinding.ViewholderMusicItemBinding

class MusicSearchAdapter(
    private val onClickItem: ((music: Music) -> Unit),
) : ListAdapter<Music, MusicSearchAdapter.MusicItemViewHolder>(MusicListDiffCallback()) {

    class MusicItemViewHolder(
        val binding: ViewholderMusicItemBinding,
        private val onClickItem: (music: Music) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(music: Music) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicItemViewHolder {
        return MusicItemViewHolder(
            ViewholderMusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }

    override fun onBindViewHolder(holder: MusicItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private class MusicListDiffCallback : DiffUtil.ItemCallback<Music>() {
        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            TODO("Not yet implemented")
        }

        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            TODO("Not yet implemented")
        }
    }
}