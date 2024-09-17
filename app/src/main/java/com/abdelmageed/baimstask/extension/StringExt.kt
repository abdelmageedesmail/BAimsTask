package com.abdelmageed.baimstask.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.convertStringToDate(): Date? {

    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return try {
        sdf.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun String.getTimeFormat(): String? {
    try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val originalTime = originalFormat.parse(this)

        val desiredFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return originalTime?.let { desiredFormat.format(it) }
    } catch (e: Exception) {
        return ""
    }
}

fun String.getDayMonthFormat(): String? {
    try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val originalTime = originalFormat.parse(this)

        val desiredFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        return originalTime?.let { desiredFormat.format(it) }
    } catch (e: Exception) {
        return ""
    }
}

fun String.getNameOfTheDay(): String? {
    try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val originalTime = originalFormat.parse(this)

        val desiredFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return originalTime?.let { desiredFormat.format(it) }
    } catch (e: Exception) {
        return ""
    }
}

fun String.getTimeFormatForTomorrow(): String? {
    try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val originalTime = originalFormat.parse(this)

        val desiredFormat = SimpleDateFormat("MM/dd hh:mm a", Locale.getDefault())
        return originalTime?.let { desiredFormat.format(it) }
    } catch (e: Exception) {
        return ""
    }
}

fun Date.getCurrentDate(): String {
    try {
        val dateFormat = SimpleDateFormat("EEEE MM/dd hh:mm a", Locale.getDefault())
        return dateFormat.format(this)
    }catch (e:Exception){
        return ""
    }
}
