/**
* Created by 조진수
* date : 22/11/21
*/
package com.sesac.gmd.presentation.ui.main.fragment.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentChartBinding
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.fragment.chart.adapter.ChartAdapter
import com.sesac.gmd.presentation.ui.main.fragment.chart.adapter.ChartViewHolder
import com.sesac.gmd.presentation.ui.main.viewmodel.ChartViewModel

/**
 * 인기차트 Fragment
 * 사용자 현재 위치 기준으로 공감 수가 많은 10개 음악 표시
 */
class ChartFragment : Fragment() {
    companion object {
        fun newInstance() = ChartFragment()
    }
    private lateinit var binding: FragmentChartBinding
    private lateinit var viewModel: ChartViewModel
    private var chartAdapter: ChartAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChartBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(Repository()))[ChartViewModel::class.java]

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