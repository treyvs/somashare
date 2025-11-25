package com.example.somashare.data.model

data class User(
    val userId: String = "",
    val email: String = "",
    val fullName: String = "",
    val role: UserRole = UserRole.STUDENT,
    val department: String = "",
    val yearOfStudy: Int = 1,
    val semesterOfStudy: Int = 1,
    val profileImageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    // Lecturer specific fields
    val officeLocation: String = "",
    val phoneNumber: String = "",
    val bio: String = ""
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "email" to email,
            "fullName" to fullName,
            "role" to role.name,
            "department" to department,
            "yearOfStudy" to yearOfStudy,
            "semesterOfStudy" to semesterOfStudy,
            "profileImageUrl" to profileImageUrl,
            "createdAt" to createdAt,
            "isActive" to isActive,
            "officeLocation" to officeLocation,
            "phoneNumber" to phoneNumber,
            "bio" to bio
        )
    }
}