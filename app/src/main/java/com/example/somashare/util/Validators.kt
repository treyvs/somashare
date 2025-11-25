package com.example.somashare.util

import android.util.Patterns

object Validators {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= Constants.MIN_PASSWORD_LENGTH
    }

    fun isValidName(name: String): Boolean {
        return name.length >= Constants.MIN_NAME_LENGTH &&
                name.matches(Regex("^[a-zA-Z\\s]+$"))
    }

    fun isValidYear(year: Int): Boolean {
        return year in 1..6
    }

    fun isValidSemester(semester: Int): Boolean {
        return semester in 1..2
    }

    fun isValidFileSize(size: Long, maxSize: Long): Boolean {
        return size > 0 && size <= maxSize
    }

    fun isPdfFile(fileName: String): Boolean {
        return fileName.lowercase().endsWith(".pdf")
    }

    fun isImageFile(fileName: String): Boolean {
        val extension = fileName.lowercase().substringAfterLast('.')
        return extension in listOf("jpg", "jpeg", "png", "gif")
    }
}