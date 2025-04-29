package com.vladisc.financial.app.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
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

    fun convertDateFromISOPattern(date: String): String {
        if (date.isEmpty()) {
            return date
        }

        // Define two possible formats (with and without seconds)
        val formatWithSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatWithoutSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

        val dateTime = try {
            // Try parsing with format that includes seconds
            LocalDateTime.parse(date, formatWithSeconds)
        } catch (_: Exception) {
            // If it fails, try parsing with format that does not include seconds
            LocalDateTime.parse(date, formatWithoutSeconds)
        }

        // Format the parsed date to only include the date (no time)
        val outputFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        return outputFormatter.format(dateTime.toLocalDate())
    }
}

