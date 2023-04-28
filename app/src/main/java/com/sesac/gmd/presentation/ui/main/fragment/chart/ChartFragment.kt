package com.sesac.gmd.presentation.ui.main.fragment.chart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.sesac.gmd.R
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.common.util.Utils.Companion.displayToastExceptions
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.model.remote.GetChartResult
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.repository.remote.RemoteRepository
import com.sesac.gmd.databinding.FragmentChartBinding
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.adapter.ChartAdapter
import com.sesac.gmd.presentation.ui.main.viewmodel.HomeChartViewModel

/**
 * 인기차트 Fragment
 * 사용자 현재 위치(city = 시, 군, 구) 기준으로 공감 수(songRank)가 많은 10개 음악 표시
 */
class ChartFragment : BaseFragment<FragmentChartBinding>(FragmentChartBinding::inflate) {
    companion object {
        //private val TAG = ChartFragment::class.java.simpleName
        fun newInstance() = ChartFragment()
    }
    private val viewModel: HomeChartViewModel by activityViewModels { ViewModelFactory(RemoteRepository()) }
    private lateinit var chartAdapter: ChartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // 서버에서 인기차트 데이터 가져오기
        getChartList()
        // Observer 등록
        setObserver()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    // 인기차트 갱신 함수
    private fun getChartList() {
        try {
            viewModel.getChartData()
        } catch (e: Exception) {
            displayToastExceptions(e)
        }
    }

    // Observer set
    private fun setObserver() {
        with(viewModel) {
            // 유저의 현재 위치 Observing -> Fragment Title 설정
            location.observe(viewLifecycleOwner) { initLocationTitle(it) }

            // 생성된 지뭐듣 인기차트 데이터 Observing
            chartList.observe(viewLifecycleOwner) { initChart(it) }
        }
    }

    // Fragment 상단에 현재 위치 표시
    @SuppressLint("SetTextI18n")
    private fun initLocationTitle(currentLocation: Location) = with(binding) {
        // LiveData 에 유저 위치 정보가 저장 되어 있지 않은 경우(위/경도 값이 존재하지 않는 경우)
        if (currentLocation.latitude == 0.0) {
            txtChartLocation.visibility = View.GONE
            txtChartTitle.visibility = View.GONE
            toastMessage(getString(R.string.error_not_found_user_location))
        } else {
            txtChartLocation.text = "${currentLocation.state.toString()} ${currentLocation.city.toString()}"
            txtChartLocation.visibility = View.VISIBLE
            txtChartTitle.visibility = View.VISIBLE
        }
    }

    private fun initChart(chartData: MutableList<GetChartResult>) {
        // 해당 지역에서 생성된 인기 차트가 없는 경우
        if (chartData.size == 0) {
            binding.imgChartNoResult.visibility = View.VISIBLE
        } else {
            chartAdapter = ChartAdapter(chartData)
            binding.rvChartResult.adapter = chartAdapter

            binding.rvChartResult.visibility = View.VISIBLE
            binding.imgChartNoResult.visibility = View.GONE
        }
    }
}