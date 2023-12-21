package com.sesac.gmd.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<T : ViewDataBinding> : BottomSheetDialogFragment() {
    private var _binding: T? = null
    val binding get() = requireNotNull(_binding) { throw IllegalStateException("Failed to bind BottomSheet..!") }

    abstract val layoutResourceId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // BottomSheet 전체 화면으로 펼치기
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from<View>(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        // BottomSheet 바깥 영역 접근성 포커스 할당
        val outsideArea = dialog?.findViewById<View>(com.google.android.material.R.id.touch_outside)
        outsideArea?.apply {
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
            contentDescription = "바깥 영역을 클릭하여 창 닫기"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}