package com.denizk0461.studip.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.denizk0461.studip.R
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.data.DummyData
import com.denizk0461.studip.data.StudIPScraper
import com.denizk0461.studip.databinding.FragmentEventBinding
import com.denizk0461.studip.viewmodel.EventViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerViewAdapter: StudIPEventPageAdapter
    private lateinit var recyclerViewLayoutManager: LinearLayoutManager

    private val viewModel: EventViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            recyclerViewAdapter = StudIPEventPageAdapter(DummyData.events.toList())
            binding.recyclerView.adapter = recyclerViewAdapter
            recyclerViewLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.layoutManager = recyclerViewLayoutManager
            PagerSnapHelper().attachToRecyclerView(binding.recyclerView)
            binding.recyclerView.scheduleLayoutAnimation()
        }

        viewModel.doAsync { StudIPScraper().parse(requireContext()) }

        binding.fab.setOnClickListener { view ->
            launchWebview()
        }

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchWebview() {
        findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
}