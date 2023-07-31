package com.denizk0461.weserplaner.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.viewBinding
import com.denizk0461.weserplaner.databinding.SheetTimetableAddBinding
import com.denizk0461.weserplaner.fragment.SettingsFragment
import com.denizk0461.weserplaner.model.Timetable
import com.denizk0461.weserplaner.viewmodel.SheetTimetableAddViewModel

class TimetableAddSheet : AppSheet(R.layout.sheet_timetable_add) {

    // View binding
    private val binding: SheetTimetableAddBinding by viewBinding(SheetTimetableAddBinding::bind)

    // View model
    private val viewModel: SheetTimetableAddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up FAB behaviour for parent fragment's FAB if activity is not null
        val parentFragment = activity?.let { activity ->
            // Retrieve parent fragment to access its functions
            val navHostFragment = activity
                .supportFragmentManager
                .fragments[0] as NavHostFragment
            navHostFragment
                .childFragmentManager
                .primaryNavigationFragment as SettingsFragment
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonSave.setOnClickListener {
            viewModel.insertTimetable(Timetable(name = binding.editTextTitle.text.toString().trim()))

            // Tell the user that the event has been updated
            parentFragment?.showSnackBar(
                getString(R.string.settings_timetable_sheet_confirm_add)
            )

            dismiss()
        }
    }
}