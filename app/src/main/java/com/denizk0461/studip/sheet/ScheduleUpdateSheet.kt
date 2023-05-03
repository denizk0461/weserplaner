package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.transition.TransitionManager
import com.denizk0461.studip.R
import com.denizk0461.studip.data.getParcelableCompat
import com.denizk0461.studip.data.getTimestampAcademicQuarterEnd
import com.denizk0461.studip.data.getTimestampAcademicQuarterStart
import com.denizk0461.studip.data.parseToMinutes
import com.denizk0461.studip.data.showToast
import com.denizk0461.studip.data.timeslotsForAcademicQuarter
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetScheduleUpdateBinding
import com.denizk0461.studip.dialog.TimePickerFragment
import com.denizk0461.studip.exception.AcademicQuarterNotApplicableException
import com.denizk0461.studip.exception.ParcelNotFoundException
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.viewmodel.ScheduleUpdateViewModel

/**
 * Update sheet to allow the user to edit attributes for a specific schedule event.
 */
class ScheduleUpdateSheet : AppSheet(R.layout.sheet_schedule_update), TimePickerFragment.OnTimeSetListener {

    // Editable fields
    private var timeslotStart = ""
    private var timeslotEnd = ""
    private var colour = -1

    // View binding
    private val binding: SheetScheduleUpdateBinding by viewBinding(SheetScheduleUpdateBinding::bind)

    // View model
    private val viewModel: ScheduleUpdateViewModel by viewModels()

    // Whether the user has once clicked the delete button; used to prevent an accidental click
    private var hasClickedDelete: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            // Retrieve event from arguments bundle
            val event: StudIPEvent = arguments.getParcelableCompat("event")

            // Set values of the text field to that of the event
            timeslotStart = event.timeslotStart
            timeslotEnd = event.timeslotEnd
            colour = event.colour

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
                    showToast(
                        context,
                        getString(R.string.sheet_schedule_update_hint_quarter_error)
                    )
                }
            }

            binding.buttonAcademicQuarter.visibility = getVisibilityForAcademicQuarter(
                timeslotsForAcademicQuarter.contains(timeslotStart),
                timeslotsForAcademicQuarter.contains(timeslotEnd),
            )

            // Prepare timestamp buttons
            binding.buttonTimeStart.text = timeslotStart
            binding.buttonTimeStart.setOnClickListener {
                // Launch a time picker dialogue to pick a new start timestamp
                TimePickerFragment(
                    this,
                    true,
                ).also { sheet ->
                    val bundle = Bundle()
                    bundle.putString("timestamp", binding.buttonTimeStart.text.toString())
                    sheet.arguments = bundle
                }.show((context as FragmentActivity).supportFragmentManager, "timePicker")
            }
            binding.buttonTimeEnd.text = timeslotEnd
            binding.buttonTimeEnd.setOnClickListener {
                // Launch a time picker dialogue to pick a new end timestamp
                TimePickerFragment(
                    this,
                    false,
                ).also { sheet ->
                    val bundle = Bundle()
                    bundle.putString("timestamp", binding.buttonTimeEnd.text.toString())
                    sheet.arguments = bundle
                }.show((context as FragmentActivity).supportFragmentManager, "timePicker")
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
                /*
                 * Check if the user has already clicked the button before; this is done to prevent
                 * accidentally deleting an event.
                 */
                if (hasClickedDelete) {
                    viewModel.delete(event)
                    // Dismiss the sheet upon deletion
                    dismiss()
                } else {
                    // Update the text to show the user that they clicked the button once
                    TransitionManager.beginDelayedTransition(binding.buttonContainer as ViewGroup)
                    binding.buttonDelete.text = getString(R.string.sheet_schedule_update_delete_confirm)
                    hasClickedDelete = true
                }
            }

            // Prepare update/save button
            binding.buttonSave.setOnClickListener {
                viewModel.update(
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
        } catch (e: ParcelNotFoundException) {
            // Tell the user that something went wrong when retrieving Parcelable
            showToast(context, getString(R.string.event_update_sheet_error))

            // Close the sheet
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

            // Set appropriate visibility to the academic quarter button
            TransitionManager.beginDelayedTransition(binding.sheet.parent as ViewGroup)
            binding.buttonAcademicQuarter.visibility = getVisibilityForAcademicQuarter(
                timeslotsForAcademicQuarter.contains(timeslotStart),
                timeslotsForAcademicQuarter.contains(timeslotEnd),
            )
        }
    }

    /**
     * Determines which visibility the academic quarter button should have given the timestamps.
     *
     * @param isStartValid  whether the starting timestamp can be converted to an academic quarter
     * @param isEndValid    whether the ending timestamp can be converted to an academic quarter
     */
    private fun getVisibilityForAcademicQuarter(isStartValid: Boolean, isEndValid: Boolean): Int =
        if (isStartValid && isEndValid) {
            View.VISIBLE
        } else {
            View.GONE
        }
}