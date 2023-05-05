package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.denizk0461.studip.R
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.databinding.FragmentEventBinding
import com.denizk0461.studip.sheet.ScheduleUpdateSheet
import com.denizk0461.studip.viewmodel.EventViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

/**
 * User-facing fragment view that displays the user's Stud.IP schedule.
 */
class EventFragment : AppFragment() {

    // Nullable view binding reference
    private var _binding: FragmentEventBinding? = null

    /*
     * Non-null reference to the view binding. This property is only valid between onCreateView and
     * onDestroyView.
     */
    private val binding get() = _binding!!

    // Adapter for the view pager displaying all events
    private lateinit var viewPagerAdapter: StudIPEventPageAdapter

    // View model reference for providing access to the database
    private val viewModel: EventViewModel by viewModels()

    // Titles for the view pager's tabs
    private lateinit var weekdays: Array<String>

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get localised weekday names
        weekdays = context.resources?.getStringArray(R.array.weekdays) ?: arrayOf()

        // Set up the view pager's adapter
        viewPagerAdapter = StudIPEventPageAdapter(
            childFragmentManager,
            lifecycle,
        )

        // Assign the adapter to the view pager
        binding.viewPager.adapter = viewPagerAdapter

        // Create and attach the object mediating the tabs for the view pager
        TabLayoutMediator(binding.dayTabLayout, binding.viewPager) { tab, position ->
            tab.text = weekdays[position]
        }.attach()

        // Set the view pager's current page to the current day, if the user chose this option
        if (viewModel.preferenceCurrentDay) {

            // Determine the current day and go to the respective page.
            binding.viewPager.setCurrentItem(
                when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                    Calendar.TUESDAY -> 1
                    Calendar.WEDNESDAY -> 2
                    Calendar.THURSDAY -> 3
                    Calendar.FRIDAY -> 4
                    Calendar.SATURDAY -> 5
                    Calendar.SUNDAY -> 6
                    else -> 0 // assume Monday
                }, false
            )
        }

        // Set up button for adding a new event
        binding.fabAddEvent.setOnClickListener {
            /*
             * Open the bottom sheet used for editing an event, but tell the sheet that it will be
             * used for creating a new event instead.
             */
            openBottomSheet(
                ScheduleUpdateSheet().also { sheet ->
                    val bundle = Bundle()
                    bundle.putBoolean("isEditing", false)
                    sheet.arguments = bundle
                }
            )
        }

        // Show the user a little message if they're opening the app for the first time
        if (viewModel.preferenceFirstLaunch) {

            // Show toast
//            showToast(context, getString(R.string.toast_first_launch))

            // Remember that the app has been launched before
            viewModel.preferenceFirstLaunch = false
        }
    }

    // Invalidate the view binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}