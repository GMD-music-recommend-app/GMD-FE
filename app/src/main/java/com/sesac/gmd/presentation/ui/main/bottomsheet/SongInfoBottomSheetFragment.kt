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
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.sesac.gmd.R
import com.sesac.gmd.common.util.AlertDialogUtil.displayYesNoDialog
import com.sesac.gmd.common.util.DEFAULT_TAG
import com.sesac.gmd.common.util.RX_THROTTLE_TIME
import com.sesac.gmd.common.util.Utils.Companion.displayToastExceptions
import com.sesac.gmd.common.util.Utils.Companion.toastMessage
import com.sesac.gmd.common.util.YOUTUBE_BASE_URL
import com.sesac.gmd.data.model.remote.GetPinInfoResult
import com.sesac.gmd.data.repository.Repository
import com.sesac.gmd.databinding.FragmentSongInfoBottomSheetBinding
import com.sesac.gmd.presentation.factory.ViewModelFactory
import com.sesac.gmd.presentation.ui.main.viewmodel.HomeChartViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * 핀 클릭 시 해당 핀의 정보를 표시하는 BottomSheetDialog
 * TODO: Expanded Bottom Sheet Dialog 로 변경 필요
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
    private val viewModel: HomeChartViewModel by activityViewModels { ViewModelFactory(Repository()) }
    private val disposablesSongInfo = CompositeDisposable()
    private var pinIdx : String? = null    // 유저가 클릭한 핀의 인덱스

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSongInfoBottomSheetBinding.inflate(inflater, container, false)

        // TODO: 임시 작성 코드. 추후 삭제 필요 
        viewModel.clearPinLikeValue()

        pinIdx = arguments?.getString("pinIdx").toString()

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
            Log.e(DEFAULT_TAG+TAG, e.toString())
            displayToastExceptions(e) // 오류 내용에 따라 ToastMessage 출력
            dismiss()
        }
    }

    private fun setObserver() = with(viewModel) {
        pinInfo.observe(viewLifecycleOwner) {
            // 가져온 곡 정보 view 와 매핑
            initViews()
            binding.bottomSheetMusicInfo.visibility = View.VISIBLE
        }
        isPinLiked.observe(viewLifecycleOwner) {
            if (it == getString(R.string.success_to_like_pin)) {
                binding.btnLike.setImageResource(R.drawable.ic_liked)
                toastMessage(getString(R.string.alert_liked_pin))
            } else if (it == getString(R.string.success_to_cancel_liked_pin)) {
                binding.btnLike.setImageResource(R.drawable.ic_like)
                toastMessage(getString(R.string.alert_cancel_to_like_pin))
            }
        }
    }

    // 화면에 표시 될 핀 정보 세팅
    @SuppressLint("SetTextI18n")
    private fun initViews() = with(binding) {
        val pinInfo = viewModel.pinInfo.value

        if (pinInfo != null) {
            // 앨범 이미지, 곡 제목, 아티스트
            initSongInfoViews(pinInfo)
            // 추천한 유저, 사연 부분 View 표시
            setUserNameStory(pinInfo)
            // 해시태그 부분 View 설정
            initHashTagView(pinInfo)
            // 내가 생성한 추천 음악인지 표시
            setBookmarkIcon(pinInfo)
        }
    }

    // 앨범 이미지, 곡 제목, 아티스트
    private fun initSongInfoViews(pinInfo: GetPinInfoResult) = with(binding) {
        Glide.with(imgInfoMusicAlbumCover)
            .load(pinInfo.albumImage)
            .placeholder(R.drawable.ic_sample_image)
            .error(R.drawable.ic_sample_image)
            .into(imgInfoMusicAlbumCover)
        txtInfoMusicTitle.text = pinInfo.songTitle
        txtInfoMusicArtist.text = pinInfo.artist
    }

    // 추천한 유저, 사연 부분 View 표시
    private fun setUserNameStory(pinInfo: GetPinInfoResult) {
        // 추천한 유저, 사연
        val txtUser = "${pinInfo.nickname}님의 추천  "
        val txtUserStory = pinInfo.reason
        val txtBuilder = SpannableStringBuilder(txtUser+txtUserStory)

        // 추천한 유저의 이름은 Bold 로 표시되도록 설정
        txtBuilder.setSpan(StyleSpan(Typeface.BOLD), 0,txtUser.length, SPAN_INCLUSIVE_EXCLUSIVE)
        binding.txtViewMore.text = txtBuilder
    }

    // 해시태그 부분 View 설정
    private fun initHashTagView(pinInfo: GetPinInfoResult) = with(binding) {
        // 해시태그가 있으면 Hashtag TextView 를 Visible, 없으면 Gone 으로 설정
        if (pinInfo.hashtag.isNullOrBlank()) {
            txtInfoMusicHashtag.visibility = View.GONE
        } else {
            txtInfoMusicHashtag.text = pinInfo.hashtag
            txtInfoMusicHashtag.visibility = View.VISIBLE
        }
    }

    // 해당 음악이 로그인 한 유저가 만든 핀이면 노란색 아이콘 표시
    private fun setBookmarkIcon(pinInfo: GetPinInfoResult) {
        if (pinInfo.isMade == 1.toString()) {
            binding.imgBookMark.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            binding.imgBookMark.setImageResource(R.drawable.ic_bookmark)
        }
    }

    // Listener 초기화 함수 TODO: 코드 수정 필요
    private fun setListener() = with(binding) {
        // 유튜브로 듣기
        val observableYotube = containerInfoMusicAlbumImage
            .clicks()
            .throttleFirst(RX_THROTTLE_TIME, TimeUnit.MILLISECONDS)
            .subscribe { listenToYoutube() }

        // 공감하기
        val observableLike = btnLike
            .clicks()
            .throttleFirst(RX_THROTTLE_TIME, TimeUnit.MILLISECONDS)
            .subscribe { likedPin(pinIdx!!) }

        // 공유하기
        val observableShare = btnShare
            .clicks()
            .throttleFirst(RX_THROTTLE_TIME, TimeUnit.MILLISECONDS)
            .subscribe { sharePinInfo() }

        disposablesSongInfo.addAll(observableYotube, observableLike, observableShare)
    }

    // 유튜브로 듣기
    private fun listenToYoutube() {
        displayYesNoDialog(requireContext(), null, getString(R.string.alert_go_to_youtube),
            posFunc = {
                startActivity(
                    Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                            "$YOUTUBE_BASE_URL+${viewModel.pinInfo.value!!.artist}+${viewModel.pinInfo.value!!.songTitle}"))
                )},
            negFunc = {}
        )
    }

    // 공감하기
    private fun likedPin(pinIdx: String) {
        try {
            viewModel.insertLikePin(pinIdx.toInt())
        } catch (e : Exception) {
            displayToastExceptions(e)
        }
    }

    // 공유하기
    private fun sharePinInfo() {
        toastMessage(getString(R.string.alert_service_ready))
    }

    override fun onDestroy() {
        super.onDestroy()
        // Disposable 객체 해제
        disposablesSongInfo.dispose()
    }
}