/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sesac.gmd.R
import com.sesac.gmd.common.base.BaseFragment
import com.sesac.gmd.databinding.FragmentHomeBinding
import com.sesac.gmd.presentation.ui.create_song.CreateSongBottomSheetFragment
import com.sesac.gmd.presentation.ui.login.LoginBottomSheetFragment
import com.sesac.gmd.presentation.ui.songinfo.SongInfoBottomSheetFragment

private const val TAG = "HomeFragment"

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), OnMapReadyCallback, OnMarkerClickListener {
    private lateinit var mMap: GoogleMap
    private val bottomSheet by lazy { view?.findViewById<ConstraintLayout>(R.id.bottom_sheet_song) }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 구글 맵 생성
        initMap()
        // Listener 초기화
        setListener()
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (googleMap != null) {
            mMap = googleMap
        }

        // TODO: 최초 시작 위치 임시 설정. 추후 수정 필요
        val startingPoint = LatLng(37.573898277022, 126.9731314753)
        with(mMap) {
            addMarker(MarkerOptions()
                .position(startingPoint)
                .title("NMIXX - COOL(Your rainbow)"))
                ?.hideInfoWindow()
            moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 15F))
            setOnMarkerClickListener(this@HomeFragment)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Log.d(TAG, "Marker Clicked!")

        // TODO: 임시 작성 코드. 추후 수정 필요
        val songInfo = SongInfoBottomSheetFragment.newInstance()
        songInfo.show(childFragmentManager, TAG)
        return true
    }

    private fun setListener() {
        with(binding) {
            btnCreateSong.setOnClickListener {
                Log.d(TAG, "노래 추천하기 버튼 Clicked!")
                val createSong = CreateSongBottomSheetFragment.newInstance()
                createSong.show(childFragmentManager, TAG)
            }
            // TODO: 이건 진짜 꼭 수정 필요!!! 임시 작성 코드
            fabGPSTracking.setOnClickListener {
                val loginBottom = LoginBottomSheetFragment.newInstance()
                loginBottom.show(childFragmentManager, TAG)
            }
        }
    }
}