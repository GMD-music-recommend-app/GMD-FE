/*
* Created by gabriel
* date : 22/12/06
* */

package com.sesac.gmd.presentation.ui.create_song.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.GeoUtil.geocoding
import com.sesac.gmd.common.util.TEMP_USER_IDX
import com.sesac.gmd.common.util.Utils.Companion.parseXMLFromMania
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.model.SongList
import com.sesac.gmd.data.repository.Repository
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeoutException
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

    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> get() = _createSuccess

    // Coroutine 내 REST 처리 중 에러 발생 시 호출됨
    private fun onError(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }

    // 다른 위치 지정
    fun setLocation(context: Context, lat: Double, lng: Double) {
        val getLocation = geocoding(context, lat, lng)
        _location.value = getLocation
    }

    // 현재 위치 정보 저장
    @SuppressLint("MissingPermission")
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

    // 음악 검색
    fun getSong(keyword: String) {
        isLoading.postValue(true)
        viewModelScope.launch(exceptionHandler) {
            try {
                val responseBody = repository.getSong(keyword)
                val result = parseXMLFromMania(responseBody.string())
                songList.postValue(result)
                isLoading.value = false
            } catch (e : TimeoutException) {
                Log.d(DEFAULT_TAG + TAG, "getSong() error! : ${e.message}")
                toastMessage("연결이 고르지 않습니다.\n 다시 시도해주시기 바랍니다.")
                isLoading.value = false
                throw TimeoutException("$e")
            } catch (e: Exception) {
                // TODO: 예외 처리 필요(인터넷 연결x)
                Log.d(DEFAULT_TAG + TAG, "getSong() error! : ${e.message}")
                toastMessage("예기치 못한 오류가 발생했습니다!")
                isLoading.value = false
                throw Exception("$e")
            }
        }
    }

    // 음악 선택
    fun addSong(selectedSong: Song) {
        // TODO: 동일유저 동일 장소 동일 음악 생성 유효성 검사 필요(추후 구현)
        _selectedSong.value = selectedSong
    }

    // 핀 생성하기
    fun createPin(reason: String, hashtag: String?) {
        viewModelScope.launch(exceptionHandler) {
            try {
                val response = repository.createPin(
                    TEMP_USER_IDX,
                    location.value!!,
                    selectedSong.value!!,
                    reason,
                    hashtag
                )
                if (response.isSuccessful) {
                    // TODO: 예외처리 필요(중복 곡 생성 시)
                    Log.d(DEFAULT_TAG+TAG, "---------------------Song Create Success!---------------------")
                    Log.d(DEFAULT_TAG+TAG, "pin Number : ${response.body()!!.result.pinIdx}")

                    _createSuccess.value = true
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