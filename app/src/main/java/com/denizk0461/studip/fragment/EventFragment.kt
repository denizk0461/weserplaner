package com.denizk0461.studip.fragment

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.denizk0461.studip.R
import com.denizk0461.studip.adapter.StudIPEventItemAdapter
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.databinding.FragmentEventBinding
import com.denizk0461.studip.model.StudIPEvent
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

    // Used to determine the current day
    private var dayOfWeek: Int = 0

    // Titles for the view pager's tabs
    private lateinit var weekdays: Array<String>

//    private var viewPagerScrollPosition: Parcelable? = null
    private var viewPagerPosition = -1

    private var hasFragmentStarted = false

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weekdays = resources.getStringArray(R.array.weekdays)

        // Determine the current day
        dayOfWeek = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> 0 // assume Monday
        }

        // Set up the view pager's adapter
        viewPagerAdapter =
            StudIPEventPageAdapter(activity as FragmentActivity, listOf(), object : StudIPEventItemAdapter.OnClickListener {
                override fun onClick(event: StudIPEvent) {
                    // TODO implement functionality or delete
                }
                override fun onLongClick(event: StudIPEvent): Boolean {
                    // Save the current page of the ViewPager
                    viewPagerPosition = binding.viewPager.currentItem

                    // Open a bottom sheet to edit the event
                    openBottomSheet(ScheduleUpdateSheet(event, onUpdate = { eventToUpdate ->
                        viewModel.update(eventToUpdate)
                    }, onDelete = { eventToDelete ->
                        viewModel.delete(eventToDelete)
                    }))
                    return true
                }
            })

        // Assign the adapter to the view pager
        binding.viewPager.adapter = viewPagerAdapter

        // Create and attach the object mediating the tabs for the view pager
        TabLayoutMediator(binding.dayTabLayout, binding.viewPager) { tab, position ->
            tab.text = weekdays[position]
        }.attach()

        // Set up LiveData observer to refresh the view on update
        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            // Update the item list in the view pager's adapter
            viewPagerAdapter.setNewItems(events)

            if (!hasFragmentStarted) {
                // Scroll to the current day, if no page has been stored to be scrolled to
                switchToCurrentDayView()
                hasFragmentStarted = true
//            } else {
//                /*
//                 * If a page was previously saved, scroll to that one instead. This is meant to
//                 * prevent jumping from
//                 */
//                binding.viewPager.currentItem = viewPagerPosition
            }
        }
    }

    override fun onPause() {
//        viewPagerPosition = binding.viewPager.currentItem
//        Log.d("AAA?", "onpause: $viewPagerPosition")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.viewPager.currentItem = viewPagerPosition
        Log.d("AAA?", "onresume: pos: $viewPagerPosition; vpp: ${binding.viewPager.currentItem}")

        // Scroll to the current day
//        switchToCurrentDayView()
    }

    /**
     * Called when the layout changes (e.g. device rotation) but NOT when the fragment is selected
     * from the bottom navigation view.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("AAA?", "${binding.viewPager.currentItem}")
        outState.putInt(
            "viewPagerCurrentPage",
            binding.viewPager.currentItem
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val restoredPage = savedInstanceState?.getInt("viewPagerCurrentPage") ?: 0
        viewPagerPosition = restoredPage

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