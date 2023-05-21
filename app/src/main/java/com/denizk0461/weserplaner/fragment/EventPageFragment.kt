package com.denizk0461.weserplaner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denizk0461.weserplaner.adapter.StudIPEventPageAdapter
import com.denizk0461.weserplaner.adapter.StudIPEventItemAdapter
import com.denizk0461.weserplaner.databinding.RecyclerViewBinding
import com.denizk0461.weserplaner.model.StudIPEvent
import com.denizk0461.weserplaner.sheet.ScheduleUpdateSheet
import com.denizk0461.weserplaner.viewmodel.EventPageViewModel

/**
 * Fragment that is instantiated by [StudIPEventPageAdapter] to display individual days' pages and
 * their events.
 */
class EventPageFragment : AppFragment<RecyclerViewBinding>(), StudIPEventItemAdapter.OnClickListener {

    // View model reference for providing access to the database
    private val viewModel: EventPageViewModel by viewModels()

    /**
     * Adapter that manages the page's items
     */
    private lateinit var eventAdapter: StudIPEventItemAdapter

    /**
     * Current day that is used to show only events of a given day (0 = Monday, 4 = Friday)
     */
    private var currentDay = -1

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Retrieve current day to fetch items specifically for that day
        currentDay = arguments?.getInt("currentDay") ?: -1

        // Instantiate event adapter with the current day
        eventAdapter = StudIPEventItemAdapter(
            currentDay,
            viewModel.preferenceCourseHighlighting,
            this,
        )

        // Inflate view binding
        _binding = RecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            // Set page to scroll vertically
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            /*
             * Create new adapter for every page. Attribute position denotes day that will be set up
             * by the newly created adapter (0 = Monday, 4 = Friday, 6 = Sunday)
             */
            adapter = eventAdapter

            // Animate creation of new page
            scheduleLayoutAnimation()
        }

        // Retrieve parent fragment to access its functions
        val navHostFragment = activity?.supportFragmentManager?.fragments?.get(0) as? NavHostFragment
        val parent = navHostFragment?.childFragmentManager?.primaryNavigationFragment as? EventFragment

        // Set up scroll change listener to shrink and extend FAB accordingly
        binding.recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            // Calculate the vertical scroll difference
            val dy = scrollY - oldScrollY
            if (dy > 0) {
                // If scrolling down, shrink the FAB
                parent?.shrinkFab()
            } else if (dy < 0) {
                // If scrolling up, extend the FAB
                parent?.extendFab()
            }
        }

        // Set up LiveData observer to refresh the view on update
        viewModel.getEventsForDay(currentDay).observe(viewLifecycleOwner) { events ->
            eventAdapter.setNewData(events.ifEmpty { listOf(
                // Use an event with eventId = -1 to denote that there are no events for this day
                StudIPEvent(
                    -1,
                    -1,
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    0,
                    0,
                )
            ) })
        }
    }

    override fun onClick(event: StudIPEvent) {
        // TODO implement functionality or delete
    }

    override fun onLongClick(event: StudIPEvent): Boolean {
        // Open a bottom sheet to edit the event
        openBottomSheet(
            ScheduleUpdateSheet().also { sheet ->
                val bundle = Bundle()
                bundle.putBoolean("isEditing", true)
                bundle.putParcelable("event", event)
                sheet.arguments = bundle
            }
        )
        return true
    }
}