package com.sesac.gmd.presentation.ui.main.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sesac.gmd.databinding.FragmentMyCommentsBinding
import com.sesac.gmd.presentation.ui.main.fragment.setting.adapter.MyCommentsAdapter
import com.sesac.gmd.presentation.ui.main.fragment.setting.adapter.MyCommentsViewHolder

class MyCommentsFragment : Fragment() {
    companion object {
        fun newInstance() = MyCommentsFragment()
    }
    private lateinit var binding: FragmentMyCommentsBinding
    private var myCommentsAdapter: MyCommentsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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