package com.example.somashare.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageRepository {
    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadPdf(
        fileUri: Uri,
        unitCode: String,
        paperYear: Int,
        paperType: String,
        onProgress: (Int) -> Unit = {}
    ): Result<String> {
        return try {
            val fileName = "${UUID.randomUUID()}.pdf"
            val path = "papers/$unitCode/$paperYear/$paperType/$fileName"
            val storageRef = storage.reference.child(path)

            val uploadTask = storageRef.putFile(fileUri)
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred /
                        taskSnapshot.totalByteCount).toInt()
                onProgress(progress)
            }

            uploadTask.await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadProfileImage(
        fileUri: Uri,
        userId: String,
        onProgress: (Int) -> Unit = {}
    ): Result<String> {
        return try {
            val fileName = "${UUID.randomUUID()}.jpg"
            val path = "profiles/$userId/$fileName"
            val storageRef = storage.reference.child(path)

            val uploadTask = storageRef.putFile(fileUri)
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred /
                        taskSnapshot.totalByteCount).toInt()
                onProgress(progress)
            }

            uploadTask.await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFile(fileUrl: String): Result<Unit> {
        return try {
            val storageRef = storage.getReferenceFromUrl(fileUrl)
            storageRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}