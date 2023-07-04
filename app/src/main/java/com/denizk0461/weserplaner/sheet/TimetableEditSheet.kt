package com.denizk0461.weserplaner.sheet

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.TransitionManager
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.viewBinding
import com.denizk0461.weserplaner.databinding.SheetTimetableEditBinding
import com.denizk0461.weserplaner.fragment.SettingsFragment
import com.denizk0461.weserplaner.model.Timetable
import com.denizk0461.weserplaner.viewmodel.SheetTimetableEditViewModel
import java.io.IOException

class TimetableEditSheet : AppSheet(R.layout.sheet_timetable_edit) {

    // View binding
    private val binding: SheetTimetableEditBinding by viewBinding(SheetTimetableEditBinding::bind)

    // View model
    private val viewModel: SheetTimetableEditViewModel by viewModels()

    private var hasClickedDelete = false

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

        // Retrieve event from arguments
        val timetableId = arguments?.getInt("timetableId") ?: throw IOException("asdf wtf?") // TODO better error handling

        val timetable = viewModel.getTimetableForId(timetableId)

        binding.editTextTitle.setText(timetable.name)

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonDelete.setOnClickListener {
            /*
             * Check if the user has already clicked the button before; this is done to prevent
             * accidentally deleting an event.
             */
            if (hasClickedDelete) {
                viewModel.setSelectedTimetable(viewModel.getLargestTimetableId())

                viewModel.deleteTimetable(timetableId)

                // Tell the user that the event has been deleted
                parentFragment?.showSnackBar(
                    getString(R.string.settings_timetable_sheet_confirm_delete)
                )

                // Dismiss the sheet upon deletion
                dismiss()
            } else {
                // Update the text to show the user that they clicked the button once
                TransitionManager
                    .beginDelayedTransition(binding.buttonContainer as ViewGroup)
                binding.buttonDelete.text =
                    getString(R.string.settings_timetable_sheet_delete_confirm)
                hasClickedDelete = true
            }
        }

        binding.buttonSave.setOnClickListener {
            viewModel.updateTimetable(Timetable(timetableId, binding.editTextTitle.text.toString().trim()))

            // Tell the user that the event has been updated
            parentFragment?.showSnackBar(
                getString(R.string.settings_timetable_sheet_confirm_save)
            )

            dismiss()
        }
    }
}