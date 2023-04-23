package com.denizk0461.studip.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.denizk0461.studip.R
import com.denizk0461.studip.data.Dependencies
import com.denizk0461.studip.databinding.ActivityMainBinding
import com.denizk0461.studip.db.AppRepository
import com.denizk0461.studip.fragment.EventFragment
import com.denizk0461.studip.fragment.CanteenFragment
import com.denizk0461.studip.fragment.SettingsFragment

/**
 * Main activity that handles all common fragments. This is opened on app launch.
 */
class MainActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityMainBinding
    /*
     * ID of the bottom navigation view button that launched the currently active fragment; set on
     * activity launch to the fragment that will be first shown
     */
    private var currentFragment: Int = R.id.plan
    // All fragments are instantiated at app launch
    private val fragments: List<Fragment> =
        listOf(EventFragment(), CanteenFragment(), SettingsFragment())

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

        /*
         * Set up navigation view bar to launch fragments.
         * TODO: why do fragments launch slowly when quickly clicking between them?
         */
        binding.contentMain.navView.setOnItemSelectedListener { item ->
            loadFragment(item.itemId)
        }

        // Launch the fragment defined in currentFragment
        loadFragment(currentFragment)
    }

    /**
     * Loads a given fragment.
     *
     * @param id    the ID of the bottom navigation view button that was clicked
     * @return      true on success, false if another fragment is currently instantiating
     */
    private fun loadFragment(id: Int): Boolean {
        // Get fragment to be loaded
        val fragment = getFragment(id)

        // Check if another fragment exists and is not fully initialised
        if (fragment.lifecycle.currentState != Lifecycle.State.INITIALIZED) {
            // Return false to avoid interrupting the fragment's lifecycle
            return false
        }

        // Prepare and commit a transaction between the current and the next fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, fragment, id.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        // Set text of the app bar accordingly
        binding.contentMain.appTitleBar.text = getTitleString(id)
        // Note down the new current fragment
        currentFragment = id

        // Transaction successful
        return true
    }

    /**
     * Retrieve fragment by the ID of the bottom navigation view's button.
     *
     * @param id    the ID of the bottom navigation button
     * @return      the corresponding fragment
     */
    private fun getFragment(id: Int) = when (id) {
        R.id.food -> fragments[1] // CanteenFragment.kt
        R.id.settings -> fragments[2] // SettingsFragment.kt
        else -> fragments[0] // on unknown value or R.id.plan, launch EventFragment.kt
    }

    /**
     * Retrieve app bar text by the ID of the bottom navigation view's button.
     *
     * @param id    the ID of the bottom navigation button
     * @return      the corresponding fragment's title
     */
    private fun getTitleString(id: Int) = when (id) {
        R.id.food -> getString(R.string.title_food)
        R.id.settings -> getString(R.string.title_settings)
        else -> getString(R.string.title_schedule) // on unknown value or R.id.plan, get text for EventFragment.kt
    }
}