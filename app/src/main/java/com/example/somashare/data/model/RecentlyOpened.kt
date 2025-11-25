package com.example.somashare.data.model

data class RecentlyOpened(
    val paperId: String = "",
    val paperName: String = "",
    val unitCode: String = "",
    val unitName: String = "",
    val openedAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "paperId" to paperId,
            "paperName" to paperName,
            "unitCode" to unitCode,
            "unitName" to unitName,
            "openedAt" to openedAt
        )
    }
}
