package com.sesac.gmd.presentation.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sesac.gmd.databinding.FragmentMyCommentsBinding
import com.sesac.gmd.presentation.main.MainActivity
import com.sesac.gmd.presentation.ui.setting.adapter.MyCommentsAdapter
import com.sesac.gmd.presentation.ui.setting.adapter.MyCommentsViewHolder

private const val TAG = "MyCommentsFragment"

class MyCommentsFragment : Fragment() {
    private lateinit var binding: FragmentMyCommentsBinding
    private lateinit var activity: MainActivity

    private var myCommentsAdapter: MyCommentsAdapter? = null

    companion object {
        fun newInstance() = MyCommentsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MyCommentsFragment : onCreate() called!")
        activity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "ChartFragment : onCreateView() called!")
        binding = FragmentMyCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initializeData()
    }

    private fun initViews() = with(binding) {
        if (myCommentsAdapter == null) {
            myCommentsAdapter = MyCommentsAdapter()
            recyclerView.adapter = myCommentsAdapter
        }
    }

    private fun initializeData() {
        myCommentsAdapter?.submitList(
            (1..10).map {
                MyCommentsViewHolder.MyComments(
                    id = it.toLong(),
                    location = "",
                    singerName = "가수 이름 : $it",
                    title = "제목 : $it",
                    comments = ""
                )
            }
        )
    }


}