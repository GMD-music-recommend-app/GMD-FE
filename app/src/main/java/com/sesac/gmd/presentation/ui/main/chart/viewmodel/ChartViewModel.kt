package com.sesac.gmd.presentation.ui.main.chart.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.GeoUtil
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.presentation.ui.main.chart.adapter.ChartViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChartViewModel(private val repository: Repository) : ViewModel() {
    companion object {
        private const val TAG = "CreateSongViewModel"
    }
    // ProgressBar
    val isLoading = MutableLiveData<Boolean>()

    // REST 처리 중 Coroutine 내에서 예외가 발생했을 때
    private val errorMessage = MutableLiveData<String>()

    private val _chartListLiveData = MutableLiveData<List<ChartViewHolder.Chart>>()
    val chartListLiveData: LiveData<List<ChartViewHolder.Chart>> = _chartListLiveData

    // Coroutine 내 REST 처리 중 에러 발생 시 호출됨
    private fun onError(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, thrownException ->
        onError("Coroutine 내 예외 : ${thrownException.localizedMessage}")
        Log.e(DEFAULT_TAG + TAG, thrownException.message.toString())
    }

    fun fetchData(context: Context, latLng: LatLng) = viewModelScope.launch(exceptionHandler) {
        try {
            isLoading.value = true
            // 받아온 현재 위치를 기준으로 geocoding 실행 후 해당 위치 정보를 LiveData 에 저장
            val userLocation = GeoUtil.geocoding(context, latLng.latitude, latLng.longitude)
            val city = userLocation.city ?: let {
                onError("위치를 불러올 수 없습니다.")
                return@launch
            }
            val chartResponse = repository.getChartList(city)
            if (chartResponse.body()?.isSuccess == true) {
                delay(2_000L)
                val result = chartResponse.body()?.result ?: emptyList()
                val chartList = result.map {
                    ChartViewHolder.Chart(
                        id = it.pinIdx.toLong(),
                        chartNumber = it.songRank,
                        imageUrl = it.albumImage,
                        title = it.songTitle,
                        singerName = it.artist,
                        empathyCount = it.likeCount
                    )
                }
                _chartListLiveData.value = chartList
            }
            isLoading.value = false
        } catch (e: Exception) {
            e.printStackTrace()
            toastMessage("예기치 못한 오류가 발생했습니다!")
        }
    }
}