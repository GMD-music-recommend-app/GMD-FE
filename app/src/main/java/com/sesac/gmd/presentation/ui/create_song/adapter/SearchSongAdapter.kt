/*
* Created by gabriel
* date : 22/12/08
* */

package com.sesac.gmd.presentation.ui.create_song.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.gmd.data.model.Item
import com.sesac.gmd.databinding.ViewholderSearchSongResultBinding

private const val TAG = "SearchSongRecyclerItem"

class SearchSongAdapter(private val items : MutableList<Item>)
    : RecyclerView.Adapter<SearchSongAdapter.SearchSongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSongViewHolder {
        val binding = ViewholderSearchSongResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchSongViewHolder(binding).also { searchSongViewHolder ->
            // Listener 등록
            setListener(searchSongViewHolder)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearchSongViewHolder, position: Int) {
        val song = items[position]
        with(holder.binding) {
            songTitle.text = song.songTitle
            songArtist.text = """${song.artist.joinToString ( "," )} · ${song.album.albumTitle}"""
            Glide.with(holder.itemView.context)
                .load(song.album.albumImageURL)
                .into(songAlbumImage)
        }
    }

    // Listener 초기화 함수
    private fun setListener(holder: SearchSongViewHolder) {
        with(holder.binding) {
            selectSong.setOnClickListener{
                // TODO: 곡 중복 선택 안되도록 구현 필요
            }
        }
    }

    override fun getItemCount() = items.size

    inner class SearchSongViewHolder(val binding: ViewholderSearchSongResultBinding) : RecyclerView.ViewHolder(binding.root)
}