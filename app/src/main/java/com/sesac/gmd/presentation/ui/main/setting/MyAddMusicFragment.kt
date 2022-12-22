package com.sesac.gmd.presentation.ui.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sesac.gmd.databinding.FragmentMyAddMusicBinding
import com.sesac.gmd.presentation.ui.main.setting.adapter.AddMusicAdapter
import com.sesac.gmd.presentation.ui.main.setting.adapter.AddMusicViewHolder

class MyAddMusicFragment : Fragment() {
    companion object {
        fun newInstance() = MyAddMusicFragment()

        const val TAG = "MyAddMusicFragment"
    }
    private lateinit var binding: FragmentMyAddMusicBinding
    private var addMusicAdapter: AddMusicAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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