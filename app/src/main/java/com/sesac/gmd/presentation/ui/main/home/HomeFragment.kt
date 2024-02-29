package com.sesac.gmd.presentation.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.R
import com.sesac.gmd.common.DEFAULT_MAP_ZOOM_LEVEL
import com.sesac.gmd.common.SEOUL_CITY_HALL_LATITUDE
import com.sesac.gmd.common.SEOUL_CITY_HALL_LONGITUDE
import com.sesac.gmd.common.asLatLng
import com.sesac.gmd.data.model.Location
import com.sesac.gmd.databinding.FragmentHomeBinding
import com.sesac.gmd.presentation.base.BaseFragment
import com.sesac.gmd.presentation.ui.main.LocationViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(), OnMapReadyCallback {
    override val layoutResourceId = R.layout.fragment_home
    private val locationVM: LocationViewModel by activityViewModels()

    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setObserver()
    }

    private fun initViews() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap.apply {
            val startingPoint = locationVM.currentLocation.value?.let { location ->
                LatLng(
                    location.latitude,
                    location.longitude
                )
            } ?: run {
                LatLng(SEOUL_CITY_HALL_LATITUDE, SEOUL_CITY_HALL_LONGITUDE)
            }
            moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, DEFAULT_MAP_ZOOM_LEVEL))

            // 내 위치 Tracking Button
            isMyLocationEnabled = true
        }
        /**
         * 지도가 완전히 표시된 후 클릭에 반응하도록
         * onMapReady 안에서 setListener 호출
         */
        setListener()
    }

    private fun setObserver() {
        locationVM.currentLocation.observe(viewLifecycleOwner, ::updateMapPoint)
    }

    // FIXME: 메서드 이름 명확하게 수정
    private fun updateMapPoint(location: Location) {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                location.asLatLng(),
                DEFAULT_MAP_ZOOM_LEVEL
            )
        )
    }

    private fun setListener() {
        binding.btnCreateSong.setOnClickListener {
            CreateSongBottomSheetFragment().show(parentFragmentManager, tag)
        }
    }
}