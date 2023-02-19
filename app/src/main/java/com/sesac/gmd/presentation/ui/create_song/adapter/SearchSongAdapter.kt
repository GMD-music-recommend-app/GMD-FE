package com.sesac.gmd.presentation.ui.create_song.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.gmd.R
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.model.SongList
import com.sesac.gmd.databinding.ViewholderSearchSongResultBinding

/**
 * 음악 검색에서 사용하는 RecyclerView 의 Adapter
 */
class SearchSongAdapter(private val songList : SongList, val onClickItem: (song: Song) -> Unit)
    : RecyclerView.Adapter<SearchSongAdapter.SearchSongViewHolder>()
{
    inner class SearchSongViewHolder(val binding: ViewholderSearchSongResultBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(song: Song) = with(binding) {
            txtSearchMusicTitle.text = song.songTitle
            txtSearchMusicArtist.text = """${song.artist.joinToString ( "," )} · ${song.album.albumTitle}"""
            Glide.with(itemView.context)
                .load(song.album.albumImage)
                .placeholder(R.drawable.ic_load)
                .error(R.drawable.ic_sample_image)
                .into(imgSearchMusicAlbumImage)

            btnSelectSong.setOnClickListener {
                onClickItem.invoke(song)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSongViewHolder {

        return SearchSongViewHolder(
            ViewholderSearchSongResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearchSongViewHolder, position: Int) {
        holder.bind(songList.songs[position])
    }

    override fun getItemCount() = songList.songs.size
}