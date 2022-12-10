/*
* Created by gabriel
* date : 22/12/08
* */

package com.sesac.gmd.presentation.ui.create_song.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sesac.gmd.R
import com.sesac.gmd.data.model.Item
import com.sesac.gmd.data.model.SongInfo

class SearchSongAdapter(private val items : MutableList<Item>)
    : RecyclerView.Adapter<SearchSongAdapter.RecyclerViewHolder>() {

    inner class RecyclerViewHolder(itemRoot: View) : RecyclerView.ViewHolder(itemRoot) {
        val songTitle: TextView = itemRoot.findViewById(R.id.song_title)
        val songArtist: TextView = itemRoot.findViewById(R.id.song_artist)
        val songAlbum: TextView = itemRoot.findViewById(R.id.song_album)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val tempEntity = items[position]
        with(holder) {
                songTitle.text = tempEntity.songTitle
                songAlbum.text = tempEntity.album.albumTitle
                songArtist.text = tempEntity.artist.joinToString ( "," )
        }
    }

    override fun getItemCount() = items.size
}