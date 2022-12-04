/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.presentation.ui.chart

import android.os.Bundle
import android.view.View
import com.sesac.gmd.databinding.FragmentChartBinding
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.presentation.ui.chart.adapter.ChartAdapter
import com.sesac.gmd.presentation.ui.chart.adapter.ChartViewHolder

private const val TAG = "ChartFragment"

class ChartFragment : BaseFragment<FragmentChartBinding>(FragmentChartBinding::inflate) {
    private var chartAdapter: ChartAdapter? = null

    companion object {
        fun newInstance() = ChartFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initializeData()
    }

    private fun initViews() = with(binding) {
        if (chartAdapter == null) {
            chartAdapter = ChartAdapter()
            recyclerView.adapter = chartAdapter
        }
    }

    private fun initializeData() {
        chartAdapter?.submitList(
            (1..10).map {
                ChartViewHolder.Chart(
                    id = it.toLong(),
                    chartNumber = it,
                    imageUrl = "",
                    title ="제목 : $it",
                    singerName = "가수 이름 : $it",
                    empathyCount = it
                )
            }
        )
    }
}