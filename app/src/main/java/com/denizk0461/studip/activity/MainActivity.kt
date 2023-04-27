package com.denizk0461.studip.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.denizk0461.studip.R
import com.denizk0461.studip.data.Dependencies
import com.denizk0461.studip.databinding.ActivityMainBinding
import com.denizk0461.studip.db.AppRepository
import com.denizk0461.studip.fragment.EventFragment
import com.denizk0461.studip.fragment.CanteenFragment
import com.denizk0461.studip.fragment.SettingsFragment
import com.denizk0461.studip.model.SettingsPreferences

/**
 * Main activity that handles all common fragments. This is opened on app launch.
 */
class MainActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityMainBinding

    /*
     * Tag for the currently active fragment; set on activity launch to the fragment that will be
     * shown first.
     */
    private var currentFragment: String = "event"

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        /*
         * Instantiate repository object that is accessed by the fragments' view models to retrieve
         * data.
         */
        Dependencies.repo = AppRepository(application)

        // Inflate view binding and bind to this activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Open the fragment that the user specified to open on app start
        if (
            Dependencies.repo.getBooleanPreference(SettingsPreferences.LAUNCH_CANTEEN_ON_START)
        ) {
            // Open canteen fragment
            binding.contentMain.navView.selectedItemId = R.id.food
            currentFragment = "canteen"
        } else {
            // Open event fragment
            binding.contentMain.navView.selectedItemId = R.id.plan
            currentFragment = "event"
        }

        // Set up navigation view bar to launch fragments
        binding.contentMain.navView.setOnItemSelectedListener { item ->
            // Launch fragment based on the item that has been clicked
            loadFragment(when (item.itemId) {
                R.id.food -> "canteen"
                R.id.settings -> "settings"
                else -> "event"
            })
        }

        // Launch the fragment defined in currentFragment
        loadFragment(currentFragment)
    }

    /**
     * Loads a given fragment.
     *
     * @param type  tag for the fragment that is to be opened
     * @return      true on success, false if another fragment is currently instantiating
     */
    private fun loadFragment(type: String): Boolean {

        // Find the fragment that is to be opened, or create it if it hasn't been instantiated yet
        val fragment = supportFragmentManager.findFragmentByTag(type) ?: getFragment(type)

        // Don't interrupt the current fragment's lifecycle if it isn't fully initialised
        if (fragment.lifecycle.currentState != Lifecycle.State.INITIALIZED) {
            return false
        }

        // Start the transaction to show the new fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, fragment, type)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

        // Success
        return true
    }

    /**
     * Retrieve fragment by its tag.
     *
     * @param type  tag for the corresponding fragment
     * @return      the corresponding fragment
     */
    private fun getFragment(type: String) = when (type) {
        "canteen" -> CanteenFragment() // CanteenFragment.kt
        "settings" -> SettingsFragment() // SettingsFragment.kt
        else -> EventFragment() // on unknown value or "event", launch EventFragment.kt
    }
}