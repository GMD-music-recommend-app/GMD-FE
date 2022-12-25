/*
* Created by gabriel
* date : 22/11/21
* */
package com.sesac.gmd.presentation.ui.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.sesac.gmd.R
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.api.server.song.get_pinlist.Pin
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentHomeBinding
import com.sesac.gmd.presentation.ui.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.bottomsheet.CreateSongBottomSheetFragment
import com.sesac.gmd.presentation.ui.main.bottomsheet.SongInfoBottomSheetFragment
import com.sesac.gmd.presentation.ui.main.viewmodel.MainViewModel
import kotlinx.coroutines.*

class HomeFragment : Fragment(),
    OnMapReadyCallback,
    OnMarkerClickListener
{
    companion object {
        fun newInstance() = HomeFragment()

        fun newInstance(lat: Double, lng: Double) : Fragment {
            val bundle = Bundle()
            bundle.putDouble("latitude", lat)
            bundle.putDouble("longitude", lng)

            return HomeFragment().also {
                it.arguments = bundle
            }
        }
        const val TAG = "HomeFragment"
    }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var startingPoint: LatLng

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
        else toastMessage("앱을 이용하기 위해서는 위치 정보 접근 권한이 필요합니다")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(Repository()))[MainViewModel::class.java]

        // ???
        // 현재 위치 초기화
        initLocation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener 등록
        setListener()
        // 구글 맵 생성
        initMap()
    }

    // 현재 위치 정보 초기화
    private fun initLocation() {
        if (arguments?.isEmpty != true) {
            // Splash 에서 arguments 로 전달받은 latitude, longitude
            val lat = arguments?.getDouble("latitude")
            val lng = arguments?.getDouble("longitude")
            if (lat != null && lng != null) {
                viewModel.setLocation(requireContext(), lat, lng)
            }
        }
        // ???
//        else {
//            viewModel.getCurrentLocation(requireContext())
//        }
    }

    // Listener 초기화
    private fun setListener() {
        with(binding) {
            btnCreateSong.setOnClickListener {
                val createSong = CreateSongBottomSheetFragment.newInstance()
                createSong.show(childFragmentManager, createSong.tag)
            }
        }
    }

    // Observer set
    private fun setObserver() {
        with(viewModel) {
            pinLists.observe(viewLifecycleOwner) { pins ->
                // 핀 리스트 데이터를 가져오면(= when getPins() called,) 해당 핀 리스트 지도에 표시
                setMarkers(pins)
            }
            location.observe(viewLifecycleOwner) {
                val currentLocation = LatLng(it.latitude, it.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16F))
                getPins(currentLocation)
            }
            // ???
//            location.observe(viewLifecycleOwner) {
//                startingPoint = LatLng(viewModel.location.value!!.latitude, viewModel.location.value!!.longitude)
//            }
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
                LatLng(viewModel.location.value!!.latitude, viewModel.location.value!!.longitude)
            } else {
                LatLng(37.5662952, 126.97794509999994)  // 서울 시청
            }

        with(mMap) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16F))
            setOnMarkerClickListener(this@HomeFragment)
            // 지도에 표시할 음악 핀 가져오기
            getPins(startingPoint)
        }

        // ???
        binding.currentLocationButton.setOnClickListener {
            getMyLocation()
        }

        // Observer 등록
        setObserver()
    }

    // 지도에 표시할 음악 핀 가져오기
    private fun getPins(startingPoint: LatLng) {
        viewModel.getPinList(startingPoint.latitude, startingPoint.longitude)
    }

    // 음악 핀 지도에 표시
    private fun setMarkers(pinList: List<Pin>) {
        // ???
        if (::mMap.isInitialized.not()) return
        mMap.clear()

        pinList.forEach {
            val location = LatLng(it.latitude, it.longitude)
            with(mMap) {
                addMarker(
                    MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_temp))
                        .anchor(0.5f, 0.5f)     // 마커의 하단이 아닌 중앙을 꼭짓점으로 하도록 수정
                )!!.tag = it.pinIdx
            }
        }
    }

    // 핀 클릭 시 곡 상세정보 BottomSheet show
    override fun onMarkerClick(marker: Marker): Boolean {
        val songInfo = SongInfoBottomSheetFragment.newInstance(marker.tag.toString())
        songInfo.show(childFragmentManager, songInfo.tag)
        return true
    }

    override fun onResume() {
        super.onResume()
        getMyLocation()
    }

    // ???
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

    // ???
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

    // ???
    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    // ???
    inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(location: Location) {
            viewModel.setLocation(requireContext(), location.latitude, location.longitude)
            removeLocationListener()
        }
    }
}