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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.common.util.Utils.Companion.parseXMLFromMania
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.model.Item
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.data.model.SongInfo
import com.sesac.gmd.data.repository.CreateSongRepository
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "CreateSongViewModel"

class CreateSongViewModel(private val repository: CreateSongRepository) : ViewModel() {
    // ProgressBar
    var isLoading = MutableLiveData<Boolean>()

    // Location
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> get() = _location

    // Search result Song list
    val songList = MutableLiveData<MutableList<Item>>()

    // Selected Song
    private val _selectedSong = MutableLiveData<SongInfo>()
    val selectedSong: LiveData<SongInfo> get() = _selectedSong

    // 사연
    private val _comment = MutableLiveData<String>()
    private val comment: LiveData<String>
            get() = _comment

    // REST 처리 중 Coroutine 내에서 예외가 발생했을 때
    private val errorMessage = MutableLiveData<String>()
    private val exceptionHandler = CoroutineExceptionHandler { _, thrownException ->
        onError("Coroutine 내 예외 : ${thrownException.localizedMessage}")
    }

    // 다른 위치 지정
    fun setLocation() {}

    // TODO: 위치 정보가 null 일 경우 현재 위치 정보를 mutableLiveData<Location> 에 넣는다
    // 현재 위치 정보 저장
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context) {
        // 사용자의 현재 위치를 정확하게 받아옴
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            // 받아온 현재 위치를 기준으로 geocoding 실행
            val userLocation = geocoding(context, it.latitude, it.longitude)
            _location.postValue(userLocation)
        }
    }

    // Geocoding(위/경도 -> 행정구역 변환) 함수
    private fun geocoding(context: Context, lat: Double, lng: Double) : Location {
        val userLocation = Location(lat, lng)
        val geocoder = Geocoder(context, Locale.getDefault())

        // Over Android API 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lng, 1, object: GeocodeListener {
                // 제대로 Geocoding 성공했을 경우
                override fun onGeocode(addr: MutableList<Address>) {
                    userLocation.state = if (addr[0].adminArea == null) addr[0].subAdminArea else addr[0].adminArea
                    userLocation.city = if (addr[0].locality == null) addr[0].subLocality else addr[0].locality
                    userLocation.street = if (addr[0].thoroughfare == null) addr[0].subThoroughfare else addr[0].thoroughfare
                }
                // Geocoding 실패했을 경우
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    toastMessage("예기치 못한 문제가 발생했습니다.")
                    Log.d("Geocoder occurred error", errorMessage!!)
                }
            })
        } else {
            // Under Android API 33
            val addr = geocoder.getFromLocation(lat, lng, 1)
            try {
                if (addr != null) {
                    if (addr.isNotEmpty()) {
                        userLocation.state = if (addr[0].adminArea == null) addr[0].subAdminArea else addr[0].adminArea
                        userLocation.city = if (addr[0].locality == null) addr[0].subLocality else addr[0].locality
                        userLocation.street = if (addr[0].thoroughfare == null) addr[0].subThoroughfare else addr[0].thoroughfare
                    }
                }
            } catch (e: Exception) {
                toastMessage("예기치 못한 문제가 발생했습니다.")
                e.printStackTrace()
            }
        }
        return userLocation
    }

    // 음악 검색
    fun getSong(keyword: String) {
        isLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
            val responseBody = repository.getSong(keyword)
            Log.d(TAG, responseBody.toString())
            val result = parseXMLFromMania(responseBody.string())
            songList.postValue(result.items)
            withContext(Dispatchers.Main) {
                isLoading.value = false
            }
        }
    }

    // 음악 선택
    fun selectSong() {
        // TODO: 1곡 이상 선택했는지 유효성 검사 필요
        // TODO: 동일유저 동일 장소 동일 음악 생성 유효성 검사 필요(추후 구현)
        // TODO: 노래 추가 여부에 따라 버튼 형태 변경되도록 코드 수정 필요
    }

    // 핀 생성하기
    fun createPin() {
//        Log.d("SearchSongFragment", "${it.latitude}")
//        Log.d("SearchSongFragment", "${it.longitude}")
//        Log.d("SearchSongFragment", "Location lat : ${_location.value?.latitude}")
//        Log.d("SearchSongFragment", "Location lng : ${_location.value?.longitude}")
//        Log.d("SearchSongFragment", "Location city : ${_location.value?.city}")
    }

    // Coroutine 내 REST 처리 중 에러 발생 시 호출됨
    private fun onError(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }
}