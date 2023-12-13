package com.sesac.gmd.presentation.ui.main.chart

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sesac.gmd.R
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.model.response.GetChartResult
import com.sesac.gmd.databinding.FragmentChartBinding
import com.sesac.gmd.presentation.base.BaseFragment
import com.sesac.gmd.presentation.ui.main.MainViewModel

class ChartFragment : BaseFragment<FragmentChartBinding>() {
    override val layoutResourceId = R.layout.fragment_chart

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: ChartViewModel by viewModels()

//    private lateinit var chartAdapter: ChartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
    }

    private fun setObserver() {
        activityViewModel.currentLocation.observe(viewLifecycleOwner, ::updateTitleView)
        viewModel.chartList.observe(viewLifecycleOwner, ::updateChartListView)
    }

    private fun updateTitleView(location: Location) {

    }

    private fun updateChartListView(list: MutableList<GetChartResult>) {

    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = TODO("Not yet implemented")
}