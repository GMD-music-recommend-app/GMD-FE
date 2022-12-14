package com.sesac.gmd.presentation.ui.setting.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sesac.gmd.databinding.ViewholderMyAddMusicItemBinding

class AddMusicViewHolder(
    private val binding: ViewholderMyAddMusicItemBinding
): ViewHolder(binding.root) {

    fun bind(addMusic: AddMusic) = with(binding) {
        addMusicLocation.text = addMusic.location
        addMusicTitleTextView.text = addMusic.title
        addMusicSingerTextView.text = addMusic.singerName
        addMusicStory.text = addMusic.story
    }

    data class AddMusic(
        val id: Long,
        val location: String,
        val imageUrl: String,
        val title: String,
        val singerName: String,
        val story: String,
    )

}