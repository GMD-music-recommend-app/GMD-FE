package com.sesac.gmd.presentation.ui.music_recommend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.sesac.gmd.R
import com.sesac.gmd.common.CREATE_MUSIC_HERE
import com.sesac.gmd.common.GO_TO_PAGE
import com.sesac.gmd.common.Logger
import com.sesac.gmd.common.SET_OTHER_PLACE
import com.sesac.gmd.databinding.ActivityMusicRecommendBinding

// TODO:  
class MusicRecommendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicRecommendBinding
    private lateinit var mNavController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initNavigation()
        initView(savedInstanceState)
    }

    private fun initBinding() {
        binding = ActivityMusicRecommendBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_create_song) as NavHostFragment
        mNavController = navHostFragment.navController

        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph_music_recommend)

        val appBarConfiguration = AppBarConfiguration(navGraph)
        binding.toolbar.setupWithNavController(mNavController,appBarConfiguration)

        mNavController.graph = navGraph
    }

    private fun initView(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val page = intent.getStringExtra(GO_TO_PAGE)
            Logger.e("page: ${page}")

            when (page) {
                SET_OTHER_PLACE -> {
                    mNavController.graph.setStartDestination(R.id.otherSpotSelectionFragment)
                }
                CREATE_MUSIC_HERE -> {
                    mNavController.graph.setStartDestination(R.id.musicSearchFragment)
                    mNavController.navigate(R.id.go_to_music_search)
                }
            }
        }
    }
}