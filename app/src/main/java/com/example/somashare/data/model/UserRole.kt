package com.example.somashare.data.model

enum class UserRole {
    STUDENT,
    LECTURER,
    ADMIN;

    companion object {
        fun fromString(value: String): UserRole {
            return try {
                valueOf(value.uppercase())
            } catch (e: Exception) {
                STUDENT
            }
        }
    }
}