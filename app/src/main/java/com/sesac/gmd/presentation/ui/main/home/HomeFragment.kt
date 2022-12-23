/*
* Created by gabriel
* date : 22/11/21
* */
package com.sesac.gmd.presentation.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sesac.gmd.R
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentHomeBinding
import com.sesac.gmd.presentation.ui.main.bottomsheet.CreateSongBottomSheetFragment
import com.sesac.gmd.presentation.ui.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.viewmodel.MainViewModel
import com.sesac.gmd.presentation.ui.main.bottomsheet.SongInfoBottomSheetFragment

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(Repository()))[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 위치 초기화
        initLocation()
        // Listener 등록
        setListener()
        // Observer 등록
        setObserver()
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
        } else {
            viewModel.getCurrentLocation(requireContext())
        }
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
            pinLists.observe(viewLifecycleOwner) {
                // 핀 리스트 데이터를 가져오면(= when getPins() called,) 해당 핀 리스트 지도에 표시
                setMarkers()
            }
            location.observe(viewLifecycleOwner) {
                startingPoint = LatLng(viewModel.location.value!!.latitude, viewModel.location.value!!.longitude)
            }
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
                LatLng(37.5662952, 126.97794509999994)    // 서울 시청
            }

        with(mMap) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16F))
            setOnMarkerClickListener(this@HomeFragment)
            // 지도에 표시할 음악 핀 가져오기
            getPins()
        }
    }

    // 지도에 표시할 음악 핀 가져오기
    private fun getPins() {
        viewModel.getPinList(startingPoint.latitude, startingPoint.longitude)
    }

    // 음악 핀 지도에 표시
    private fun setMarkers() {
        viewModel.pinLists.value?.forEach {
            val location = LatLng(it.latitude, it.longitude)
            with(mMap) {
                addMarker(
                    MarkerOptions()
                        .position(location)
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
}