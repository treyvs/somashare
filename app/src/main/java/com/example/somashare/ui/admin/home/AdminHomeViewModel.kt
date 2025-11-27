package com.example.somashare.ui.admin.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somashare.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AdminHomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val totalUsers: Int = 0,
    val totalPapers: Int = 0,
    val totalUnits: Int = 0,
    val pendingVerifications: Int = 0
)

class AdminHomeViewModel : ViewModel() {
    private val paperRepository = PaperRepository()
    private val unitRepository = UnitRepository()
    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow(AdminHomeUiState())
    val uiState: StateFlow<AdminHomeUiState> = _uiState.asStateFlow()

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                paperRepository.getAllPapers().first().let { papers ->
                    val pending = papers.count { !it.isVerified }
                    _uiState.update {
                        it.copy(
                            totalPapers = papers.size,
                            pendingVerifications = pending
                        )
                    }
                }

                unitRepository.getAllUnits().first().let { units ->
                    _uiState.update { it.copy(totalUnits = units.size) }
                }

                // Count users - simplified
                _uiState.update {
                    it.copy(
                        totalUsers = 0, // You'd need to implement getAllUsers()
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Failed to load data")
                }
            }
        }
    }
}