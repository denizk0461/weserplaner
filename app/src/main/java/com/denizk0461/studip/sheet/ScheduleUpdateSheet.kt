package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.fragment.app.FragmentActivity
import com.denizk0461.studip.R
import com.denizk0461.studip.data.parseToMinutes
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetScheduleUpdateBinding
import com.denizk0461.studip.dialog.TimePickerFragment
import com.denizk0461.studip.model.StudIPEvent

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

        binding.editTextTitle.setText(event.title)
        binding.editTextLecturers.setText(event.lecturer)
        binding.editTextRoom.setText(event.room)

        binding.buttonTimeStart.text = timeslotStart
        binding.buttonTimeStart.setOnClickListener {
            TimePickerFragment(binding.buttonTimeStart.text.toString(), this, true).show((context as FragmentActivity).supportFragmentManager, "timePicker")
        }
        binding.buttonTimeEnd.text = timeslotEnd
        binding.buttonTimeEnd.setOnClickListener {
            TimePickerFragment(binding.buttonTimeEnd.text.toString(), this, false).show((context as FragmentActivity).supportFragmentManager, "timePicker")
        }

        binding.buttonDelete.setOnClickListener {
            onDelete(event)
            dismiss()
        }

        binding.buttonSave.setOnClickListener {
            onUpdate(
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
            dismiss()
        }
    }

    /**
     *
     */
    override fun onTimeSet(hours: Int, minutes: Int, isEventStart: Boolean) {

        val newTimestamp = "$hours:$minutes"

        if ((isEventStart && newTimestamp.parseToMinutes() > timeslotEnd.parseToMinutes()) ||
            (!isEventStart && newTimestamp.parseToMinutes() < timeslotStart.parseToMinutes())) {
            // tell user that the timestamp must be set earlier/later
        } else {

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