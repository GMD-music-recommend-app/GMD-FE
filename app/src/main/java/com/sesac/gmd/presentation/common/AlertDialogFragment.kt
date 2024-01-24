package com.sesac.gmd.presentation.common

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.sesac.gmd.R
import com.sesac.gmd.common.dpToPixels
import com.sesac.gmd.databinding.FragmentAlertDialogBinding

class AlertDialogFragment(private val message: String) : DialogFragment() {
    private var _binding: FragmentAlertDialogBinding? = null
    private val binding get() = requireNotNull(_binding) { throw IllegalStateException("Failed to bind Dialog..!") }

    private var posText: String? = null
    private var posFunc: (() -> Unit)? = null
    private var negText: String? = null
    private var negFunc: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAlertDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setDialogConfigs()
        setNegativeButtonVisibility()
        setButtonListener()
    }

    private fun initViews() = with(binding) {
        txtAlertMessage.text = message
        btnAlertPositive.text = posText
        btnAlertNegative.text = negText
    }

    private fun setDialogConfigs() {
        isCancelable = false

        /**
         * Dialog 크기 설정
         * * 화면의 크기가 320dp 이하인 경우 다이얼로그 양쪽으로 20dp margin 설정
         * * 화면의 크기가 320dp를 넘는다면 다이얼로그의 너비를 320dp로 고정
         */
        val screenWidth = calculateScreenWidth(context as Activity) // 단위 : Dp
        val dialogWidth =
            if (screenWidth <= DIALOG_MAXIMUM_WIDTH_SIZE) {
                screenWidth - (2 * DIALOG_MARGIN_HORIZONTAL)
            } else {
                DIALOG_MAXIMUM_WIDTH_SIZE
            }

        dialog?.window?.setLayout(
            dialogWidth.dpToPixels(requireContext()), ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Dialog Background 세팅(모서리 둥글게)
        dialog?.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_dialog_alert)
        )
    }

    private fun setNegativeButtonVisibility() = with(binding) {
        if (negText == null) {
            btnAlertNegative.visibility = View.GONE
        }
    }

    private fun setButtonListener() = with(binding) {
        btnAlertPositive.setOnClickListener {
            posFunc?.invoke()
            dismiss()
        }
        btnAlertNegative.setOnClickListener {
            negFunc?.invoke()
            dismiss()
        }
    }

    fun AlertDialogFragment.positiveButton(text: String) {
        positiveButton(text, null)
    }

    fun AlertDialogFragment.positiveButton(action: (() -> Unit)?) {
        positiveButton(null, action)
    }

    fun AlertDialogFragment.positiveButton(text: String?, action: (() -> Unit)?) {
        this.posText = text ?: "확인"
        this.posFunc = action
    }

    fun AlertDialogFragment.negativeButton(text: String) {
        negativeButton(text, null)
    }

    fun AlertDialogFragment.negativeButton(action: (() -> Unit)?) {
        negativeButton(null, action)
    }

    fun AlertDialogFragment.negativeButton(text: String?, action: (() -> Unit)?) {
        this.negText = text ?: "취소"
        this.negFunc = action
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DIALOG_MAXIMUM_WIDTH_SIZE = 320
        private const val DIALOG_MARGIN_HORIZONTAL = 20

        /**
         * Device의 가로 길이를 계산하는 함수
         * 해당 함수에서 가로 길이 계산 후
         * Dialog 생성 시 가로 (길이 - 여백) 만큼의 너비로 Dialog 가로 길이 설정
         * @param activity Dialog 올려질 부모 View
         * @return 화면의 가로 길이(dp)
         */
        private fun calculateScreenWidth(activity: Activity): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = activity.windowManager.currentWindowMetrics
                val insets =
                    windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                val screenWidthPx = windowMetrics.bounds.width() - insets.left - insets.right

                val displayMetrics = activity.resources.displayMetrics
                val screenWidthDp = screenWidthPx / displayMetrics.density

                screenWidthDp.toInt()
            } else {
                val displayMetrics = DisplayMetrics()
                val windowManager =
                    activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val screenWidthPx = displayMetrics.widthPixels
                val screenWidthDp = screenWidthPx / displayMetrics.density
                screenWidthDp.toInt()
            }
        }
    }
}