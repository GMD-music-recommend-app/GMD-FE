/**
* Created by 조진수
* date : 22/12/08
*/
package com.sesac.gmd.presentation.ui.create_song.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.model.SongList
import com.sesac.gmd.databinding.ViewholderSearchSongResultBinding

/**
 * 음악 검색에서 사용하는 RecyclerView 의 Adapter
 */
class SearchSongAdapter(private val songList : SongList,
                        val onClickItem: (song: Song) -> Unit) : RecyclerView.Adapter<SearchSongAdapter.SearchSongViewHolder>()
{
    inner class SearchSongViewHolder(val binding: ViewholderSearchSongResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSongViewHolder {
        val binding =
            ViewholderSearchSongResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

            btnSelectSong.setOnClickListener {
                onClickItem.invoke(song)
            }
        }
    }

    override fun getItemCount() = songList.songs.size
}