package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.transition.TransitionManager
import com.denizk0461.studip.R
import com.denizk0461.studip.data.getTimestampAcademicQuarterEnd
import com.denizk0461.studip.data.getTimestampAcademicQuarterStart
import com.denizk0461.studip.data.parseToMinutes
import com.denizk0461.studip.data.showToast
import com.denizk0461.studip.data.timeslotsAcademicQuarter
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetScheduleUpdateBinding
import com.denizk0461.studip.dialog.TimePickerFragment
import com.denizk0461.studip.exception.AcademicQuarterNotApplicableException
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

        /*
         * Show academic quarter suggestion if it is applicable. For this to be the case, both the
         * start timestamp and the end timestamp must match up in a pattern that would make it
         * likely for the course to use the academic quarter. Example:
         * 14:00 - 16:00 -> likely 14:15 - 15:45
         *
         * 13:00 - 15:30 -> likely no academic quarter intended
         */
        binding.buttonAcademicQuarter.visibility = if (
            timeslotsAcademicQuarter.contains(timeslotStart) &&
            timeslotsAcademicQuarter.contains(timeslotEnd)
        ) {
            binding.buttonAcademicQuarter.setOnClickListener {
                try {
                    // Retrieve new timestamps with the academic quarter applied, or throw a tantrum
                    val newStart = getTimestampAcademicQuarterStart(timeslotStart)
                    val newEnd = getTimestampAcademicQuarterEnd(timeslotEnd)

                    // Set the buttons to the new timesatmps
                    timeslotStart = newStart
                    binding.buttonTimeStart.text = newStart
                    timeslotEnd = newEnd
                    binding.buttonTimeEnd.text = newEnd

                    /*
                     * Attempt to fix the issue of the bottom sheet jumping up when hiding the
                     * button.
                     */
                    TransitionManager.beginDelayedTransition(binding.sheet.parent as ViewGroup)
                    binding.buttonAcademicQuarter.visibility = View.GONE

                // Catch if a timeslot couldn't be found in the list, and tell the user
                } catch (e: AcademicQuarterNotApplicableException) {
                    showToast(context, getString(R.string.sheet_schedule_update_hint_quarter_error))
                }
            }

            // If the pattern applies, show the button to the user
            View.VISIBLE
        } else {
            // Hide the button if the action is not necessary or applicable
            View.GONE
        }

        // Prepare timestamp buttons
        binding.buttonTimeStart.text = timeslotStart
        binding.buttonTimeStart.setOnClickListener {
            // Launch a time picker dialogue to pick a new start timestamp
            TimePickerFragment(
                binding.buttonTimeStart.text.toString(),
                this,
                true,
            ).show((context as FragmentActivity).supportFragmentManager, "timePicker")
        }
        binding.buttonTimeEnd.text = timeslotEnd
        binding.buttonTimeEnd.setOnClickListener {
            // Launch a time picker dialogue to pick a new end timestamp
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
                    eventId = event.eventId,
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
        // Parse the separate ints into a timestamp string. Add a leading zero if necessary.
        val newTimestamp = "$hours:${String.format("%02d", minutes)}"

        if (isEventStart && newTimestamp.parseToMinutes() > timeslotEnd.parseToMinutes()) {
            // If editing start and start > end, show an error
            showToast(context, getString(R.string.sheet_schedule_update_timestamp_error_start))
            // If editing end and end < start, show an error
        } else if (!isEventStart && newTimestamp.parseToMinutes() < timeslotStart.parseToMinutes()) {
            showToast(context, getString(R.string.sheet_schedule_update_timestamp_error_end))
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