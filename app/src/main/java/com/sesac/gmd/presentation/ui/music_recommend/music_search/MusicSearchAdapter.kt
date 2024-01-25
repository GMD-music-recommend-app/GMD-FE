package com.sesac.gmd.presentation.ui.music_recommend.music_search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.gmd.R
import com.sesac.gmd.common.isEqualMusicItem
import com.sesac.gmd.data.model.Music
import com.sesac.gmd.databinding.ViewholderSearchMusicResultBinding

class MusicSearchAdapter(
    private val onItemClick: ((music: Music) -> Unit),
) : ListAdapter<Music, MusicSearchAdapter.MusicItemViewHolder>(MusicListDiffCallback()) {

    class MusicItemViewHolder(
        val binding: ViewholderSearchMusicResultBinding,
        private val onItemClick: (music: Music) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(music: Music) {
            setMusicAlbumArtImage(music)
            setTextViewContents(music)
            setListener(music)
        }

        private fun setMusicAlbumArtImage(music: Music) {
            Glide.with(itemView.context)
                .load(music.album.albumImage)
                .placeholder(R.drawable.ic_music_basic)
                .error(R.drawable.ic_music_basic)
                .into(binding.imgResultAlbumArt)
        }

        @SuppressLint("SetTextI18n")
        private fun setTextViewContents(music: Music) {
            binding.txtResultMusicTitle.text = music.musicTitle
            binding.txtSearchMusicArtist.text =
                """${music.artist.joinToString(",")} · ${music.album.albumTitle}"""
        }

        private fun setListener(music: Music) {
            binding.btnSelectMusic.setOnClickListener { onItemClick.invoke(music) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicItemViewHolder {
        return MusicItemViewHolder(
            ViewholderSearchMusicResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
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
            return oldItem.isEqualMusicItem(newItem)
        }

        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.musicIdx == newItem.musicIdx
        }
    }
}