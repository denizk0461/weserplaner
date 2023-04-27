package com.denizk0461.studip.exception

import java.io.IOException

/**
 * This exception should be used when a conversion to an academic quarter is attempted to be applied
 * to a timestamp that is not meant for it.
 */
class AcademicQuarterNotApplicableException : IOException()