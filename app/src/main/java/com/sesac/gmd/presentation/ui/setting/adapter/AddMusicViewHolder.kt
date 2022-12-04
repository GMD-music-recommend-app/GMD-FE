package com.sesac.gmd.presentation.ui.setting.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sesac.gmd.databinding.ViewholderMyAddMusicItemBinding

class AddMusicViewHolder(
    private val binding: ViewholderMyAddMusicItemBinding
): ViewHolder(binding.root) {

    fun bind(addMuisc: AddMusic) = with(binding) {
        addMusicLocation.text = addMuisc.location
        addMusicTitleTextView.text = addMuisc.title
        addMusicSingerTextView.text = addMuisc.singerName
        addMusicStory.text = addMuisc.story
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