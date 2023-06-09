package com.denizk0461.weserplaner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.showSnackBar
import com.denizk0461.weserplaner.databinding.FragmentRoomFinderBinding
import com.denizk0461.weserplaner.viewmodel.RoomFinderViewModel

/**
 * Fragment view that the user can use to find specific rooms on the university campus.
 */
class RoomFinderFragment : AppFragment<FragmentRoomFinderBinding>() {

    // View model reference for providing access to the database
    private val viewModel: RoomFinderViewModel by viewModels()

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRoomFinderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tell the user that this fragment's functionality is not yet implemented
        context.theme.showSnackBar(
            binding.coordinatorLayout,
            getString(R.string.room_finder_snack_unimplemented),
        )
    }
}