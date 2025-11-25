package com.example.somashare.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.somashare.data.local.PreferencesManager
import com.example.somashare.data.model.UserRole
import com.example.somashare.data.repository.AuthRepository
import com.example.somashare.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isSignUpSuccess: Boolean = false,
    val error: String? = null,
    val userRole: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val preferencesManager = PreferencesManager(application)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authRepository.signIn(email, password)

            if (result.isSuccess) {
                val userId = result.getOrNull()?.uid
                if (userId != null) {
                    // Get user role from database
                    userRepository.getUserById(userId).first()?.let { user ->
                        preferencesManager.saveUserRole(user.role.name)
                        preferencesManager.saveUserId(userId)

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoginSuccess = true,
                                userRole = user.role.name
                            )
                        }
                    }
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Login failed"
                    )
                }
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        fullName: String,
        role: UserRole,
        department: String,
        yearOfStudy: Int,
        semesterOfStudy: Int
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authRepository.signUp(
                email = email,
                password = password,
                fullName = fullName,
                role = role,
                department = department,
                yearOfStudy = yearOfStudy,
                semesterOfStudy = semesterOfStudy
            )

            if (result.isSuccess) {
                val user = result.getOrNull()!!
                preferencesManager.saveUserRole(user.role.name)
                preferencesManager.saveUserId(user.userId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSignUpSuccess = true,
                        userRole = user.role.name
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Sign up failed"
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            preferencesManager.clear()
        }
    }
}