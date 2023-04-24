package com.denizk0461.studip.dialog

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker

class TimePickerFragment(
    private val timestamp: String,
    private val listener: OnTimeSetListener,
    private val isEventStart: Boolean,
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timestampSplit = timestamp.split(":")
        return TimePickerDialog(activity, this, timestampSplit[0].toInt(), timestampSplit[1].toInt(), true)
    }

    interface OnTimeSetListener {
        fun onTimeSet(hours: Int, minutes: Int, isEventStart: Boolean)
    }

    override fun onTimeSet(picker: TimePicker?, hours: Int, minutes: Int) {
        listener.onTimeSet(hours, minutes, isEventStart)
    }
}