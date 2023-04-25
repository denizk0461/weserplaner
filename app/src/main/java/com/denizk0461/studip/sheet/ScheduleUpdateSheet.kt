package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.denizk0461.studip.R
import com.denizk0461.studip.data.parseToMinutes
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetScheduleUpdateBinding
import com.denizk0461.studip.dialog.TimePickerFragment
import com.denizk0461.studip.model.StudIPEvent

/**
 * Update sheet to allow the user to edit attributes for a specific schedule event.
 *
 * @param event     event to be updated
 * @param onUpdate  action to execute to update the event
 * @param onDelete  action to execute to delete the event
 */
class ScheduleUpdateSheet(
    private val event: StudIPEvent,
    private val onUpdate: (event: StudIPEvent) -> Unit,
    private val onDelete: (event: StudIPEvent) -> Unit,
) : AppSheet(R.layout.sheet_schedule_update), TimePickerFragment.OnTimeSetListener {

    // Editable fields
    private var timeslotStart = event.timeslotStart
    private var timeslotEnd = event.timeslotEnd
    private var colour = event.colour

    // View binding
    private val binding: SheetScheduleUpdateBinding by viewBinding(SheetScheduleUpdateBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set editable text fields
        binding.editTextTitle.setText(event.title)
        binding.editTextLecturers.setText(event.lecturer)
        binding.editTextRoom.setText(event.room)

        // Prepare timestamp buttons
        binding.buttonTimeStart.text = timeslotStart
        binding.buttonTimeStart.setOnClickListener {
            TimePickerFragment(
                binding.buttonTimeStart.text.toString(),
                this,
                true,
            ).show((context as FragmentActivity).supportFragmentManager, "timePicker")
        }
        binding.buttonTimeEnd.text = timeslotEnd
        binding.buttonTimeEnd.setOnClickListener {
            TimePickerFragment(
                binding.buttonTimeEnd.text.toString(),
                this,
                false,
            ).show((context as FragmentActivity).supportFragmentManager, "timePicker")
        }

        /*
         * TODO implement option to change the colour for the course - single-selection coloured
         *  filter buttons with checkmarks when they're selected maybe?
         */

        // Prepare cancel button
        binding.buttonCancel.setOnClickListener {
            // Do nothing and dismiss the sheet
            dismiss()
        }

        // Prepare delete button
        binding.buttonDelete.setOnClickListener {
            onDelete(event)
            // Dismiss the sheet upon deletion
            dismiss()
        }

        // Prepare update/save button
        binding.buttonSave.setOnClickListener {
            onUpdate(
                // construct new StudIPEvent from the data the user may have edited
                StudIPEvent(
                    id = event.id,
                    title = binding.editTextTitle.text.toString(),
                    lecturer = binding.editTextLecturers.text.toString(),
                    room = binding.editTextRoom.text.toString(),
                    day = event.day,
                    timeslotStart = timeslotStart,
                    timeslotEnd = timeslotEnd,
                    timeslotId = timeslotStart.parseToMinutes(),
                    colour = colour,
                )
            )
            // Dismiss the sheet upon update
            dismiss()
        }
    }

    /**
     * Checks and stores a new timestamp after the user picks one through [TimePickerFragment].
     *
     * @param hours         newly set hour
     * @param minutes       newly set minute
     * @param isEventStart  whether this timestamp is for the start of the course
     */
    override fun onTimeSet(hours: Int, minutes: Int, isEventStart: Boolean) {
        // Parse the separate ints into a timestamp string
        val newTimestamp = "$hours:$minutes"

        // If start > end, show an error
        if ((isEventStart && newTimestamp.parseToMinutes() > timeslotEnd.parseToMinutes()) ||
            (!isEventStart && newTimestamp.parseToMinutes() < timeslotStart.parseToMinutes())) {
            // TODO tell user that the timestamp must be set earlier/later
        } else {
            // If the new timestamp is valid, save it
            if (isEventStart) {
                timeslotStart = newTimestamp
                binding.buttonTimeStart.text = newTimestamp
            } else {
                timeslotEnd = newTimestamp
                binding.buttonTimeEnd.text = newTimestamp
            }
        }
    }
}