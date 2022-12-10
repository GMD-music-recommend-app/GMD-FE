/*
* Created by gabriel
* date : 22/12/08
* */

package com.sesac.gmd.presentation.ui.create_song.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.gmd.R
import com.sesac.gmd.data.model.Item
import com.sesac.gmd.databinding.ViewholderSearchSongResultBinding

private const val TAG = "SearchSongRecyclerItem"

class SearchSongAdapter(private val items : MutableList<Item>)
    : RecyclerView.Adapter<SearchSongAdapter.SearchSongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSongViewHolder {
        val binding = ViewholderSearchSongResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return SearchSongViewHolder(binding)
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

        // Listener 등록
        setListener(holder, position)
    }

    private fun setListener(holder: SearchSongViewHolder, position: Int) {
        with(holder.binding) {
            selectSong.setOnClickListener {
                Log.d(TAG, "Recycler Item Select Clicked!")
                it.setBackgroundResource(R.drawable.ic_selected)
            }
        }
    }

    override fun getItemCount() = items.size

    inner class SearchSongViewHolder(val binding: ViewholderSearchSongResultBinding) : RecyclerView.ViewHolder(binding.root)
}