/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.presentation.ui.chart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.gmd.R
import com.sesac.gmd.databinding.FragmentChartBinding
import com.sesac.gmd.presentation.ui.activities.MainActivity

private const val TAG = "ChartFragment"

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity : MainActivity

    companion object {
        fun newInstance() = ChartFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ChartFragment : onCreate() called!")
        activity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "ChartFragment : onCreateView() called!")
        _binding = FragmentChartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}