/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.presentation.ui.home

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentHomeBinding
import com.sesac.gmd.presentation.ui.create_song.bottomsheet.CreateSongBottomSheetFragment
import com.sesac.gmd.presentation.ui.songinfo.SongInfoBottomSheetFragment

class HomeFragment : Fragment(),
    OnMapReadyCallback,
    OnMarkerClickListener,
    OnMyLocationClickListener,
    OnMyLocationButtonClickListener
{
    companion object {
        fun newInstance() = HomeFragment()
    }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 구글 맵 생성
        initMap()
        // Listener 초기화
        setListener()
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Listener 초기화
    private fun setListener() {
        with(binding) {
            btnCreateSong.setOnClickListener {
                Log.d("HomeFragment", "노래 추천하기 버튼 Clicked!")
                val createSong = CreateSongBottomSheetFragment.newInstance()
                createSong.show(childFragmentManager, "HomeFragment")
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // TODO: 최초 시작 위치 임시 설정. 추후 수정 필요
        val startingPoint = LatLng(37.5662952, 126.97794509999994)
        with(mMap) {
            addMarker(
                MarkerOptions()
                .position(startingPoint)
                .title("NMIXX - COOL(Your rainbow)"))
                ?.hideInfoWindow()
            moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16F))
            setOnMarkerClickListener(this@HomeFragment)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        // TODO: 임시 작성 코드. 추후 수정 필요
        val songInfo = SongInfoBottomSheetFragment.newInstance()
        songInfo.show(childFragmentManager, "HomeFragment")
        return true
    }

    override fun onMyLocationClick(p0: Location) {}

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }
}

//class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
//    OnMapReadyCallback,
//    OnMarkerClickListener,
//    OnMyLocationClickListener,
//    OnMyLocationButtonClickListener
//{
//    companion object {
//        fun newInstance() = HomeFragment()
//    }
//    private lateinit var mMap: GoogleMap
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // 구글 맵 생성
//        initMap()
//        // Listener 초기화
//        setListener()
//    }
//
//    private fun initMap() {
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//    // Listener 초기화
//    private fun setListener() {
//        with(binding) {
//            btnCreateSong.setOnClickListener {
//                Log.d(TAG, "노래 추천하기 버튼 Clicked!")
//                val createSong = CreateSongBottomSheetFragment.newInstance()
//                createSong.show(childFragmentManager, TAG)
//            }
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // TODO: 최초 시작 위치 임시 설정. 추후 수정 필요
//        val startingPoint = LatLng(37.5662952, 126.97794509999994)
//        with(mMap) {
//            addMarker(MarkerOptions()
//                .position(startingPoint)
//                .title("NMIXX - COOL(Your rainbow)"))
//                ?.hideInfoWindow()
//            moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16F))
//            setOnMarkerClickListener(this@HomeFragment)
//        }
//    }
//
//    override fun onMarkerClick(marker: Marker): Boolean {
//        Log.d(TAG, "Marker Clicked!")
//
//        // TODO: 임시 작성 코드. 추후 수정 필요
//        val songInfo = SongInfoBottomSheetFragment.newInstance()
//        songInfo.show(childFragmentManager, TAG)
//        return true
//    }
//
//    override fun onMyLocationClick(p0: Location) {
//
//    }
//
//    override fun onMyLocationButtonClick(): Boolean {
//        return false
//    }
//}