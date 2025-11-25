package com.example.somashare.data.model

enum class PaperType(val displayName: String) {
    FINAL_EXAM("Final Exam"),
    MIDTERM("Midterm"),
    CAT_1("CAT 1"),
    CAT_2("CAT 2"),
    ASSIGNMENT("Assignment"),
    QUIZ("Quiz"),
    PRACTICAL("Practical");

    companion object {
        fun fromString(value: String): PaperType {
            return entries.find {
                it.displayName.equals(value, ignoreCase = true) ||
                        it.name.equals(value, ignoreCase = true)
            } ?: FINAL_EXAM
        }
    }
}
