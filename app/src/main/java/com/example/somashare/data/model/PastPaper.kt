package com.example.somashare.data.model

data class PastPaper(
    val paperId: String = "",
    val paperName: String = "",
    val unitId: String = "",
    val unitCode: String = "",
    val unitName: String = "",
    val yearOfStudy: Int = 1,
    val semesterOfStudy: Int = 1,
    val paperYear: Int = 2024,
    val paperType: PaperType = PaperType.FINAL_EXAM,
    val fileUrl: String = "",
    val fileSize: Long = 0,
    val uploadedBy: String = "",
    val uploaderName: String = "",
    val uploadDate: Long = System.currentTimeMillis(),
    val downloadCount: Int = 0,
    val viewCount: Int = 0,
    val isVerified: Boolean = false,
    val isActive: Boolean = true,
    val description: String = ""
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "paperId" to paperId,
            "paperName" to paperName,
            "unitId" to unitId,
            "unitCode" to unitCode,
            "unitName" to unitName,
            "yearOfStudy" to yearOfStudy,
            "semesterOfStudy" to semesterOfStudy,
            "paperYear" to paperYear,
            "paperType" to paperType.name,
            "fileUrl" to fileUrl,
            "fileSize" to fileSize,
            "uploadedBy" to uploadedBy,
            "uploaderName" to uploaderName,
            "uploadDate" to uploadDate,
            "downloadCount" to downloadCount,
            "viewCount" to viewCount,
            "isVerified" to isVerified,
            "isActive" to isActive,
            "description" to description
        )
    }
}