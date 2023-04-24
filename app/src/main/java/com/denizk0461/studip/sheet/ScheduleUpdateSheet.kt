package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
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

        binding.apply {
            editTextTitle.setText(event.title)
            editTextLecturers.setText(event.lecturer)
            editTextRoom.setText(event.room)
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
        if (isEventStart) {
            timeslotStart = "$hours:$minutes"
        } else {
            timeslotEnd = "$hours:$minutes"
        }
    }
}