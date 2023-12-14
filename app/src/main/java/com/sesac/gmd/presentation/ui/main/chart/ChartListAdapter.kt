package com.sesac.gmd.presentation.ui.main.chart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sesac.gmd.data.model.response.GetChartResult
import com.sesac.gmd.databinding.ViewholderChartItemBinding

class ChartListAdapter() :
    ListAdapter<GetChartResult, ChartListAdapter.ChartItemViewHolder>(ChartDiffCallback()) {

    class ChartItemViewHolder(val binding: ViewholderChartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GetChartResult) {
            with(binding) {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartItemViewHolder {
        return ChartItemViewHolder(
            ViewholderChartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChartItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private class ChartDiffCallback : DiffUtil.ItemCallback<GetChartResult>() {
        override fun areItemsTheSame(oldItem: GetChartResult, newItem: GetChartResult): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: GetChartResult, newItem: GetChartResult): Boolean {
            TODO("Not yet implemented")
        }
    }
}


//class ChartAdapter(chartList : MutableList<GetChartResult>) : RecyclerView.Adapter<ChartAdapter.ChartViewHolder>() {
//    // 무작위 순서로 받아온 인기차트 데이터를 'songRank'를 기준으로 정렬
//    private val sortedChartData = chartList.sortedBy { it.songRank }
//
//    inner class ChartViewHolder(val binding: ViewholderChartItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        @SuppressLint("SetTextI18n")
//        fun bind(chartItem: GetChartResult) = with(binding) {
//            // TODO: 추후 수정 필요(서버 API 오류)
//            /*txtChartMusicRank.text = chartItem.songRank.toString()
//            txtChartMusicTitle.text = chartItem.songTitle
//            txtChartMusicArtist.text = chartItem.artist
//            txtChartLikedCount.text = chartItem.likeCount.toString()
//            Glide.with(itemView.context)
//                .load(chartItem.albumImage)
//                .placeholder(R.drawable.ic_load)
//                .error(R.drawable.ic_sample_image)
//                .into(imgChartMusicAlbum)*/
//
//            //txtChartMusicRank.text = chartItem.songRank.toString() + "위"
//            txtChartMusicTitle.text = chartItem.city
//            txtChartMusicArtist.text = chartItem.state
//            txtChartLikedCount.text = "${chartItem.likeCount}회 공감"
//            Glide.with(itemView.context)
//                .load(chartItem.artist)
//                .placeholder(R.drawable.ic_load)
//                .error(R.drawable.ic_sample_image)
//                .into(imgChartMusicAlbum)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
//        return ChartViewHolder(
//            ViewholderChartItemBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
//        holder.bind(sortedChartData[position])
//    }
//
//    override fun getItemCount() = sortedChartData.size
//}