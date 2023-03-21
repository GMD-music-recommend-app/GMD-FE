package com.sesac.gmd.presentation.ui.main.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.sesac.gmd.R
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.common.util.*
import com.sesac.gmd.common.util.Utils.Companion.displayToastExceptions
import com.sesac.gmd.data.api.server.song.get_pinlist.Pin
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentHomeBinding
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.bottomsheet.CreateSongBottomSheetFragment
import com.sesac.gmd.presentation.ui.main.bottomsheet.SongInfoBottomSheetFragment
import com.sesac.gmd.presentation.ui.main.viewmodel.HomeChartViewModel

class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    OnMapReadyCallback,
    OnMarkerClickListener
{
    companion object {
        // private val TAG = HomeFragment::class.simpleName
        fun newInstance() = HomeFragment()
    }
    // activity 의 LifeCycle 을 이용하기 위해 activityViewModels 사용
    private val viewModel: HomeChartViewModel by activityViewModels { ViewModelFactory(Repository()) }
    private lateinit var mMap: GoogleMap
    private lateinit var startingPoint: LatLng  // Google Map 지도 중심 점

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 구글 맵 View 초기화
        initMap()

        // Listener 등록
        setListener()
    }

    // 구글 맵 초기화
    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // 지도가 준비되었을 때 호출되는 콜백 메소드
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 지도 중심점 설정
        startingPoint =
            if (viewModel.location.value != null) {
                // viewModel 에 현재 위치 값이 있는 경우
                if  (viewModel.location.value!!.latitude != 0.0 && viewModel.location.value!!.longitude != 0.0) {
                    LatLng(viewModel.location.value!!.latitude, viewModel.location.value!!.longitude)
                }
                // viewModel 에 현재 위치 값이 없는 경우(위/경도 값이 0.0인 경우)
                else {
                    LatLng(SEOUL_CITY_LATITUDE, SEOUL_CITY_LONGITUDE)  // 서울 시청
                }
            }
            // viewModel 에 현재 위치 값이 존재하지 않는 경우(location == null 인 경우)
            else {
                LatLng(SEOUL_CITY_LATITUDE, SEOUL_CITY_LONGITUDE)  // 서울 시청
            }

        // 지도 중심점, 줌 레벨 설정, 화면에 표시할 음악 핀 데이터 가져오기
        with(mMap) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, DEFAULT_ZOOM_LEVEL))
            setOnMarkerClickListener(this@HomeFragment)

            // 지도에 표시할 음악 핀 데이터 서버에서 가져오기
            getPins(startingPoint)

            // 내 위치 Tracking Button
            isMyLocationEnabled = true
        }

        // Observer 등록
        setObserver()
    }

    // Listener 초기화
    private fun setListener() = with(binding) {
        // 음악 추천하기 버튼
        btnCreateSong.setOnClickListener {
            val createSong = CreateSongBottomSheetFragment.newInstance()
            createSong.show(childFragmentManager, createSong.tag)
        }
    }

    // Observer set
    private fun setObserver() = with(viewModel) {
        // 유저의 현재 위치 정보
        location.observe(viewLifecycleOwner) {
            val currentLocation = LatLng(it.latitude, it.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM_LEVEL))
            getPins(currentLocation)
        }

        // 지도에 표시되는 음악 핀 데이터
        pinLists.observe(viewLifecycleOwner) { pins ->
            // 핀 리스트 데이터를 가져오면(getPins() 호출 시) 해당 핀 리스트 지도에 표시
            setMarkers(pins)
        }
    }

    // 지도에 표시할 음악 핀 데이터 서버에서 가져오기
    private fun getPins(startingPoint: LatLng) {
        try {
            viewModel.getPinList(startingPoint.latitude, startingPoint.longitude)
        } catch (e : Exception) {
            displayToastExceptions(e) // 오류 내용에 따라 ToastMessage 출력
        }
    }

    // 서버에서 가져온 음악 핀 데이터 지도에 표시
    private fun setMarkers(pinList: List<Pin>) {
        if (::mMap.isInitialized.not()) return      // 구글 맵이 초기화되지 않았으면 메서드 종료
        mMap.clear()                                // 기존에 생성된 마커를 지우고 새로 그림

        pinList.forEach {
            val location = LatLng(it.latitude, it.longitude)
            with(mMap) {
                addMarker(
                    MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
                        .anchor(DEFAULT_PIN_CENTER_POINTER, DEFAULT_PIN_CENTER_POINTER)     // 마커의 하단이 아닌 중앙을 꼭짓점으로 하도록 수정
                )!!.tag = it.pinIdx
            }
        }
    }

    // 핀 클릭 시 곡 상세 정보 Bottom Sheet Dialog 표시
    override fun onMarkerClick(marker: Marker): Boolean {
        val songInfo = SongInfoBottomSheetFragment.newInstance(marker.tag.toString())
        songInfo.show(childFragmentManager, songInfo.tag)
        return true
    }
}