package com.denizk0461.studip.activity

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.denizk0461.studip.R
import com.denizk0461.studip.databinding.ActivityMainBinding
import com.denizk0461.studip.viewmodel.MainViewModel

/**
 * Main activity that handles all common fragments. This is opened on app launch.
 */
class MainActivity : FragmentActivity() {

    // View binding
    private lateinit var binding: ActivityMainBinding

    // View model
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Instantiate view model
        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(MainViewModel::class.java)

        // Inflate view binding and bind to this activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up bottom navigation view to navigate between fragments
        binding.navView.setupWithNavController(
            binding.navHostFragmentContentMain.getFragment<NavHostFragment>().findNavController()
        )

        setNavigationGraph()
    }

    /**
     * Sets the navigation graph for the fragments. Also sets the start destination.
     */
    private fun setNavigationGraph() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.main_nav_graph)
        navGraph.setStartDestination(if (viewModel.preferenceLaunchCanteen) {
            R.id.canteen
        } else {
            R.id.schedule
        })

        navController.graph = navGraph
    }
}