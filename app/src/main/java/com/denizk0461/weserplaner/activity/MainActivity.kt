package com.denizk0461.weserplaner.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.databinding.ActivityMainBinding
import com.denizk0461.weserplaner.values.AppLayout
import com.denizk0461.weserplaner.viewmodel.MainViewModel

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

//        if (viewModel.getTimetableCount() == 0) {
//            viewModel.insertTimetable(
//                Timetable(0, "default")
//            )
//        }

        /*
         * Check whether the user is launching the app for the first time in order to show a
         * tutorial. This value will be changed to 'false' upon completing the tutorial, thus not
         * requiring any changes to the value here.
         */
        if (viewModel.preferenceFirstLaunch) {
            /*
             * Close this activity to prevent skipping the introduction activity by pressing the
             * back button.
             */
            finish()

            // Launch the tutorial activity
            startActivity(Intent(this, IntroductionActivity::class.java))
        }

        when (viewModel.preferenceAppLayout) {
            AppLayout.DEFAULT -> {
                // Ensure that the navigation bar is visible
                binding.navView.visibility = View.VISIBLE

                // Set up bottom navigation view to navigate between fragments
                binding.navView.setupWithNavController(
                    binding.navHostFragmentContentMain.getFragment<NavHostFragment>().findNavController()
                )

                // Check whether the user has enabled beta screens
                val showBetaScreens = viewModel.preferenceBetaScreensEnabled

                // Set the visibility for the navigation bar items for beta screens accordingly
                binding.navView.menu.findItem(R.id.exam_overview).isVisible = showBetaScreens
                binding.navView.menu.findItem(R.id.room_finder).isVisible = showBetaScreens

                // Set up navigation graph for the default layout
                (supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment)
                    .setupDefaultNavigationGraph()
            }
            AppLayout.COMPACT -> {
                // Hide the navigation bar, as it is unnecessary in the compact layout
                binding.navView.visibility = View.GONE

                // Set up navigation graph for the compact layout
                (supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment)
                    .setupCompactNavigationGraph()
            }
        }
    }

    /**
     * Sets the default navigation graph for the fragments. Also sets the start destination, as
     * specified by the user.
     */
    private fun NavHostFragment.setupDefaultNavigationGraph() {
        // Inflate new nav graph
        val navGraph = this.navController.navInflater.inflate(R.navigation.nav_graph_default)

        // Set the starting fragment for the app
        navGraph.setStartDestination(when (viewModel.preferenceLaunchFragment) {
            1 -> R.id.canteen
            else -> R.id.schedule // 0
        })

        // Set the newly defined nav graph
        this.navController.graph = navGraph
    }

    /**
     * Sets the compact navigation graph for the fragments.
     */
    private fun NavHostFragment.setupCompactNavigationGraph() {
        // Inflate new nav graph
        val navGraph = this.navController.navInflater.inflate(R.navigation.nav_graph_compact)

        // Set the newly defined nav graph
        this.navController.graph = navGraph
    }
}