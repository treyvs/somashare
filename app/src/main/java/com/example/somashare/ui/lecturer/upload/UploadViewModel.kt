package com.example.somashare.ui.lecturer.upload

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.somashare.data.model.PaperType
import com.example.somashare.data.model.PastPaper
import com.example.somashare.data.repository.AuthRepository
import com.example.somashare.data.repository.PaperRepository
import com.example.somashare.data.repository.StorageRepository
import com.example.somashare.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UploadUiState(
    val selectedFileUri: Uri? = null,
    val selectedFileName: String = "",
    val paperName: String = "",
    val unitCode: String = "",
    val unitName: String = "",
    val yearOfStudy: String = "",
    val semester: String = "",
    val paperYear: String = "",
    val paperType: String = "Final Exam",
    val isUploading: Boolean = false,
    val uploadProgress: Int = 0,
    val uploadSuccess: Boolean = false,
    val error: String? = null
)

class UploadViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val paperRepository = PaperRepository()
    private val storageRepository = StorageRepository()

    private val _uiState = MutableStateFlow(UploadUiState())
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    fun onFileSelected(uri: Uri) {
        val fileName = getFileName(uri)
        _uiState.update {
            it.copy(
                selectedFileUri = uri,
                selectedFileName = fileName,
                paperName = if (it.paperName.isEmpty()) fileName.removeSuffix(".pdf") else it.paperName
            )
        }
    }

    fun clearFile() {
        _uiState.update {
            it.copy(
                selectedFileUri = null,
                selectedFileName = "",
                uploadSuccess = false,
                error = null
            )
        }
    }

    fun canUpload(): Boolean {
        val state = _uiState.value
        return state.selectedFileUri != null &&
                state.paperName.isNotBlank() &&
                state.unitCode.isNotBlank() &&
                state.unitName.isNotBlank() &&
                state.yearOfStudy.isNotBlank() &&
                state.semester.isNotBlank() &&
                state.paperYear.isNotBlank()
    }

    fun uploadPaper() {
        if (!canUpload()) {
            _uiState.update { it.copy(error = "Please fill all fields") }
            return
        }

        val state = _uiState.value
        val fileUri = state.selectedFileUri ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, uploadProgress = 0, error = null) }

            try {
                val userId = authRepository.getCurrentUserId() ?: throw Exception("Not logged in")
                val user = userRepository.getUserById(userId).first()

                // Upload file
                val uploadResult = storageRepository.uploadPdf(
                    fileUri = fileUri,
                    unitCode = state.unitCode,
                    paperYear = state.paperYear.toInt(),
                    paperType = state.paperType,
                    onProgress = { progress ->
                        _uiState.update { it.copy(uploadProgress = progress) }
                    }
                )

                if (uploadResult.isFailure) {
                    throw uploadResult.exceptionOrNull() ?: Exception("Upload failed")
                }

                val fileUrl = uploadResult.getOrNull()!!

                // Create paper metadata
                val paper = PastPaper(
                    paperName = state.paperName,
                    unitId = state.unitCode,
                    unitCode = state.unitCode,
                    unitName = state.unitName,
                    yearOfStudy = state.yearOfStudy.toInt(),
                    semesterOfStudy = state.semester.toInt(),
                    paperYear = state.paperYear.toInt(),
                    paperType = PaperType.fromString(state.paperType),
                    fileUrl = fileUrl,
                    fileSize = 0,
                    uploadedBy = userId,
                    uploaderName = user?.fullName ?: "Unknown",
                    uploadDate = System.currentTimeMillis(),
                    isActive = true,
                    isVerified = false
                )

                val saveResult = paperRepository.uploadPaper(paper)

                if (saveResult.isSuccess) {
                    _uiState.update {
                        it.copy(
                            isUploading = false,
                            uploadProgress = 100,
                            uploadSuccess = true
                        )
                    }
                } else {
                    throw saveResult.exceptionOrNull() ?: Exception("Failed to save metadata")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isUploading = false,
                        uploadProgress = 0,
                        error = e.message ?: "Upload failed"
                    )
                }
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        val context = getApplication<Application>()
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    cursor.getString(nameIndex)
                } else "document.pdf"
            } ?: "document.pdf"
        } catch (e: Exception) {
            "document.pdf"
        }
    }
}