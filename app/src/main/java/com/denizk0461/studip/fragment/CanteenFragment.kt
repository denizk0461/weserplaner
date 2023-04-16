package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.denizk0461.studip.adapter.CanteenOfferPageAdapter
import com.denizk0461.studip.databinding.FragmentCanteenBinding
import com.denizk0461.studip.viewmodel.CanteenViewModel
import com.google.android.material.tabs.TabLayoutMediator

class CanteenFragment : Fragment() {

    private var _binding: FragmentCanteenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var dates = listOf<String>()

    private lateinit var viewPagerAdapter: CanteenOfferPageAdapter
    private val viewModel: CanteenViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCanteenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = CanteenOfferPageAdapter(listOf(), 0)
        binding.viewPager.adapter = viewPagerAdapter

        createTabLayoutMediator()

        viewModel.allOffers.observe(viewLifecycleOwner) { offers ->

            dates = offers.map { it.date }.distinct()
            createTabLayoutMediator()

            viewPagerAdapter.setNewItems(offers, dates.size)

            // TODO this must be changed. if an exception is raised, then this will not fire
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchOffers {
//                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun createTabLayoutMediator() {
        if (dates.isNotEmpty()) {
            TabLayoutMediator(binding.dayTabLayout, binding.viewPager) { tab, position ->
                tab.text = dates[position]
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}