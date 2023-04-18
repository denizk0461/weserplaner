package com.denizk0461.studip.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denizk0461.studip.adapter.CanteenOfferPageAdapter
import com.denizk0461.studip.databinding.FragmentCanteenBinding
import com.denizk0461.studip.model.CanteenOffer
import com.denizk0461.studip.model.DietaryPreferences
import com.denizk0461.studip.viewmodel.CanteenViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator

class CanteenFragment : Fragment() {

    private var _binding: FragmentCanteenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var dates = listOf<String>()

    private lateinit var liveData: LiveData<List<CanteenOffer>>

    private lateinit var viewPagerAdapter: CanteenOfferPageAdapter
    private val viewModel: CanteenViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCanteenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chipMap = mapOf(
            binding.chipPrefFair to DietaryPreferences.FAIR,
            binding.chipPrefFish to DietaryPreferences.FISH,
            binding.chipPrefPoultry to DietaryPreferences.POULTRY,
            binding.chipPrefLamb to DietaryPreferences.LAMB,
            binding.chipPrefVital to DietaryPreferences.VITAL,
            binding.chipPrefBeef to DietaryPreferences.BEEF,
            binding.chipPrefPork to DietaryPreferences.PORK,
            binding.chipPrefVegetarian to DietaryPreferences.VEGETARIAN,
            binding.chipPrefVegan to DietaryPreferences.VEGAN,
            binding.chipPrefGame to DietaryPreferences.GAME,
        )

        chipMap.forEach { (chip, pref) ->
            chip.isChecked = getPreference(pref)
            chip.setOnCheckedChangeListener { buttonView, newValue ->
                setPreference(pref, newValue)
                Log.d("eek!4", "object as : ${viewModel.getDietaryPrefs().deconstruct()}")

                /* Since this removes the view before immediately adding it back, this method causes a
                 * flicker. Note for the future: implement a more efficient / better-looking method.
                 *
                 * TODO order the chips alphabetically
                 */
                val index = binding.chipsPreference.indexOfChild(buttonView)
                binding.chipsPreference.removeView(buttonView)
                binding.chipsPreference.addView(buttonView, index)
            }
        }

//        binding.chipPrefFair
//        binding.chipPrefFair.setOnCheckedChangeListener { _, newValue ->
//            setPreference(DietaryPreferences.FAIR, newValue)
//        }

        viewPagerAdapter = CanteenOfferPageAdapter(listOf(), 0, viewModel.getDietaryPrefs())
        binding.viewPager.adapter = viewPagerAdapter

        createTabLayoutMediator()

        liveData = viewModel.getOffersByPreference()
        liveData.observe(viewLifecycleOwner) { offers ->

            dates = offers.map { it.date }.distinct()
            createTabLayoutMediator()

//            val newOffers = mutableListOf<CanteenOffer>()

//            val prefs = viewModel.getDietaryPrefs()
//            if (prefs.isFair) { newOffers.addAll(offers.filter { it.isFair }) }
//            if (prefs.isFish) { newOffers.addAll(offers.filter { it.isFish }) }
//            if (prefs.isPoultry) { newOffers.addAll(offers.filter { it.isPoultry }) }
//            if (prefs.isLamb) { newOffers.addAll(offers.filter { it.isLamb }) }
//            if (prefs.isVital) { newOffers.addAll(offers.filter { it.isVital }) }
//            if (prefs.isBeef) { newOffers.addAll(offers.filter { it.isBeef }) }
//            if (prefs.isPork) { newOffers.addAll(offers.filter { it.isPork }) }
//            if (prefs.isVegetarian) { newOffers.addAll(offers.filter { it.isVegetarian }) }
//            if (prefs.isVegan) { newOffers.addAll(offers.filter { it.isVegan }) }
//            if (prefs.isGame) { newOffers.addAll(offers.filter { it.isGame }) }
//            newOffers.sortBy { it ->
//                Log.d("eek!4", "title: ${it.title} - id: ${it.itemId}")
//                it.itemId
//            }

            viewPagerAdapter.setNewItems(offers, dates.size)

            // TODO this must be changed. if an exception is raised, then this will not fire
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchOffers(onRefreshUpdate = { status ->
                // TODO refresh updates
            }, onFinish = {
//                binding.swipeRefreshLayout.isRefreshing = false
            })
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

    private fun setPreference(pref: DietaryPreferences, newValue: Boolean) {
        viewModel.setPreference(pref, newValue)
        viewPagerAdapter.refreshView(viewModel.getDietaryPrefs())
//        Log.d("eek!4", "this")
//        liveData.forceRefresh()
//        viewModel.forceRefresh()
        // notifyDataSetChanged?
    }

    private fun getPreference(pref: DietaryPreferences): Boolean =
        viewModel.getPreference(pref)

    private fun MutableLiveData<List<CanteenOffer>>.forceRefresh() {
        this.value = this.value
    }
}