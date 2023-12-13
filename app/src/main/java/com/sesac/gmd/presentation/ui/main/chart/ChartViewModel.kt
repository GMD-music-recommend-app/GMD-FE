package com.sesac.gmd.presentation.ui.main.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesac.gmd.data.model.response.GetChartResult

class ChartViewModel: ViewModel() {
    private var _chartList = MutableLiveData<MutableList<GetChartResult>>()
    val chartList: LiveData<MutableList<GetChartResult>> get() = _chartList


}