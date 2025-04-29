package com.vladisc.financial.app.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateUtils {
    fun convertMillisToDate(millis: Long, format: String? = "d.M.yyyy"): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(Date(millis))
    }

    fun convertDateFromPatternToISO(date: String, pattern: String? = "d.M.yyyy"): String {
        if (date.isEmpty()) {
            return date
        }
        val inputFormatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ISO_LOCAL_DATE // same as "yyyy-MM-dd"
        val date = LocalDate.parse(date, inputFormatter)
        return outputFormatter.format(date)
    }
}

