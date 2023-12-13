package com.sesac.gmd.presentation.ui.create_song

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sesac.gmd.databinding.ActivityCreateSongBinding

class CreateSongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateSongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSongBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }
}