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
import com.denizk0461.studip.model.*
import com.denizk0461.studip.viewmodel.CanteenViewModel
import com.google.android.material.tabs.TabLayoutMediator

class CanteenFragment : Fragment() {

    private var _binding: FragmentCanteenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var liveData: LiveData<List<CanteenOffer>>

    private lateinit var viewPagerAdapter: CanteenOfferPageAdapter
    private val viewModel: CanteenViewModel by viewModels()


    private var elements: List<CanteenOffer> = listOf()
    private var dateSize = 0

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

        createTabLayoutMediator(listOf())

        liveData = viewModel.allOffers
        liveData.observe(viewLifecycleOwner) { offers ->
            elements = offers

            val groupedElements = offers.groupElements().distinct()

//            dates = groupedElements.map { it.date }.distinct()
            val newDates = viewModel.getDates()
            createTabLayoutMediator(newDates)

            dateSize = newDates.size

            viewPagerAdapter.setNewItems(groupedElements, dateSize)

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

    private fun createTabLayoutMediator(dates: List<OfferDate>) {
        if (dates.isNotEmpty()) {
            TabLayoutMediator(binding.dayTabLayout, binding.viewPager) { tab, position ->
                tab.text = dates[position].date
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setPreference(pref: DietaryPreferences, newValue: Boolean) {
        viewModel.setPreference(pref, newValue)
        viewPagerAdapter.setNewItems(elements.groupElements().distinct(), dateSize)
    }

    private fun getPreference(pref: DietaryPreferences): Boolean =
        viewModel.getPreference(pref)

    private fun getPrefRegex(): Regex {
        val prefs = viewModel.getDietaryPrefs().deconstruct()//.replace('f', '.')

        val template = ".........."

        return if (prefs == "..........") {
            Regex(template)
        } else {
            var regexString = ""

            val indices = mutableListOf<Int>()
            prefs.forEachIndexed { index, c ->
                if (c == 't') indices.add(index)
            }
            var isFirst = true
            indices.forEach { index ->
                if (!isFirst) regexString += '|'
                regexString += template.substring(0 until index) + 't' + template.substring(index+1)
                isFirst = false
            }
            Regex(regexString)
        }
    }

    private fun List<CanteenOffer>.groupElements(): List<CanteenOfferGroup> {

        val prefsRegex = getPrefRegex()

        val filteredForPreferences = if (prefsRegex.toString() == "ffffffffff") {
            // show all elements and skip filtering
            this
        } else {
            this.filter {
                prefsRegex.matches(it.dietaryPreferences)
            }
        }

        val b = filteredForPreferences.map { offer ->
            CanteenOfferGroup(
                offer.date,
                offer.dateId,
                offer.category,
                offer.canteen,
                filteredForPreferences.filter {
                    it.category == offer.category && it.date == offer.date && it.canteen == offer.canteen
                }.map {
                    CanteenOfferGroupElement(
                        it.title,
                        it.price,
                        it.dietaryPreferences,
                    )
                }
            )
        }
        return b
    }
}