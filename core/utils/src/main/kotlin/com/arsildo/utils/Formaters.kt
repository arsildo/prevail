package com.arsildo.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Formaters {
    fun adjustTimeZone(input: String): String {
        val inputTimeZone = TimeZone.getTimeZone("America/New_York") // EST/EDT timezone
        val timeZone = TimeZone.getDefault()
        val name = timeZone.id
        val userTimeZoneObject = TimeZone.getTimeZone(name)

        val dateFormatInput = SimpleDateFormat("MM/dd/yy(EEE)HH:mm:ss", Locale.US)
        dateFormatInput.timeZone = inputTimeZone

        val dateFormatOutput = SimpleDateFormat("HH:mm:ss dd/MM/yy", Locale.US)
        dateFormatOutput.timeZone = userTimeZoneObject

        try {
            val date = dateFormatInput.parse(input)
            return dateFormatOutput.format(date)
        } catch (e: Exception) {
            return "Invalid date format"
        }
    }
}