package com.example.somashare.ui.lecturer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somashare.data.model.PastPaper
import com.example.somashare.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LecturerHomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userName: String = "",
    val uploadedPapers: List<PastPaper> = emptyList(),
    val uploadedCount: Int = 0,
    val totalViews: Int = 0
)

class LecturerHomeViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val paperRepository = PaperRepository()

    private val _uiState = MutableStateFlow(LecturerHomeUiState())
    val uiState: StateFlow<LecturerHomeUiState> = _uiState.asStateFlow()

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val userId = authRepository.getCurrentUserId()
            if (userId == null) {
                _uiState.update { it.copy(isLoading = false, error = "Not logged in") }
                return@launch
            }

            try {
                userRepository.getUserById(userId).first()?.let { user ->
                    _uiState.update { it.copy(userName = user.fullName) }
                }

                paperRepository.getPapersByUploader(userId).first().let { papers ->
                    val totalViews = papers.sumOf { it.viewCount }
                    _uiState.update {
                        it.copy(
                            uploadedPapers = papers,
                            uploadedCount = papers.size,
                            totalViews = totalViews,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Failed to load data")
                }
            }
        }
    }
}