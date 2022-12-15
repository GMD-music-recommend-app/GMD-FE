/*
* Created by gabriel
* date : 22/12/08
* */

package com.sesac.gmd.presentation.ui.create_song.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.gmd.data.model.SongList
import com.sesac.gmd.databinding.ViewholderSearchSongResultBinding

class SearchSongAdapter(private val songList : SongList) : RecyclerView.Adapter<SearchSongAdapter.SearchSongViewHolder>() {
    private lateinit var context: Context

    inner class SearchSongViewHolder(val binding: ViewholderSearchSongResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSongViewHolder {
        val binding =
            ViewholderSearchSongResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return SearchSongViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearchSongViewHolder, position: Int) {
        val song = songList.songs[position]
        with(holder.binding) {
            songTitle.text = song.songTitle
            songArtist.text = """${song.artist.joinToString ( "," )} · ${song.album.albumTitle}"""
            Glide.with(holder.itemView.context)
                .load(song.album.albumImage)
                .into(songAlbumImage)

            root.setOnClickListener {
                // TODO: Implement Item Click
            }
        }
    }

    override fun getItemCount() = songList.songs.size
}