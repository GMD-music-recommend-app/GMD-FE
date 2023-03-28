package com.sesac.gmd.presentation.ui.main.fragment.chart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.gmd.R
import com.sesac.gmd.data.model.remote.GetChartResult
import com.sesac.gmd.databinding.ViewholderChartItemBinding

class ChartAdapter(chartList : MutableList<GetChartResult>) : RecyclerView.Adapter<ChartAdapter.ChartViewHolder>() {
    // 무작위 순서로 받아온 인기차트 데이터를 'songRank'를 기준으로 정렬
    private val sortedChartData = chartList.sortedBy { it.songRank }

    inner class ChartViewHolder(val binding: ViewholderChartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(chartItem: GetChartResult) = with(binding) {
            // TODO: 추후 수정 필요(서버 API 오류)
            /*txtChartMusicRank.text = chartItem.songRank.toString()
            txtChartMusicTitle.text = chartItem.songTitle
            txtChartMusicArtist.text = chartItem.artist
            txtChartLikedCount.text = chartItem.likeCount.toString()
            Glide.with(itemView.context)
                .load(chartItem.albumImage)
                .placeholder(R.drawable.ic_load)
                .error(R.drawable.ic_sample_image)
                .into(imgChartMusicAlbum)*/

            //txtChartMusicRank.text = chartItem.songRank.toString() + "위"
            txtChartMusicTitle.text = chartItem.city
            txtChartMusicArtist.text = chartItem.state
            txtChartLikedCount.text = "${chartItem.likeCount}회 공감"
            Glide.with(itemView.context)
                .load(chartItem.artist)
                .placeholder(R.drawable.ic_load)
                .error(R.drawable.ic_sample_image)
                .into(imgChartMusicAlbum)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        return ChartViewHolder(
            ViewholderChartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        holder.bind(sortedChartData[position])
    }

    override fun getItemCount() = sortedChartData.size
}