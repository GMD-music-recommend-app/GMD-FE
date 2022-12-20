/*
* Created by gabriel
* date : 22/12/06
* */

package com.sesac.gmd.presentation.ui.create_song.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.Utils.Companion.parseXMLFromMania
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.model.SongList
import com.sesac.gmd.data.repository.Repository
import kotlinx.coroutines.*
import java.util.*
/*
* 멤버는 호출 순서대로 배치
* */
private const val TAG = "CreateSongViewModel"

class CreateSongViewModel(private val repository: Repository) : ViewModel() {
    // Location
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> get() = _location

    // ProgressBar
    var isLoading = MutableLiveData<Boolean>()

    // Search result Song list
    val songList = MutableLiveData<SongList>()

    // Selected Song
    private val _selectedSong = MutableLiveData<Song>()
    val selectedSong: LiveData<Song> get() = _selectedSong

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

    // 다른 위치 지정
    fun setLocation() {}

    // 현재 위치 정보 저장
    @SuppressLint("MissingPermission")  // TODO: Splash -> getPermission 구현하면 해당 줄 삭제
    fun getCurrentLocation(context: Context)  {
        // 사용자의 정확한 현재 위치 요청
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { // 비동기로 실행
            if (it == null) {
                // fusedLocationClient 가 현재 위치를 파악하지 못하는 경우
                toastMessage("사용자의 현재 위치를 알 수 없습니다.")
            }
            else {
                // 받아온 현재 위치를 기준으로 geocoding 실행 후 해당 위치 정보를 LiveData 에 저장
                val userLocation = geocoding(context, it.latitude, it.longitude)
                _location.value = userLocation
            }
        }
    }

    // Geocoding(위/경도 -> 행정구역 변환) 함수
    @Suppress("DEPRECATION")
    private fun geocoding(context: Context, lat: Double, lng: Double) : Location {
        val userLocation = Location(lat, lng)
        val geocoder = Geocoder(context, Locale.getDefault())

        // Over Android API 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lng, 1, object: GeocodeListener {
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
                    Log.d(DEFAULT_TAG + TAG, "Geocoder occurred error : $errorMessage!!")
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

    // 음악 검색
    fun getSong(keyword: String) {
        isLoading.postValue(true)
        viewModelScope.launch(exceptionHandler) {
            try {
                val responseBody = repository.getSong(keyword)
                val result = parseXMLFromMania(responseBody.string())
                songList.postValue(result)
                isLoading.value = false
            } catch (e: Exception) {
                // TODO: 예외 처리 필요(인터넷 연결x)
                Log.d(DEFAULT_TAG + TAG, "getSong() error! : ${e.message}")
                toastMessage("예기치 못한 오류가 발생했습니다!")
            }
        }
    }

    // 음악 선택
    fun addSong(selectedSong: Song) {
        // TODO: 동일유저 동일 장소 동일 음악 생성 유효성 검사 필요(추후 구현)
        _selectedSong.value = selectedSong
    }

    // TODO: 임시 작성 코드. OAuth 구현하면 userIdx Preference 에서 가져오고 해당 줄 삭제
    private val userIdx = 5

    // 핀 생성하기
    fun createPin(reason: String, hashtag: String?) {
        viewModelScope.launch(exceptionHandler) {
            try {
                val response = repository.createPin(
                    userIdx,
                    location.value!!,
                    selectedSong.value!!,
                    reason,
                    hashtag
                )
                if (response.isSuccessful) {
                    // TODO: 예외처리 필요(중복 곡 생성 시)
                    Log.d(DEFAULT_TAG+TAG, "---------------------Song Create Success!---------------------")
                    Log.d(DEFAULT_TAG+TAG, "pin Number : ${response.body()!!.result.pinIdx}")
                } else {
                    Log.d(DEFAULT_TAG+TAG, "---------------------Song Create Fail!---------------------")
                    Log.d(DEFAULT_TAG+TAG, "errorMessage : ${response.body()!!.message}")

                    toastMessage("예기치 못한 오류가 발생했습니다.")
                    onError("onError: ${response.errorBody()!!.string()}")
                }
            } catch (e: Exception) {
                toastMessage("예기치 못한 오류가 발생했습니다.")
                Log.d(DEFAULT_TAG + TAG, "createPin() error! : ${e.message}")
            }
        }
    }
}