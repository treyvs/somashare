package com.example.somashare.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateString(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.toTimeAgo(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        seconds < 60 -> "Just now"
        minutes < 60 -> "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
        hours < 24 -> "$hours ${if (hours == 1L) "hour" else "hours"} ago"
        days < 7 -> "$days ${if (days == 1L) "day" else "days"} ago"
        weeks < 4 -> "$weeks ${if (weeks == 1L) "week" else "weeks"} ago"
        months < 12 -> "$months ${if (months == 1L) "month" else "months"} ago"
        else -> "$years ${if (years == 1L) "year" else "years"} ago"
    }
}

fun Long.formatFileSize(): String {
    val kb = 1024
    val mb = kb * 1024
    val gb = mb * 1024

    return when {
        this >= gb -> String.format("%.2f GB", this.toFloat() / gb)
        this >= mb -> String.format("%.2f MB", this.toFloat() / mb)
        this >= kb -> String.format("%.2f KB", this.toFloat() / kb)
        else -> "$this B"
    }
}

fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }
}