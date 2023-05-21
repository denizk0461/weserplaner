package com.denizk0461.weserplaner.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.getParcelableCompat
import com.denizk0461.weserplaner.data.viewBinding
import com.denizk0461.weserplaner.databinding.SheetEventConfirmDeletionBinding
import com.denizk0461.weserplaner.model.StudIPEvent
import com.denizk0461.weserplaner.viewmodel.EventConfirmDeletionViewModel

/**
 * Sheet displayed to the user to confirm that they want to delete an event from their timetable.
 */
class EventConfirmDeletionSheet : AppSheet(R.layout.sheet_event_confirm_deletion) {

    // View model
    private val viewModel: EventConfirmDeletionViewModel by viewModels()

    // View binding
    private val binding: SheetEventConfirmDeletionBinding by viewBinding(SheetEventConfirmDeletionBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve event from arguments
        val event = arguments.getParcelableCompat<StudIPEvent>("event")

        // Set up dismiss button
        binding.buttonDismiss.setOnClickListener {
            // Do nothing and dismiss the sheet
            dismiss()
        }

        // Set up button to delete the event
        binding.buttonDelete.setOnClickListener {
            // Delete the event
            viewModel.delete(event)

            // Dismiss the sheet
            dismiss()
        }

        // Set up cancel button
        binding.buttonCancel.setOnClickListener {
            // Do nothing and dismiss the sheet
            dismiss()
        }

        // Set text content with the event's title inserted
        binding.textConfirmDeletionContent.text = getString(
            R.string.sheet_event_confirm_deletion_content,
            event.title,
        )
    }
}