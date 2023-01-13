/**
* Created by 조진수
* date : 22/11/24
*/
package com.sesac.gmd.presentation.ui.main.bottomsheet

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sesac.gmd.R
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.Utils.Companion.setAlertDialog
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentSongInfoBottomSheetBinding
import com.sesac.gmd.presentation.ui.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.viewmodel.MainViewModel

// TODO: Expanded Bottom Sheet Dialog 로 변경 필요
/**
 * 핀 클릭 시 해당 핀의 정보를 표시하는 BottomSheetDialog
 */
class SongInfoBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = SongInfoBottomSheetFragment()

        fun newInstance(pinIdx: String) : BottomSheetDialogFragment {
            val bundle = Bundle()
            bundle.putString("pinIdx", pinIdx)

            return SongInfoBottomSheetFragment().also {
                it.arguments = bundle
            }
        }
        const val TAG = "SongInfo"
    }
    private lateinit var binding: FragmentSongInfoBottomSheetBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSongInfoBottomSheetBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(Repository()))[MainViewModel::class.java]

        Log.d(DEFAULT_TAG+TAG, "${arguments?.getString("pinIdx")}")

        // 해당 곡 상세 정보 가져오기
        getSongInfo()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer 등록
        setObserver()
        // Listener 등록
        setListener()
    }

    // 해당 곡 상세 정보 가져오기
    private fun getSongInfo() {
        try {
            viewModel.getPinInfo(arguments?.getString("pinIdx")!!.toInt())
        } catch (e: Exception) {
            dismiss()
            toastMessage("예기치 못한 오류가 발생했습니다.")
            Log.d(DEFAULT_TAG + TAG, "getPinInfo() error! : ${e.message}")
        }
    }

    private fun setObserver() {
        viewModel.pinInfo.observe(viewLifecycleOwner) {
            // 가져온 곡 정보 view 와 매핑
            setContents()
            binding.bottomSheetSong.visibility = View.VISIBLE
        }
    }

    // 화면에 표시 될 핀 정보 세팅
    @SuppressLint("SetTextI18n")
    private fun setContents() {
        with(binding) {
            with(viewModel.pinInfo) {
                Glide.with(ivSongAlbumCover)
                    .load(this.value?.albumImage)
                    .into(ivSongAlbumCover)
                txtSongTitle.text = this.value?.songTitle.toString()
                txtSongArtist.text = this.value?.artist.toString()
                txtSongAlbumName.text = this.value?.albumTitle.toString()
                txtSongHashtag.text = this.value?.hashtag.toString()
                txtSongUserName.text = this.value?.nickname.toString()
                txtSongUserStory.text = this.value?.reason.toString()
                if (!this.value?.isMade.toBoolean()) {
                    ivStar.setImageResource(R.drawable.ic_star)
                } else {
                    ivStar.setImageResource(R.drawable.ic_star_empty)
                }
                // TODO: 댓글 추가

                // TODO: 임시 작성 코드 추후 삭제 필요
                if(arguments?.getString("pinIdx") == "50") {
                    txtSongComment1.text = "JetPack"
                    txtSongComment1Spc.text = "노래방 가서 불러야겟다"
                    txtSongComment2.text = ""
                    txtSongComment2Spc.text = ""
                    txtSongComment3.text = ""
                    txtSongComment3Spc.text = ""
                } else if (arguments?.getString("pinIdx") == "51") {
                    txtSongComment1.text = "Milli"
                    txtSongComment1Spc.text = "크러쉬 2411도 추천"
                    txtSongComment2.text = "하늘보리"
                    txtSongComment2Spc.text = "오 추천 감사합니다"
                    txtSongComment3.text = "브리브리대마왕"
                    txtSongComment3Spc.text = "명반임. 전곡 다 들어보는거 추천"
                }
            }
        }
    }

    // Listener 초기화 함수
    private fun setListener() {
        // TODO: 코드 수정 필요
        var isLike = false
        with(binding) {
            // 유튜브로 듣기
            btnSongYoutube.setOnClickListener {
                setAlertDialog(requireContext(), null,
                    "유튜브 페이지로 이동하시겠습니까?",
                    posFunc = {
                        startActivity(
                            Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                    "https://www.youtube.com/results?search_query=${viewModel.pinInfo.value!!.artist}+${viewModel.pinInfo.value!!.songTitle}"))
                        )},
                    negFunc = {}
                )}
            // 공감하기
            btnSongLike.setOnClickListener {
                isLike =
                    if (!isLike) {
                        toastMessage("공감 +1")
                        it.setBackgroundResource(R.drawable.ic_liked)
                        true
                    } else {
                        toastMessage("공감 취소")
                        it.setBackgroundResource(R.drawable.ic_like)
                        false
                    }
            }
            // 공유하기
            btnSongShare.setOnClickListener {
                toastMessage("준비 중입니다.")
            }
        }
    }
}