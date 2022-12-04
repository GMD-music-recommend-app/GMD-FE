/*
* Created by gabriel
* date : 22/12/02
* */

package com.sesac.gmd.presentation.ui.create_song.other_place

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.sesac.gmd.R
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.databinding.FragmentFindOtherPlaceBinding
import com.sesac.gmd.presentation.ui.create_song.create.SearchSongFragment

private const val TAG = "FinOtherPlaceFragment"

class FindOtherPlaceFragment : BaseFragment<FragmentFindOtherPlaceBinding>(FragmentFindOtherPlaceBinding::inflate), OnMapReadyCallback{
    private lateinit var mMap: GoogleMap

    companion object {
        fun newInstance() = FindOtherPlaceFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 구글 맵 생성
        initMap()
        // Listener 초기화
        setListener()

        // TODO: 임시 작성 코드. 추후 수정 필요
        val findOtherBottomSheet = FindOtherPlaceBottomSheetFragment.newInstance()
        findOtherBottomSheet.show(childFragmentManager, TAG)
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.subMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun setListener() {
        val supportMapFragment = parentFragmentManager

        with(binding) {
            btnCreatePlace.setOnClickListener {
                supportMapFragment
                    .beginTransaction()
                    .replace(R.id.container, SearchSongFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (googleMap != null) {
            mMap = googleMap
        }

        val startingPoint = LatLng(36.573898277022, 126.9731314753)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 15F))

        mMap.setOnMapLongClickListener { point ->
            val position = LatLng(point.latitude, point.longitude)
            mMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("여기에 생성한 음악 핀")
            )
                ?.showInfoWindow()
        }
    }
/*
    override fun onLongClick(v: View?): Boolean {
        // TODO: 최초 시작 위치 임시 설정. 추후 수정 필요
        val startingPoint = LatLng(point, 126.9731314753)
        with(mMap) {
            addMarker(MarkerOptions()
                .position(startingPoint)
                .title("여기에 생성한 음악 핀"))
            moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 15F))
        }
    }*/
}