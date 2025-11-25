package com.example.somashare.data.model

data class Unit(
    val unitId: String = "",
    val unitCode: String = "",
    val unitName: String = "",
    val department: String = "",
    val yearOfStudy: Int = 1,
    val semesterOfStudy: Int = 1,
    val credits: Int = 3,
    val description: String = "",
    val lecturerId: String = "",
    val lecturerName: String = "",
    val paperCount: Int = 0,
    val isActive: Boolean = true
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "unitId" to unitId,
            "unitCode" to unitCode,
            "unitName" to unitName,
            "department" to department,
            "yearOfStudy" to yearOfStudy,
            "semesterOfStudy" to semesterOfStudy,
            "credits" to credits,
            "description" to description,
            "lecturerId" to lecturerId,
            "lecturerName" to lecturerName,
            "paperCount" to paperCount,
            "isActive" to isActive
        )
    }
}
