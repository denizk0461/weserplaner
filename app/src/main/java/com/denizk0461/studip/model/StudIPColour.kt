package com.denizk0461.studip.model

import com.denizk0461.studip.R

/**
 * Enumeration used to save and apply custom colours to Stud.IP events.
 * TODO finish implementation
 *
 * @param colourId  resource ID of the colour
 */
enum class StudIPColour(val colourId: Int) {

    DEFAULT(R.attr.scheduleColorDefault);

    companion object {

        /**
         * Retrieve a StudIPColour object by its ordinal.
         *
         * @param ordinal   numeric position of the colour in the enumeration
         * @return          enum object
         */
        fun getEnumValue(ordinal: Int): StudIPColour =
            StudIPColour.values()[ordinal]
    }
}