package com.denizk0461.studip.activity

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.denizk0461.studip.databinding.ActivityMainBinding

/**
 * Main activity that handles all common fragments. This is opened on app launch.
 */
class MainActivity : FragmentActivity() {

    // View binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Inflate view binding and bind to this activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up bottom navigation view to navigate between fragments
        binding.navView.setupWithNavController(
            binding.navHostFragmentContentMain.getFragment<NavHostFragment>().findNavController()
        )
    }
}