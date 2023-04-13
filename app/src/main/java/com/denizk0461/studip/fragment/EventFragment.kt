package com.denizk0461.studip.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.denizk0461.studip.R
import com.denizk0461.studip.activity.FetcherActivity
import com.denizk0461.studip.adapter.StudIPEventAdapter
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.databinding.FragmentEventBinding
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.viewmodel.EventViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: StudIPEventPageAdapter
    private var dayOfWeek: Int = 0
    private val dayStrings = listOf(
        R.string.monday,
        R.string.tuesday,
        R.string.wednesday,
        R.string.thursday,
        R.string.friday,
    )

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

        viewPagerAdapter = StudIPEventPageAdapter(listOf(), object : StudIPEventAdapter.OnClickListener {
            override fun onClick(event: StudIPEvent) {
                // do nothing
            }
            override fun onLongClick(event: StudIPEvent) {

            }
        })//DummyData.events.toList())
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.dayTabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(dayStrings[position])
        }.attach()

        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            viewPagerAdapter.setNewItems(events)
            switchToCurrentDayView()
        }

        binding.fab.setOnClickListener { view ->
            launchWebview()
        }

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
        binding.viewPager.currentItem = dayOfWeek
//        binding.dayTabLayout.getTabAt(dayOfWeek)?.select()
    }

    private fun launchWebview() {
//        findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment)
        startActivity(Intent(context, FetcherActivity::class.java))
    }

    /**
     * Highlights the current or next course.
     */
    private fun highlightCurrentCourse() {

    }
}