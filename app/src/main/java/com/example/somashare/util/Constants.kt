package com.example.somashare.util

object Constants {
    // Firebase Collections
    const val USERS_COLLECTION = "users"
    const val PAPERS_COLLECTION = "papers"
    const val UNITS_COLLECTION = "units"
    const val RECENTLY_OPENED_COLLECTION = "recentlyOpened"
    const val FAVORITES_COLLECTION = "favorites"

    // Firebase Storage Paths
    const val PAPERS_STORAGE_PATH = "papers"
    const val PROFILES_STORAGE_PATH = "profiles"

    // File Constraints
    const val MAX_PDF_SIZE = 10 * 1024 * 1024 // 10MB
    const val MAX_IMAGE_SIZE = 5 * 1024 * 1024 // 5MB

    // Departments
    val DEPARTMENTS = listOf(
        "Computer Science",
        "Information Technology",
        "Software Engineering",
        "Business Information Technology",
        "Mathematics",
        "Statistics",
        "Engineering",
        "Business",
        "Other"
    )

    // Years of Study
    val YEARS_OF_STUDY = listOf(1, 2, 3, 4, 5, 6)

    // Semesters
    val SEMESTERS = listOf(1, 2)

    // Paper Types
    val PAPER_TYPES = listOf(
        "Final Exam",
        "Midterm",
        "CAT 1",
        "CAT 2",
        "Assignment",
        "Quiz",
        "Practical"
    )

    // Validation
    const val MIN_PASSWORD_LENGTH = 6
    const val MIN_NAME_LENGTH = 3
}