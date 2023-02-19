/**
* Created by 조진수
* date : 22/12/02
*/
package com.sesac.gmd.presentation.ui.create_song.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.sesac.gmd.R
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.common.util.DEFAULT_ZOOM_LEVEL
import com.sesac.gmd.common.util.SEOUL_CITY_LATITUDE
import com.sesac.gmd.common.util.SEOUL_CITY_LONGITUDE
import com.sesac.gmd.common.util.Utils.Companion.setAlertDialog
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentFindOtherPlaceBinding
import com.sesac.gmd.presentation.ui.create_song.bottomsheet.FindOtherPlaceBottomSheetFragment
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.factory.ViewModelFactory

/**
 * 다른 곳에서 추가하기 Fragment
 */
class FindOtherPlaceFragment :
    BaseFragment<FragmentFindOtherPlaceBinding>(FragmentFindOtherPlaceBinding::inflate),
    OnMapReadyCallback
{
    companion object {
        fun newInstance() = FindOtherPlaceFragment()

        // 지도의 중심 점을 서울 시청으로 설정
        val startingPoint = LatLng(SEOUL_CITY_LATITUDE, SEOUL_CITY_LONGITUDE)
    }
    private val viewModel: CreateSongViewModel by activityViewModels { ViewModelFactory(Repository()) }
    private lateinit var mMap: GoogleMap
    private var addedMarker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 구글 맵 생성
        initMap()
        // Listener 초기화
        setListener()
        // 장소 검색
        searchPlace()
    }

    // 구글 맵 초기화
    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.subMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val findOtherBottomSheet = FindOtherPlaceBottomSheetFragment.newInstance()
        findOtherBottomSheet.show(childFragmentManager, findOtherBottomSheet.tag)
    }

    // 구글 맵 생성 시 지도 구성
    override fun onMapReady(googleMap: GoogleMap) = with(googleMap) {
        // 구글 맵 최초 화면 설정(중심점, 줌 레벨)
        moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, DEFAULT_ZOOM_LEVEL))

        // 구글 맵 내 Click Listener
        setOnMapLongClickListener { point ->
            // 마커가 이미 생성되어있으면 해당 마커 제거(마커가 2개 이상 생성되지 않도록)
            if (addedMarker != null) {
                clear()
            }

            // 마커(추천할 유저의 위치) 생성
            val position = LatLng(point.latitude, point.longitude)
            addedMarker = addMarker(
                MarkerOptions()
                    .position(position)
            )
            addedMarker?.showInfoWindow()
            binding.btnCreatePlace.isVisible = addedMarker != null
        }
    }

    // Listener 초기화
    private fun setListener() = with(binding) {
        btnCreatePlace.setOnClickListener {
            setAlertDialog(requireContext(),
                null,
                getString(R.string.alert_create_pin_here),
                posFunc = {
                    viewModel.setLocation(requireContext(), addedMarker!!.position.latitude, addedMarker!!.position.longitude)
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, SearchSongFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                          },
                negFunc = {})
        }
    }

    // 장소 검색
    private fun searchPlace() {
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("FindOtherPlaceFragment", "Place: ${place.name}, ${place.id}")
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng ?: startingPoint, DEFAULT_ZOOM_LEVEL))
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("FindOtherPlaceFragment", "An error occurred: $status")
            }
        })
    }
}