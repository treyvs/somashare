package com.example.somashare.data.model

data class Favorite(
    val unitId: String = "",
    val unitCode: String = "",
    val unitName: String = "",
    val addedAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "unitId" to unitId,
            "unitCode" to unitCode,
            "unitName" to unitName,
            "addedAt" to addedAt
        )
    }
}