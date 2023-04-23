package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.denizk0461.studip.R
import com.denizk0461.studip.adapter.StudIPEventItemAdapter
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.databinding.FragmentEventBinding
import com.denizk0461.studip.model.StudIPEvent
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

    // Used to determine the current day
    private var dayOfWeek: Int = 0

    // Titles for the view pager's tabs
    private val dayStrings = listOf(
        R.string.monday,
        R.string.tuesday,
        R.string.wednesday,
        R.string.thursday,
        R.string.friday,
    )

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
         * Determine the current day.
         * TODO expand to weekend
         */
        dayOfWeek = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            else -> 0
        }

        // Set up the view pager's adapter
        viewPagerAdapter = StudIPEventPageAdapter(listOf(), object : StudIPEventItemAdapter.OnClickListener {
            override fun onClick(event: StudIPEvent) {
                // TODO implement functionality or delete
            }
            override fun onLongClick(event: StudIPEvent): Boolean {
                // TODO implement functionality or delete
                return false
            }
        })

        // Assign the adapter to the view pager
        binding.viewPager.adapter = viewPagerAdapter

        // Create and attach the object mediating the tabs for the view pager
        TabLayoutMediator(binding.dayTabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(dayStrings[position])
        }.attach()

        // Set up LiveData observer to refresh the view on update
        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            // Update the item list in the view pager's adapter
            viewPagerAdapter.setNewItems(events)

            // Scroll to the current day
            switchToCurrentDayView()
        }
    }

    override fun onResume() {
        super.onResume()

        // Scroll to the current day
        switchToCurrentDayView()
    }

    // Invalidate the view binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Scroll to the current day. Current day is determined after the fragment's view has been
     * created.
     */
    private fun switchToCurrentDayView() {
        binding.viewPager.currentItem = dayOfWeek
    }
}