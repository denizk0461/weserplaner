package com.denizk0461.studip.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.denizk0461.studip.adapter.CanteenOfferPageAdapter
import com.denizk0461.studip.databinding.FragmentCanteenBinding
import com.denizk0461.studip.model.CanteenOffer
import com.denizk0461.studip.model.DietaryPreferences
import com.denizk0461.studip.viewmodel.CanteenViewModel
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

        viewPagerAdapter = CanteenOfferPageAdapter(listOf(), 0, getPrefRegex())
        binding.viewPager.adapter = viewPagerAdapter

        createTabLayoutMediator()

        liveData = viewModel.allOffers
        liveData.observe(viewLifecycleOwner) { offers ->

            dates = offers.map { it.date }.distinct()
            createTabLayoutMediator()

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
        viewPagerAdapter.refreshView(getPrefRegex())
    }

    private fun getPreference(pref: DietaryPreferences): Boolean =
        viewModel.getPreference(pref)

    private fun getPrefRegex(): Regex =
        Regex(viewModel.getDietaryPrefs().deconstruct().replace('t', '.'))
}