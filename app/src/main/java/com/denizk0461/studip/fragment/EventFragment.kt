package com.denizk0461.studip.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
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
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var dayOfWeek: Int = 0
    private val dayStrings = listOf(
        R.string.monday,
        R.string.tuesday,
        R.string.wednesday,
        R.string.thursday,
        R.string.friday,
    )

    private lateinit var viewPagerAdapter: StudIPEventPageAdapter
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

        viewPagerAdapter = StudIPEventPageAdapter(listOf(), object : StudIPEventItemAdapter.OnClickListener {
            override fun onClick(event: StudIPEvent) {

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
}