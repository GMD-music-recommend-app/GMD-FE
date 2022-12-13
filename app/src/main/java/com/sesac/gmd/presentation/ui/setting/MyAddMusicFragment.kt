package com.sesac.gmd.presentation.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sesac.gmd.databinding.FragmentMyAddMusicBinding
import com.sesac.gmd.presentation.main.MainActivity
import com.sesac.gmd.presentation.ui.setting.adapter.AddMusicAdapter
import com.sesac.gmd.presentation.ui.setting.adapter.AddMusicViewHolder

class MyAddMusicFragment : Fragment() {
    companion object {
        fun newInstance() = MyAddMusicFragment()

        const val TAG = "MyAddMusicFragment"
    }
    private lateinit var binding: FragmentMyAddMusicBinding
    private lateinit var activity : MainActivity

    private var addMusicAdapter: AddMusicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ChartFragment : onCreate() called!")
        activity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "ChartFragment : onCreateView() called!")
        binding = FragmentMyAddMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initializeData()
    }

    private fun initViews() = with(binding) {
        if (addMusicAdapter == null) {
            addMusicAdapter = AddMusicAdapter()
            recyclerView.adapter = addMusicAdapter
        }
    }

    private fun initializeData() {
        addMusicAdapter?.submitList(
            (1..10).map {
                AddMusicViewHolder.AddMusic(
                    id = it.toLong(),
                    location = "",
                    imageUrl = "",
                    title ="제목 : $it",
                    singerName = "가수 이름 : $it",
                    story = ""
                )
            }
        )
    }


}