package com.sesac.gmd.presentation.ui.main.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.R
import com.sesac.gmd.application.GMDApplication
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.GeocoderUtil.geocoding
import com.sesac.gmd.common.util.GetLocationUtil
import com.sesac.gmd.common.util.PIN_SEARCH_RADIUS
import com.sesac.gmd.common.util.TEMP_USER_IDX
import com.sesac.gmd.data.api.server.chart.GetChartResult
import com.sesac.gmd.data.api.server.song.get_pininfo.GetPinInfoResult
import com.sesac.gmd.data.api.server.song.get_pinlist.Pin
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.repository.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * MainActivity 에서 사용하는 ViewModel
 * HomeFragment, ChartFragment 에서 호출
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
    private val _isPinLiked = MutableLiveData<Boolean>()
    val isPinLiked: LiveData<Boolean> get() = _isPinLiked

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

    // Geocoding 된 위치 정보를 LiveData 에 저장
    fun saveGeoLocation(context: Context, userLocation: LatLng) {
        viewModelScope.launch(exceptionHandler) {
            _location.value = geocoding(context, userLocation)
        }
    }

    // 유저의 현재 위치 값(위도, 경도)를 LiveData<Location> 에 저장
    suspend fun setCurrentUserLocation(context: Context) {
        viewModelScope.launch(exceptionHandler) {
            // 유저의 정확한 현재 위치 요청
            val userLocation = GetLocationUtil.getCurrentLocation(context)

            // 유저의 현재 위치 값을 받아오지 못한 경우 View 로 Exception 전달
            if (userLocation.latitude == 0.0 && userLocation.longitude == 0.0) {
                throw Exception(GMDApplication.getAppInstance().resources.getString(R.string.error_not_found_user_location))
            }
            // 받아온 현재 위치를 기준으로 geocoding 실행 후 해당 위치 정보를 LiveData 에 저장
            else {
                _location.value = geocoding(context, userLocation)
            }
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
    fun getPinInfo(pinIdx: Int) {
        viewModelScope.launch(exceptionHandler) {
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
    }

    // 핀 공감하기 TODO : 미완성
//    fun insertLikePin(pinIdx: Int) {
//        viewModelScope.launch(exceptionHandler) {
//            try {
//                val response = repository.insertLikePin(pinIdx, TEMP_USER_IDX)
//                if (response.isSuccessful) {
////                   _isPinLiked.value = response.body().result
//                } else {
//                    onError("onError: ${response.errorBody()!!.string()}")
//                    throw Exception("${response.errorBody()!!}")
//                }
//            } catch (e: Exception) {
//                throw e
//            }
//        }
//    }


    // 인기차트 갱신 함수
//    fun fetchChartData(context: Context, latLng: LatLng) = viewModelScope.launch(exceptionHandler) {
//        try {
//            isLoading.value = true
//            // 받아온 현재 위치를 기준으로 geocoding 실행 후 해당 위치 정보를 LiveData 에 저장
//            val userLocation = geocoding(context, latLng.latitude, latLng.longitude)
//            val city = userLocation.city ?: let {
//                onError("위치를 불러올 수 없습니다.")
//                return@launch
//            }
//            val chartResponse = repository.getChartList(city)
//            if (chartResponse.body()?.isSuccess == true) {
//                delay(2_000L)
//                val result = chartResponse.body()?.result ?: emptyList()
//                val chartList = result.map {
//                    ChartAdapter.ChartViewHolder.Chart(
//                        id = it.pinIdx.toLong(),
//                        chartNumber = it.songRank,
//                        imageUrl = it.albumImage,
//                        title = it.songTitle,
//                        singerName = it.artist,
//                        empathyCount = it.likeCount
//                    )
//                }
//                _chartListLiveData.value = chartList
//            }
//            isLoading.value = false
//        } catch (e: Exception) {
//            e.printStackTrace()
//            toastMessage(GMDApplication.getAppInstance().resources.getString(R.string.unexpected_error))
//        }
//    }
}