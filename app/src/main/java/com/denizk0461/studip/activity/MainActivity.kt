package com.denizk0461.studip.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.denizk0461.studip.R
import com.denizk0461.studip.data.Dependencies
import com.denizk0461.studip.databinding.ActivityMainBinding
import com.denizk0461.studip.db.EventRepository
import com.denizk0461.studip.fragment.EventFragment
import com.denizk0461.studip.fragment.FoodFragment
import com.denizk0461.studip.fragment.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var currentFragment = FragmentType.SCHEDULE
    private val fragments = listOf(EventFragment(), FoodFragment(), SettingsFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        Dependencies.repo = EventRepository(application)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contentMain.navView.setOnItemSelectedListener { item ->
            loadFragment(bottomIdToType(item.itemId))
        }

        loadFragment(currentFragment)

//        setSupportActionBar(binding.toolbar)

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_FirstFragment_to_SecondFragment)
////            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                .setAnchorView(R.id.fab)
////                .setAction("Action", null).show()
//        }
    }

    private fun loadFragment(type: FragmentType): Boolean {
//        val fragment = supportFragmentManager.findFragmentByTag(type.type) ?: getFragmentByType(type)
        val fragment = getFragment(type)

        if (fragment.lifecycle.currentState != Lifecycle.State.INITIALIZED) {
            return false
        }

//        if (supportFragmentManager.fragments.size > 1) {
//
//            val currentFragment = supportFragmentManager.findFragmentByTag(currentFragment.type)
//            if (currentFragment != null) {
//                supportFragmentManager.beginTransaction().remove(currentFragment).commit()
//                supportFragmentManager.popBackStack()
//            }
//        }

//        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, fragment, type.type)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        binding.contentMain.appTitleBar.text = getTitleString(type)

        currentFragment = type

        return true
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

//    private fun getFragmentByType(type: FragmentType): Fragment {
////        Log.d("AAA!", "help")
//        return when (type) {
//            FragmentType.SCHEDULE -> EventFragment()
//            FragmentType.FOOD -> FoodFragment()
//            FragmentType.SETTINGS -> SettingsFragment()
//        }
//    }

    private fun bottomIdToType(id: Int): FragmentType = when (id) {
        R.id.food -> FragmentType.FOOD
        R.id.settings -> FragmentType.SETTINGS
        else -> FragmentType.SCHEDULE
    }

    private fun getFragment(type: FragmentType) = when (type) {
        FragmentType.SCHEDULE -> fragments[0]
        FragmentType.FOOD -> fragments[1]
        FragmentType.SETTINGS -> fragments[2]
    }

    private fun getTitleString(type: FragmentType) = when (type) {
        FragmentType.SCHEDULE -> getString(R.string.title_schedule)
        FragmentType.FOOD -> getString(R.string.title_food)
        FragmentType.SETTINGS -> getString(R.string.title_settings)
    }

    enum class FragmentType(val type: String) {
        SCHEDULE("schedule"),
        FOOD("food"),
        SETTINGS("settings"),
    }
}