package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.adapter.StudIPEventItemAdapter
import com.denizk0461.studip.databinding.RecyclerViewBinding
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.sheet.ScheduleUpdateSheet
import com.denizk0461.studip.viewmodel.EventPageViewModel

/**
 * Fragment that is instantiated by [StudIPEventPageAdapter] to display individual days' pages and
 * their events.
 *
 * @param currentDay    current day that is used to show only events of a given day (0 = Monday,
 *                      4 = Friday)
 */
class EventPageFragment(
    private val currentDay: Int,
) : AppFragment(), StudIPEventItemAdapter.OnClickListener {

    // Nullable view binding reference
    private var _binding: RecyclerViewBinding? = null

    /*
     * Non-null reference to the view binding. This property is only valid between onCreateView and
     * onDestroyView.
     */
    private val binding get() = _binding!!

    // View model reference for providing access to the database
    private val viewModel: EventPageViewModel by viewModels()

    /**
     * Adapter that manages the page's items
     */
    private val eventAdapter = StudIPEventItemAdapter(currentDay, this)

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

        // Set up LiveData observer to refresh the view on update
        viewModel.getEventsForDay(currentDay).observe(viewLifecycleOwner) { events ->

            eventAdapter.setNewData(events)
        }
    }

    override fun onClick(event: StudIPEvent) {
        // TODO implement functionality or delete
    }

    override fun onLongClick(event: StudIPEvent): Boolean {
        // Open a bottom sheet to edit the event
        openBottomSheet(ScheduleUpdateSheet(event, onUpdate = { eventToUpdate ->
            viewModel.update(eventToUpdate)
        }, onDelete = { eventToDelete ->
            viewModel.delete(eventToDelete)
        }))
        return true
    }
}