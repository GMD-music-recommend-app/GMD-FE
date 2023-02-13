/**
* Created by 조진수
* date : 22/11/24
*/
package com.sesac.gmd.presentation.ui.main.bottomsheet

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.style.StyleSpan
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
import com.sesac.gmd.common.util.YOUTUBE_BASE_URL
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentSongInfoBottomSheetBinding
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.viewmodel.HomeChartViewModel

// TODO: Expanded Bottom Sheet Dialog 로 변경 필요
/**
 * 핀 클릭 시 해당 핀의 정보를 표시하는 BottomSheetDialog
 */
class SongInfoBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        private val TAG = SongInfoBottomSheetFragment::class.simpleName

        fun newInstance() = SongInfoBottomSheetFragment()

        fun newInstance(pinIdx: String) : BottomSheetDialogFragment {
            val bundle = Bundle()
            bundle.putString("pinIdx", pinIdx)

            return SongInfoBottomSheetFragment().also {
                it.arguments = bundle
            }
        }
    }
    private lateinit var binding: FragmentSongInfoBottomSheetBinding
    private lateinit var viewModel: HomeChartViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSongInfoBottomSheetBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(Repository())
        )[HomeChartViewModel::class.java]

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
            toastMessage(getString(R.string.unexpected_error))
            Log.d(DEFAULT_TAG + TAG, "getPinInfo() error! : ${e.message}")
        }
    }

    private fun setObserver() {
        viewModel.pinInfo.observe(viewLifecycleOwner) {
            // 가져온 곡 정보 view 와 매핑
            setContents()
            binding.bottomSheetMusicInfo.visibility = View.VISIBLE
        }
    }

    // 화면에 표시 될 핀 정보 세팅
    @SuppressLint("SetTextI18n")
    private fun setContents() {
        with(binding) {
            with(viewModel.pinInfo) {

                // 앨범 이미지, 곡 제목, 아티스트
                Glide.with(imgInfoMusicAlbumCover)
                    .load(this.value?.albumImage)
                    .placeholder(R.drawable.ic_sample_image)
                    .error(R.drawable.ic_sample_image)
                    .into(imgInfoMusicAlbumCover)
                txtInfoMusicTitle.text = this.value!!.songTitle
                txtInfoMusicArtist.text = this.value!!.artist

                // 추천한 유저, 사연
                val txtUser = this.value!!.nickname
                val blank = "님의 추천  "
                val txtStory = this.value!!.reason
                val txtBuilder = SpannableStringBuilder(txtUser+blank+txtStory)

                txtBuilder.setSpan(StyleSpan(Typeface.BOLD), 0,txtUser.length + blank.length, SPAN_INCLUSIVE_EXCLUSIVE)
                txtTemp.text = txtBuilder


                // 해시태그가 있으면 Hashtag TextView 를 Visible, 없으면 Gone 으로 설정
                if (this.value?.hashtag.isNullOrBlank()) {
                    txtInfoMusicHashtag.visibility = View.GONE
                } else {
                    txtInfoMusicHashtag.visibility = View.VISIBLE
                }

                // 임시 작성 코드(해당 음악이 로그인 한 유저가 만든 핀이면 노란색 아이콘 표시)
                if (!this.value?.isMade.toBoolean()) {
                    imgBookMark.setImageResource(R.drawable.ic_bookmark_filled)
                } else {
                    imgBookMark.setImageResource(R.drawable.ic_bookmark)
                }
                // TODO: 댓글 추가
            }
        }
    }

    // Listener 초기화 함수
    private fun setListener() {
        // TODO: 코드 수정 필요
        with(binding) {
            // 유튜브로 듣기
            containerInfoMusicAlbumImage.setOnClickListener {
                setAlertDialog(requireContext(), null,
                    getString(R.string.alert_go_to_youtube),
                    posFunc = {
                        startActivity(
                            Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                    "$YOUTUBE_BASE_URL+${viewModel.pinInfo.value!!.artist}+${viewModel.pinInfo.value!!.songTitle}"))
                        )},
                    negFunc = {}
                )}
            // 공감하기
            btnLike.setOnClickListener {
                toastMessage(getString(R.string.alert_service_ready))
            }
            // 공유하기
            btnShare.setOnClickListener {
                toastMessage(getString(R.string.alert_service_ready))
            }
        }
    }
}