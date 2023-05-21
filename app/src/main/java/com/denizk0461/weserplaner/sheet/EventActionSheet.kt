package com.denizk0461.weserplaner.sheet

import android.os.Bundle
import android.view.View
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.getParcelableCompat
import com.denizk0461.weserplaner.data.viewBinding
import com.denizk0461.weserplaner.databinding.SheetEventActionBinding
import com.denizk0461.weserplaner.fragment.EventPageFragment
import com.denizk0461.weserplaner.model.StudIPEvent

/**
 * Sheet used to display actions available for a given event to the user.
 */
class EventActionSheet : AppSheet(R.layout.sheet_event_action) {

    // View binding
    private val binding: SheetEventActionBinding by viewBinding(SheetEventActionBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve event from arguments
        val event = arguments.getParcelableCompat<StudIPEvent>("event")

        // Set header text
        binding.eventHeader.text = event.title

        // Set up dismiss button
        binding.buttonDismiss.setOnClickListener {
            // Do nothing and dismiss the sheet
            dismiss()
        }

        // Set up button to edit the event
        binding.buttonEditEvent.setOnClickListener {
            // Open editing sheet
            openBottomSheet(
                ScheduleUpdateSheet().also { sheet ->
                    val bundle = Bundle()
                    bundle.putBoolean("isEditing", true)
                    bundle.putParcelable("event", event)
                    sheet.arguments = bundle
                }
            )

            // Close this sheet
            dismiss()
        }

        // Set up button to delete the event
        binding.buttonDeleteEvent.setOnClickListener {
            // Open deletion confirm sheet
            openBottomSheet(
                EventConfirmDeletionSheet().also { sheet ->
                    val bundle = Bundle()
                    bundle.putParcelable("event", event)
                    sheet.arguments = bundle
                }
            )

            // Show a snack bar in the parent fragment to tell the user that the action was successful
            (parentFragment as EventPageFragment)
                .showSnackBar(getString(R.string.sheet_event_confirm_deletion_snack))

            // Close this sheet
            dismiss()
        }
    }
}