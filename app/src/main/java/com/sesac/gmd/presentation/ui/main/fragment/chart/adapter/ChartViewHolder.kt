package com.sesac.gmd.presentation.ui.main.fragment.chart.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sesac.gmd.databinding.ViewholderChartItemBinding

class ChartViewHolder(
    private val binding: ViewholderChartItemBinding
): ViewHolder(binding.root) {

    fun bind(chart: Chart) = with(binding) {
        chartNumberTextView.text = chart.chartNumber.toString()
        chartTitleTextView.text = chart.title
    }

    data class Chart(
        val id: Long,
        val chartNumber: Int,
        val imageUrl: String,
        val title: String,
        val singerName: String,
        val empathyCount: Int
    )
}