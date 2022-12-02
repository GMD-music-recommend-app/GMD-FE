/*
* Created by gabriel
* date : 22/11/24
* */

package com.sesac.gmd.presentation.ui.songinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sesac.gmd.R
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.databinding.FragmentSongInfoBottomSheetBinding

private const val TAG = "SongInfoBottomSheet"

// TODO: Expanded Bottom Sheet Dialog 로 변경 필요
class SongInfoBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSongInfoBottomSheetBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SongInfoBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSongInfoBottomSheetBinding.inflate(inflater, container, false)

        // TODO: 코드 수정 필요
        var isLike = false
        with(binding) {
            btnSongYoutube.setOnClickListener {
                toastMessage("유튜브 페이지로 이동하시겠습니까? 다이얼로그 표시 했다고 침")
                SystemClock.sleep(1_000L)
                startActivity(
                    Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/results?search_query=NMIXX+COOL+(Your+rainbow)"))
                )
            }
            btnSongLike.setOnClickListener {
                if (!isLike) {
                    toastMessage("공감 +1")
                    it.setBackgroundResource(R.drawable.ic_liked)
                    isLike = true
                } else {
                    toastMessage("공감 취소")
                    it.setBackgroundResource(R.drawable.ic_like)
                    isLike = false
                }
            }
            btnSongShare.setOnClickListener {
                toastMessage("이미지 캡쳐 완료")
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}