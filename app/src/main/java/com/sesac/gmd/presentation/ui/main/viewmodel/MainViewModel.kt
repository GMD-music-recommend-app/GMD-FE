package com.sesac.gmd.presentation.ui.main.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.TEMP_USER_IDX
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.api.server.song.get_pininfo.GetPinInfoResult
import com.sesac.gmd.data.api.server.song.get_pinlist.Pin
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.repository.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "MainViewModel"

class MainViewModel(private val repository: Repository) : ViewModel() {
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
    fun setLocation(context: Context, lat: Double, lng: Double) {
        _location.value = geocoding(context, lat, lng)
    }

    // Geocoding(위/경도 -> 행정구역 변환) 함수
    @Suppress("DEPRECATION")
    fun geocoding(context: Context, lat: Double, lng: Double) : Location {
        val userLocation = Location(lat, lng)
        val geocoder = Geocoder(context, Locale.getDefault())

        // Over Android API 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lng, 1, object: Geocoder.GeocodeListener {
                // 제대로 Geocoding 성공했을 경우
                override fun onGeocode(address: MutableList<Address>) {
                    userLocation.state = if (address[0].adminArea == null) address[0].subAdminArea else address[0].adminArea
                    userLocation.city = if (address[0].locality == null) address[0].subLocality else address[0].locality
                    userLocation.street = if (address[0].thoroughfare == null) address[0].subThoroughfare else address[0].thoroughfare
                }
                // Geocoding 실패했을 경우
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    toastMessage("예기치 못한 문제가 발생했습니다.")
                    Log.d(DEFAULT_TAG+ TAG, "Geocoder occurred error : $errorMessage!!")
                }
            })
        } else {
            // Under Android API 33
            val address = geocoder.getFromLocation(lat, lng, 1)
            try {
                if (address != null) {
                    if (address.isNotEmpty()) {
                        userLocation.state = if (address[0].adminArea == null) address[0].subAdminArea else address[0].adminArea
                        userLocation.city = if (address[0].locality == null) address[0].subLocality else address[0].locality
                        userLocation.street = if (address[0].thoroughfare == null) address[0].subThoroughfare else address[0].thoroughfare
                    }
                }
            } catch (e: Exception) {
                toastMessage("예기치 못한 문제가 발생했습니다.")
                Log.d(DEFAULT_TAG + TAG, "Geocoder occurred error : $errorMessage!!")
            }
        }
        return userLocation
    }

    // 반경 내 핀 리스트 가져오기
    fun getPinList(lat: Double, lng: Double, radius: Int = 5_000) {
        viewModelScope.launch(exceptionHandler) {
            try {
                val response = repository.getPinList(lat, lng, radius)
                if (response.isSuccessful) {
                    _pinLists.value = response.body()!!.result
                } else {
                    toastMessage("음악 핀 데이터를 가져오지 못했습니다.")
                    onError("onError: ${response.errorBody()!!.string()}")
                }
            } catch (e: Exception) {
                toastMessage("예기치 못한 오류가 발생했습니다.")
                Log.d(DEFAULT_TAG + TAG, "getPinList() error! : ${e.message}")
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
                    toastMessage("음악 핀 데이터를 가져오지 못했습니다.")
                    onError("onError: ${response.errorBody()!!.string()}")
                }
            } catch (e: Exception) {
                toastMessage("예기치 못한 오류가 발생했습니다.")
                Log.d(DEFAULT_TAG + TAG, "getPinInfo() error! : ${e.message}")
            }
        }
    }

    // 핀 공감하기 TODO : 미완성
    fun insertLikePin(pinIdx: Int) {
        viewModelScope.launch(exceptionHandler) {
            try {
                val response = repository.insertLikePin(pinIdx, TEMP_USER_IDX)
                if (response.isSuccessful) {
//                   _isPinLiked.value = response.body().result
                } else {
                    toastMessage("예기치 못한 오류가 발생했습니다.")
                    onError("onError: ${response.errorBody()!!.string()}")
                }
            } catch (e: Exception) {
                toastMessage("예기치 못한 오류가 발생했습니다.")
                Log.d(DEFAULT_TAG + TAG, "insertLikePin() error! : ${e.message}")
            }
        }
    }
}