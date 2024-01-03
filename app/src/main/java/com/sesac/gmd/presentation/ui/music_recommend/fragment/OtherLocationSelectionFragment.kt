package com.sesac.gmd.presentation.ui.music_recommend.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.sesac.gmd.R
import com.sesac.gmd.common.DEFAULT_MAP_ZOOM_LEVEL
import com.sesac.gmd.common.SEOUL_CITY_HALL_LATITUDE
import com.sesac.gmd.common.SEOUL_CITY_HALL_LONGITUDE
import com.sesac.gmd.databinding.FragmentOtherLocationSelectionBinding
import com.sesac.gmd.presentation.base.BaseFragment
import com.sesac.gmd.presentation.common.AlertDialogFragment
import com.sesac.gmd.presentation.ui.music_recommend.SongViewModel

class OtherLocationSelectionFragment : BaseFragment<FragmentOtherLocationSelectionBinding>(), OnMapReadyCallback {
    override val layoutResourceId = R.layout.fragment_other_location_selection
    private val activityViewModel: SongViewModel by activityViewModels()

    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this@OtherLocationSelectionFragment)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googlemap: GoogleMap) {
        mMap = googlemap.apply {
            val startingPoint = activityViewModel.selectedLocation.value?.let { location ->
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
        // TODO: 주석 작성
        setListener()
    }

    private fun setListener() {
        mMap.setOnMapLongClickListener { onMapLongClick() }
        binding.btnSelectLocation.setOnClickListener { onSelectLocationButtonClick() }
    }

    private fun onMapLongClick() {

    }

    private fun onSelectLocationButtonClick() {
        AlertDialogFragment("이 곳에서 음악을 추천하시겠습니까?").apply {
            positiveButton(
                text = "확인",
                action = {
                    goToNextPage()
                }
            )
            negativeButton("취소", null)
        }.also {
            it.show(parentFragmentManager, "dialog")
        }
    }

    private fun goToNextPage() {
        // FIXME: 마커 위치 받아와서 위치 설정
        val selectedLocation = LatLng(SEOUL_CITY_HALL_LATITUDE, SEOUL_CITY_HALL_LONGITUDE)
        activityViewModel.setLocation(selectedLocation)

        Navigation.findNavController(binding.root).navigate(R.id.go_to_music_search)
    }

    private fun searchLocation() {

    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = TODO("Not yet implemented")
}