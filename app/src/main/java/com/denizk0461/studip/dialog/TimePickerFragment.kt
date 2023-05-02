package com.denizk0461.studip.dialog

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker

/**
 * Dialogue used for letting the user pick a timestamp. TODO crashes on config change (multitask)
 *
 * @param listener      listener for when the user finishes setting a time
 * @param isEventStart  whether the timestamp to be edited is the start of the event
 */
class TimePickerFragment(
    private val listener: OnTimeSetListener,
    private val isEventStart: Boolean,
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timestampSplit = (arguments?.getString("timestamp") ?: "").split(":")
        return TimePickerDialog(
            activity,
            this,
            timestampSplit[0].toInt(),
            timestampSplit[1].toInt(),
            true,
        )
    }

    /**
     * Custom implementation of TimePickerDialog#OnTimeSetListener that includes a boolean to find
     * out whether the start or the end timestamp has been edited.
     */
    interface OnTimeSetListener {

        /**
         * Timestamp has been edited by the user.
         *
         * @param hours         hour of the timestamp
         * @param minutes       minute of the timestamp
         * @param isEventStart  whether the timestamp to be edited is the start of the event
         */
        fun onTimeSet(hours: Int, minutes: Int, isEventStart: Boolean)
    }

    /**
     * Override the implementation of TimePickerDialog#OnTimeSetListener.
     */
    override fun onTimeSet(picker: TimePicker?, hours: Int, minutes: Int) {
        // Use custom implementation of OnTimeSetListener
        listener.onTimeSet(hours, minutes, isEventStart)
    }
}