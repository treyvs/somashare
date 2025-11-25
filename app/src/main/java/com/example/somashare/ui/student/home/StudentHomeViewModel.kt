// File: ui/student/home/StudentHomeViewModel.kt
package com.example.somashare.ui.student.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somashare.data.model.PastPaper
import com.example.somashare.data.model.Unit
import com.example.somashare.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class StudentHomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userName: String = "",
    val year: Int = 1,
    val semester: Int = 1,
    val units: List<Unit> = emptyList(),
    val recentPapers: List<PastPaper> = emptyList(),
    val totalPapers: Int = 0
)

class StudentHomeViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val unitRepository = UnitRepository()
    private val paperRepository = PaperRepository()

    private val _uiState = MutableStateFlow(StudentHomeUiState())
    val uiState: StateFlow<StudentHomeUiState> = _uiState.asStateFlow()

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val userId = authRepository.getCurrentUserId()
            if (userId == null) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Not logged in")
                }
                return@launch
            }

            try {
                // Load user data
                userRepository.getUserById(userId).first()?.let { user ->
                    _uiState.update {
                        it.copy(
                            userName = user.fullName,
                            year = user.yearOfStudy,
                            semester = user.semesterOfStudy
                        )
                    }

                    // Load units for user's year and semester
                    unitRepository.getUnitsByYearAndSemester(
                        user.yearOfStudy,
                        user.semesterOfStudy
                    ).first().let { units ->
                        _uiState.update { it.copy(units = units) }
                    }

                    // Load recent papers
                    paperRepository.getRecentlyOpened(userId).first().let { papers ->
                        _uiState.update { it.copy(recentPapers = papers) }
                    }

                    // Load total papers count
                    paperRepository.getAllPapers().first().let { papers ->
                        _uiState.update {
                            it.copy(totalPapers = papers.size, isLoading = false)
                        }
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