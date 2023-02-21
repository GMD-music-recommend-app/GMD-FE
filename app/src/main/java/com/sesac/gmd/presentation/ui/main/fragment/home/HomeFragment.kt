package com.sesac.gmd.presentation.ui.main.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
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
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.api.server.song.get_pinlist.Pin
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentHomeBinding
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.bottomsheet.CreateSongBottomSheetFragment
import com.sesac.gmd.presentation.ui.main.bottomsheet.SongInfoBottomSheetFragment
import com.sesac.gmd.presentation.ui.main.viewmodel.HomeChartViewModel
import kotlinx.coroutines.*

class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    OnMapReadyCallback,
    OnMarkerClickListener
{
    companion object {
       // private val TAG = HomeFragment::class.simpleName
        fun newInstance() = HomeFragment()

        fun newInstance(lat: Double, lng: Double) : Fragment {
            val bundle = Bundle()
            bundle.putDouble(LATITUDE, lat)
            bundle.putDouble(LONGITUDE, lng)

            return HomeFragment().also {
                it.arguments = bundle
            }
        }
    }
    // activity 의 LifeCycle 을 이용하기 위해 activityViewModels 사용
    private val viewModel: HomeChartViewModel by activityViewModels { ViewModelFactory(Repository()) }
    private lateinit var mMap: GoogleMap
    private lateinit var startingPoint: LatLng  // Google Map 지도 중심 점

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // 사용자 위치 정보 초기화
        CoroutineScope(Dispatchers.IO).launch {
            initUserLocation()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener 등록
        setListener()
        // 구글 맵 생성
        initMap()
    }

    /**
     * SplashActivity 에서 받아온 현재 위치 정보를 바탕으로<br>
     * 유저의 현재 위치 초기화<br>
     * Splash 에서 Intent 로 넘어온 위/경도 값이 있다면 해당 값을
     * ViewModel 의 LiveData<Location> 에 넣는다.
     */
    private suspend fun initUserLocation() {
        /**
         * Splash -> MainActivity -> HomeFragment 로 전달 된 유저의 위치 정보(위/경도)
         * Bundle 에 값을 넣어 HomeFragment 를 생성하는 경우는 Splash -> MainActivity -> HomeFragment 의 경우 밖에 없음
         * 나머지 모두 HomeFragment 생성 시 arguments 가 null 인 상태
         */
        val latitude = arguments?.getDouble(LATITUDE, 0.0)
        val longitude = arguments?.getDouble(LONGITUDE, 0.0)

        // arguments 에 값이 있다면 해당 위치 정보를 LiveData<Location> 에 저장
        if ((latitude != 0.0 && longitude != 0.0) && (latitude != null && longitude != null)) {
            viewModel.saveGeoLocation(requireContext(), LatLng(latitude, longitude))
        }
        // 값이 없다면 유저의 FusedLocation 을 통해 위치 정보를 새로 가져옴
        else {
            try {
                viewModel.setCurrentUserLocation(requireContext())
            } catch (e: Exception) {
                //displayToastExceptions(e)
                Log.e(DEFAULT_TAG+"here", e.message.toString())
            }
        }
    }

    // Listener 초기화
    private fun setListener() = with(binding) {
        // 음악 추천하기 버튼
        btnCreateSong.setOnClickListener {
            val createSong = CreateSongBottomSheetFragment.newInstance()
            createSong.show(childFragmentManager, createSong.tag)
        }
    }

    // 구글 맵 초기화
    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

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
        }

        // 현재 위치 Refresh
        binding.currentLocationButton.setOnClickListener {
            getMyLocation()
        }

        // Observer 등록
        setObserver()
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

    override fun onResume() {
        super.onResume()
        getMyLocation()
    }

    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var allPermissionsGranted = true
        permissions.entries.forEach { (_, isGranted) ->
            if (isGranted.not()) {
                allPermissionsGranted = false
                return@forEach
            }
        }
        if (allPermissionsGranted) getMyLocation()
        else toastMessage(getString(R.string.alert_need_location_permission))
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager = getSystemService(requireContext(), LocationManager::class.java) as LocationManager
        }
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnable) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            } else {
                setMyLocationListener()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime: Long = 1500
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            val currentLocation = LatLng(location.latitude, location.longitude)

            if (isAdded) {
                viewModel.saveGeoLocation(requireContext(), currentLocation)
                removeLocationListener()
            }
        }
    }
}