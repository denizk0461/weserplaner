package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.adapter.StudIPEventItemAdapter
import com.denizk0461.studip.databinding.RecyclerViewBinding
import com.denizk0461.studip.model.StudIPEvent

/**
 * Fragment that is instantiated by [StudIPEventPageAdapter] to display individual days' pages and
 * their events.
 *
 * @param events            list of all events (still not filtered by day at this point)
 * @param currentDay        current day that is used to show only events of a given day (0 = Monday,
 *                          4 = Friday)
 * @param onClickListener   used for listening to clicks and long presses
 */
class EventPageFragment(
    private val events: List<StudIPEvent>,
    private val currentDay: Int,
    private val onClickListener: StudIPEventItemAdapter.OnClickListener,
) : AppFragment() {

    // Nullable view binding reference
    private var _binding: RecyclerViewBinding? = null

    /*
     * Non-null reference to the view binding. This property is only valid between onCreateView and
     * onDestroyView.
     */
    private val binding get() = _binding!!

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
             * by the newly created adapter (0 = Monday, 4 = Friday)
             */
            adapter = StudIPEventItemAdapter(events, currentDay, onClickListener) // TODO check if empty

            // Animate creation of new page
            scheduleLayoutAnimation()
        }
    }
}