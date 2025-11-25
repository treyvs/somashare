package com.example.somashare.data.repository

import com.example.somashare.data.model.PaperType
import com.example.somashare.data.model.PastPaper
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PaperRepository {
    private val database = FirebaseDatabase.getInstance().reference

    suspend fun uploadPaper(paper: PastPaper): Result<String> {
        return try {
            val paperId = database.child("papers").push().key
                ?: throw Exception("Failed to generate paper ID")
            val paperWithId = paper.copy(paperId = paperId)
            database.child("papers").child(paperId).setValue(paperWithId.toMap()).await()

            // Increment unit paper count
            database.child("units").child(paper.unitId)
                .child("paperCount").get().await().getValue(Int::class.java)?.let { count ->
                    database.child("units").child(paper.unitId)
                        .child("paperCount").setValue(count + 1).await()
                }

            Result.success(paperId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllPapers(): Flow<List<PastPaper>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val papers = mutableListOf<PastPaper>()
                for (child in snapshot.children) {
                    val paper = parsePaper(child)
                    if (paper != null && paper.isActive) {
                        papers.add(paper)
                    }
                }
                trySend(papers.sortedByDescending { it.uploadDate })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("papers").addValueEventListener(listener)
        awaitClose { database.child("papers").removeEventListener(listener) }
    }

    fun getPapersByUnit(unitId: String): Flow<List<PastPaper>> = callbackFlow {
        val query = database.child("papers").orderByChild("unitId").equalTo(unitId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val papers = mutableListOf<PastPaper>()
                for (child in snapshot.children) {
                    val paper = parsePaper(child)
                    if (paper != null && paper.isActive) {
                        papers.add(paper)
                    }
                }
                trySend(papers.sortedByDescending { it.paperYear })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    fun getPapersByUploader(uploaderId: String): Flow<List<PastPaper>> = callbackFlow {
        val query = database.child("papers").orderByChild("uploadedBy").equalTo(uploaderId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val papers = mutableListOf<PastPaper>()
                for (child in snapshot.children) {
                    val paper = parsePaper(child)
                    if (paper != null) {
                        papers.add(paper)
                    }
                }
                trySend(papers.sortedByDescending { it.uploadDate })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    suspend fun incrementDownloadCount(paperId: String): Result<Unit> {
        return try {
            val currentCount = database.child("papers").child(paperId)
                .child("downloadCount").get().await().getValue(Int::class.java) ?: 0
            database.child("papers").child(paperId)
                .child("downloadCount").setValue(currentCount + 1).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementViewCount(paperId: String): Result<Unit> {
        return try {
            val currentCount = database.child("papers").child(paperId)
                .child("viewCount").get().await().getValue(Int::class.java) ?: 0
            database.child("papers").child(paperId)
                .child("viewCount").setValue(currentCount + 1).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyPaper(paperId: String): Result<Unit> {
        return try {
            database.child("papers").child(paperId)
                .child("isVerified").setValue(true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePaper(paperId: String): Result<Unit> {
        return try {
            database.child("papers").child(paperId)
                .child("isActive").setValue(false).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recordRecentlyOpened(
        userId: String,
        paperId: String,
        paperName: String,
        unitCode: String,
        unitName: String
    ): Result<Unit> {
        return try {
            val recentData = mapOf(
                "paperId" to paperId,
                "paperName" to paperName,
                "unitCode" to unitCode,
                "unitName" to unitName,
                "openedAt" to System.currentTimeMillis()
            )
            database.child("recentlyOpened").child(userId).child(paperId)
                .setValue(recentData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getRecentlyOpened(userId: String): Flow<List<PastPaper>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val paperIds = mutableListOf<String>()
                for (child in snapshot.children) {
                    child.child("paperId").getValue(String::class.java)?.let {
                        paperIds.add(it)
                    }
                }

                // Fetch actual paper details
                val papers = mutableListOf<PastPaper>()
                paperIds.take(10).forEach { paperId ->
                    database.child("papers").child(paperId).get()
                        .addOnSuccessListener { paperSnapshot ->
                            parsePaper(paperSnapshot)?.let { papers.add(it) }
                            if (papers.size == paperIds.size || papers.size == 10) {
                                trySend(papers)
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("recentlyOpened").child(userId)
            .orderByChild("openedAt")
            .limitToLast(10)
            .addValueEventListener(listener)
        awaitClose {
            database.child("recentlyOpened").child(userId).removeEventListener(listener)
        }
    }

    private fun parsePaper(snapshot: DataSnapshot): PastPaper? {
        return try {
            PastPaper(
                paperId = snapshot.child("paperId").getValue(String::class.java) ?: "",
                paperName = snapshot.child("paperName").getValue(String::class.java) ?: "",
                unitId = snapshot.child("unitId").getValue(String::class.java) ?: "",
                unitCode = snapshot.child("unitCode").getValue(String::class.java) ?: "",
                unitName = snapshot.child("unitName").getValue(String::class.java) ?: "",
                yearOfStudy = snapshot.child("yearOfStudy").getValue(Int::class.java) ?: 1,
                semesterOfStudy = snapshot.child("semesterOfStudy").getValue(Int::class.java) ?: 1,
                paperYear = snapshot.child("paperYear").getValue(Int::class.java) ?: 2024,
                paperType = PaperType.fromString(
                    snapshot.child("paperType").getValue(String::class.java) ?: "FINAL_EXAM"
                ),
                fileUrl = snapshot.child("fileUrl").getValue(String::class.java) ?: "",
                fileSize = snapshot.child("fileSize").getValue(Long::class.java) ?: 0L,
                uploadedBy = snapshot.child("uploadedBy").getValue(String::class.java) ?: "",
                uploaderName = snapshot.child("uploaderName").getValue(String::class.java) ?: "",
                uploadDate = snapshot.child("uploadDate").getValue(Long::class.java) ?: 0L,
                downloadCount = snapshot.child("downloadCount").getValue(Int::class.java) ?: 0,
                viewCount = snapshot.child("viewCount").getValue(Int::class.java) ?: 0,
                isVerified = snapshot.child("isVerified").getValue(Boolean::class.java) ?: false,
                isActive = snapshot.child("isActive").getValue(Boolean::class.java) ?: true,
                description = snapshot.child("description").getValue(String::class.java) ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }
}
