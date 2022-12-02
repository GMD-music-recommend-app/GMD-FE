package com.sesac.gmd.presentation.ui.chart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sesac.gmd.databinding.ViewholderChartItemBinding

class ChartAdapter: ListAdapter<ChartViewHolder.Chart, ChartViewHolder>(
    object : DiffUtil.ItemCallback<ChartViewHolder.Chart>() {
        override fun areContentsTheSame(oldItem: ChartViewHolder.Chart, newItem: ChartViewHolder.Chart) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: ChartViewHolder.Chart, newItem: ChartViewHolder.Chart) =
            oldItem.id == newItem.id
    }
) {

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
        holder.bind(currentList[position])
    }

}