package com.sesac.gmd.presentation.ui.music_recommend.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
    private var addedMarker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this@OtherLocationSelectionFragment)

        // 해당 페이지에 대한 안내 Bottom Sheet Dialog 표시
        GuideLocationSelectionBottomSheetFragment().show(parentFragmentManager, "tag")
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
        /**
         * 지도가 완전히 표시된 후 클릭에 반응하도록
         * onMapReady 안에서 setListener 호출
         */
        setListener()
    }

    private fun setListener() {
        mMap.setOnMapLongClickListener(mMapLongClickListener)
        binding.btnSelectLocation.setOnClickListener { onSelectLocationButtonClick() }
    }

    private val mMapLongClickListener = GoogleMap.OnMapLongClickListener { point ->
        // 마커가 이미 생성 되어있으면 해당 마커 제거
        addedMarker?.let { mMap.clear() }

        val location = LatLng(point.latitude, point.longitude)
        addedMarker = mMap.addMarker(MarkerOptions().position(location))
        addedMarker?.showInfoWindow()

        // 장소가 선택 되었을 때 추천하기 버튼 표시
        binding.btnSelectLocation.isVisible = addedMarker != null
    }

    private fun onSelectLocationButtonClick() {
        AlertDialogFragment("이 곳에서 음악을 추천하시겠습니까?").apply {
            positiveButton(
                text = "확인",
                action = { goToNextPage() }
            )
            negativeButton("취소", null)
        }.also {
            it.show(parentFragmentManager, "dialog")
        }
    }

    private fun goToNextPage() {
        addedMarker?.let {
            val selectedLocation = LatLng(it.position.latitude, it.position.longitude)
            activityViewModel.setLocation(selectedLocation)

            Navigation.findNavController(binding.root).navigate(R.id.go_to_music_search)
        } ?: run {
            AlertDialogFragment("위치가 선택되지 않았습니다.\n다시 시도해 주세요.").apply {
                positiveButton(
                    text = "확인",
                    action = { requireActivity().finish() }
                )
            }
        }
    }

    // TODO:
    private fun searchLocation() {

    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = TODO("Not yet implemented")
}