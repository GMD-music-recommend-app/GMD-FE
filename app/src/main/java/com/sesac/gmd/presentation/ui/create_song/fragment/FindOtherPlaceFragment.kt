/**
* Created by 조진수
* date : 22/12/02
*/
package com.sesac.gmd.presentation.ui.create_song.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.sesac.gmd.common.util.Utils.Companion.setAlertDialog
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentFindOtherPlaceBinding
import com.sesac.gmd.presentation.ui.create_song.bottomsheet.FindOtherPlaceBottomSheetFragment
import com.sesac.gmd.presentation.ui.create_song.viewmodel.CreateSongViewModel
import com.sesac.gmd.presentation.ui.factory.ViewModelFactory

/**
 * 다른 곳에서 추가하기 Fragment
 */
class FindOtherPlaceFragment : Fragment(), OnMapReadyCallback {
    companion object {
        fun newInstance() = FindOtherPlaceFragment()

        // 지도의 중심점을 서울 시청으로 설정
        val startingPoint = LatLng(37.5662952, 126.97794509999994)
    }
    private var addedMarker: Marker? = null
    private lateinit var binding: FragmentFindOtherPlaceBinding
    private lateinit var viewModel: CreateSongViewModel
    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFindOtherPlaceBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(Repository()))[CreateSongViewModel::class.java]

        return binding.root
    }

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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16F))

        mMap.setOnMapLongClickListener { point ->
            if (addedMarker != null) {
                mMap.clear()
            }
            val position = LatLng(point.latitude, point.longitude)
            addedMarker = mMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("여기에 음악 추가하기")
            )
            addedMarker?.showInfoWindow()
            binding.btnCreatePlace.isVisible = addedMarker != null
        }
    }

    // Listener 초기화
    private fun setListener() {
        with(binding) {
            btnCreatePlace.setOnClickListener {
                setAlertDialog(requireContext(), null,
                    "이 곳에 음악을 추가하시겠습니까?",
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng ?: startingPoint, 16F))
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("FindOtherPlaceFragment", "An error occurred: $status")
            }
        })
    }
}