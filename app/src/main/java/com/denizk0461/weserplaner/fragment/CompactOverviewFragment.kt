package com.denizk0461.weserplaner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.databinding.FragmentCompactOverviewBinding

class CompactOverviewFragment : AppFragment<FragmentCompactOverviewBinding>() {

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCompactOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_compact_overview_to_settings)
        }
    }
}