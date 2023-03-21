package com.sesac.gmd.presentation.ui.main.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.common.util.*
import com.sesac.gmd.common.util.GeocoderUtil.geocoding
import com.sesac.gmd.data.api.server.chart.GetChartResult
import com.sesac.gmd.data.api.server.song.get_pininfo.GetPinInfoResult
import com.sesac.gmd.data.api.server.song.get_pinlist.Pin
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.repository.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * MainActivity, HomeFragment, ChartFragment 에서 사용하는 ViewModel
 * 멤버는 호출 순서대로 배치
 */
class HomeChartViewModel(private val repository: Repository) : ViewModel() {
    companion object {
        private val TAG = HomeChartViewModel::class.simpleName
    }
    // Location
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> get() = _location

    // 지도에 표시 될 핀 리스트
    private val _pinLists = MutableLiveData<MutableList<Pin>>()
    val pinLists: LiveData<MutableList<Pin>> get() = _pinLists

    // 핀 상세 정보
    private val _pinInfo = MutableLiveData<GetPinInfoResult>()
    val pinInfo: LiveData<GetPinInfoResult> get() = _pinInfo

    // 핀 공감 여부
    private val _isPinLiked = MutableLiveData<String>()
    val isPinLiked: LiveData<String> get() = _isPinLiked

    // 인기차트 결과 음악
    private val _chartList = MutableLiveData<MutableList<GetChartResult>>()
    val chartList: LiveData<MutableList<GetChartResult>> get() = _chartList

    // ProgressBar
    var isLoading = MutableLiveData<Boolean>()

    // REST 처리 중 Coroutine 내에서 예외가 발생했을 때
    private val errorMessage = MutableLiveData<String>()
    private val exceptionHandler = CoroutineExceptionHandler { _, thrownException ->
        onError("Coroutine 내 예외 : ${thrownException.localizedMessage}")
        Log.e(DEFAULT_TAG + TAG, thrownException.message.toString())
    }

    // Coroutine 내 REST 처리 중 에러 발생 시 호출됨
    private fun onError(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }

    /**
     * MainActivity 에서 위치 정보가 Update 되어 Location Callback 을 호출 했을 때
     * 유저의 현재 위치 정보를 Geocoding 하여 LiveData<Location> 에 저장
     */
    fun saveCurrentLocation(context: Context, userLocation: LatLng) {
        viewModelScope.launch(exceptionHandler) {
            _location.value = geocoding(context, userLocation)
        }
    }

    /**
     * 반경 내 핀 리스트 가져오기
     * @param lat 사용자 현 위치(위도)
     * @param lng 사용자 현 위치(경도)
     * @param radius 핀 리스트 가져 올 반경
     */
    fun getPinList(lat: Double, lng: Double, radius: Int = PIN_SEARCH_RADIUS) {
        viewModelScope.launch(exceptionHandler) {
            try {
                val response = repository.getPinList(lat, lng, radius)
                if (response.isSuccessful) {
                    _pinLists.value = response.body()!!.result
                } else {
                    onError("onError: ${response.errorBody()!!.string()}")
                    throw Exception("${response.errorBody()!!}")
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    // 핀 정보 가져오기(핀 클릭 시 bottomSheet 화면)
    fun getPinInfo(pinIdx: Int) = viewModelScope.launch(exceptionHandler) {
        try {
            val response = repository.getSongInfo(pinIdx, TEMP_USER_IDX)
            if (response.isSuccessful) {
                _pinInfo.value = response.body()!!.result
            } else {
                onError("onError: ${response.errorBody()!!.string()}")
                throw Exception("${response.errorBody()!!}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    // TODO: 수정 필요 -> 받는 결과를 String(문장)에서 간단한 결과로 바꿔야 함  
    // 핀 공감하기
    fun insertLikePin(pinIdx: Int) = viewModelScope.launch(exceptionHandler) {
        try {
            val response = repository.insertLikePin(pinIdx, TEMP_USER_IDX)
            if (response.isSuccessful) {
                _isPinLiked.value = response.body()!!.result
            } else {
                onError("onError: ${response.errorBody()!!.string()}")
                throw Exception("${response.errorBody()!!}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
    
    // TODO: 임시 작성 코드. 추후 삭제 필요
    fun clearPinLikeValue() {
        _isPinLiked.value = ""
    }

    // 인기 차트 갱신
    fun getChartData() = viewModelScope.launch(exceptionHandler) {
        try {
            val city = location.value!!.city.toString()
            val response = repository.getChartList(city)

            if (response.isSuccessful) {
                _chartList.value = response.body()!!.result
            } else {
                onError("onError: ${response.errorBody()!!.string()}")
                throw Exception("${response.errorBody()!!}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}