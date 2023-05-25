package com.denizk0461.weserplaner.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.alamkanak.weekview.WeekViewEvent
import com.denizk0461.weserplaner.databinding.ActivityTimetableOverviewBinding
import com.denizk0461.weserplaner.viewmodel.TimetableOverviewViewModel
import java.util.Calendar
import java.util.Date

/**
 * This activity serves the user in displaying a full week overview of their timetable. This can be
 * used to quickly share their timetable with other people, in screenshot form, for example, as
 * [com.denizk0461.weserplaner.fragment.EventFragment] only displays one day at a time.
 */
class TimetableOverviewActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityTimetableOverviewBinding

    // View model
    private lateinit var viewModel: TimetableOverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Instantiate view model
        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(TimetableOverviewViewModel::class.java)

        // Inflate view binding and bind to this activity
        binding = ActivityTimetableOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide system UI elements to maximise space for the user's timetable
        hideSystemUi()

        val a = WeekViewEvent(0, "hi", createCalendarDate(), createCalendarDate(true))

        viewModel.getAllEvents().observe(this) { events ->
            binding.weekView.weekViewLoader
        }

        // TODO pay attention to whether content is hidden behind camera cutouts
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun createCalendarDate(a: Boolean = false) = Calendar.getInstance().also {
        it.time = Date(if (!a) 1684792800 else 1684800000) }
}