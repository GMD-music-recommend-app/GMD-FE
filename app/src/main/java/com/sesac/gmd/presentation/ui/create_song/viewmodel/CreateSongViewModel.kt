package com.sesac.gmd.presentation.ui.create_song.viewmodel

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
import com.sesac.gmd.common.util.TEMP_USER_IDX
import com.sesac.gmd.common.util.Utils.Companion.parseXMLFromMania
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.model.SongList
import com.sesac.gmd.data.repository.Repository
import kotlinx.coroutines.*

/**
 * 음악 추가하기 Sequence 에서 사용하는 ViewModel<br>
 * 멤버는 호출 순서대로 배치
 */
class CreateSongViewModel(private val repository: Repository) : ViewModel() {
    companion object {
        private val TAG = CreateSongViewModel::class.simpleName
    }
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
    fun setOtherLocation(context: Context, userLocation: LatLng) {
        _location.value = geocoding(context, userLocation)
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
                isLoading.value = false
                throw e
            }
        }
    }

    // 음악 검색 결과에서 추천할 음악 선택
    fun addSong(selectedSong: Song) {
        // TODO: 동일 유저 동일 장소 동일 음악 생성 유효성 검사 필요(추후 구현)
        try {
            _selectedSong.value = selectedSong
        } catch (e: Exception) {
            throw e
        }
    }

    // 핀 생성 하기
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
                // 성공적으로 음악 추가가 완료되었을 경우
                if (response.isSuccessful) {
                    // TODO: 예외처리 필요(중복 곡 생성 시)
                    _createSuccess.value = true
                }
                // 서버와 통신은 되었지만 음악 추가에 실패했을 경우
                else {
                    onError("onError: ${response.errorBody()!!.string()}")
                    throw Exception("${response.errorBody()!!}")
                }
                // 서버와 통신 자체에 실패했을 경우
            } catch (e: Exception) {
                throw e
            }
        }
    }
}