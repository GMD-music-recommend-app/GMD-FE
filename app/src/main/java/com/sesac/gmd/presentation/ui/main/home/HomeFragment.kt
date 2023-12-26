package com.sesac.gmd.presentation.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
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
    private val viewModel: HomeViewModel by viewModels()
    private val activityViewModel: LocationViewModel by activityViewModels()

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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
            val startingPoint = LatLng(SEOUL_CITY_HALL_LATITUDE, SEOUL_CITY_HALL_LONGITUDE)
            moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, DEFAULT_MAP_ZOOM_LEVEL))

            // 내 위치 Tracking Button
            isMyLocationEnabled = true
        }
        // TODO: 주석 작성 필요
        setListener()
    }

    private fun setObserver() {
        activityViewModel.currentLocation.observe(viewLifecycleOwner, ::updateMapPoint)
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

    override val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            super.handleOnBackCancelled()
        }
    }
}