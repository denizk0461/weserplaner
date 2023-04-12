package com.denizk0461.studip.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.denizk0461.studip.R
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.databinding.FragmentEventBinding
import com.denizk0461.studip.viewmodel.EventViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerViewAdapter: StudIPEventPageAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var dayOfWeek: Int = 0
    private val pagerSnapHelper = PagerSnapHelper()
    private var isUserScrolling = false

    private val viewModel: EventViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayOfWeek = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            else -> 0
        }

        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            recyclerViewAdapter = StudIPEventPageAdapter(events)//DummyData.events.toList())
            binding.recyclerView.adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.onFlingListener = null
            pagerSnapHelper.attachToRecyclerView(binding.recyclerView)
//            binding.recyclerView.scheduleLayoutAnimation()

            switchToCurrentDayView()
        }

        binding.fab.setOnClickListener { view ->
            launchWebview()
        }

        binding.recyclerView.addOnScrollListener(object : OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.dayTabs.getTabAt(layoutManager.findFirstVisibleItemPosition())?.select()
                }
            }
        })

        binding.dayTabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                isUserScrolling = false
                binding.recyclerView.smoothScrollToPosition(tab.position)
                Log.d("HELLO4", binding.recyclerView.scrollY.toString())
                 // TODO where to set isUserScrolling back to true?
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

//        binding.daytabmonday.setOnClickListener { binding.recyclerView.scrollToPosition(0) }
//        binding.daytabtuesday.setOnClickListener { binding.recyclerView.scrollToPosition(1) }
//        binding.daytabwednesday.setOnClickListener { binding.recyclerView.scrollToPosition(2) }
//        binding.daytabthursday.setOnClickListener { binding.recyclerView.scrollToPosition(3) }
//        binding.daytabfriday.setOnClickListener { binding.recyclerView.scrollToPosition(4) }

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onResume() {
        super.onResume()
        switchToCurrentDayView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun switchToCurrentDayView() {
        binding.recyclerView.scrollToPosition(dayOfWeek)
        binding.dayTabs.getTabAt(dayOfWeek)?.select()
    }

    private fun launchWebview() {
        findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    /**
     * Highlights the current or next course.
     */
    private fun highlightCurrentCourse() {

    }
}