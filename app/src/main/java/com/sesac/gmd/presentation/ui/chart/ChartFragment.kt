/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.presentation.ui.chart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentChartBinding
import com.sesac.gmd.presentation.main.MainActivity
import com.sesac.gmd.presentation.ui.chart.adapter.ChartAdapter
import com.sesac.gmd.presentation.ui.chart.adapter.ChartViewHolder

private const val TAG = "ChartFragment"

class ChartFragment : Fragment() {
    private lateinit var binding: FragmentChartBinding
    private lateinit var activity : MainActivity

    private var chartAdapter: ChartAdapter? = null

    companion object {
        fun newInstance() = ChartFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ChartFragment : onCreate() called!")
        activity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "ChartFragment : onCreateView() called!")
        binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
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