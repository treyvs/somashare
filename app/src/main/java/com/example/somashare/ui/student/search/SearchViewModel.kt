package com.example.somashare.ui.student.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somashare.data.model.PastPaper
import com.example.somashare.data.model.PaperType
import com.example.somashare.data.repository.PaperRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class PaperFilter {
    ALL, FINAL_EXAM, MIDTERM, CAT, ASSIGNMENT
}

data class SearchUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val allPapers: List<PastPaper> = emptyList(),
    val filteredPapers: List<PastPaper> = emptyList(),
    val selectedFilter: PaperFilter = PaperFilter.ALL,
    val searchQuery: String = ""
)

class SearchViewModel : ViewModel() {
    private val paperRepository = PaperRepository()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun loadPapers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                paperRepository.getAllPapers().first().let { papers ->
                    _uiState.update {
                        it.copy(
                            allPapers = papers,
                            filteredPapers = papers,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Failed to load papers")
                }
            }
        }
    }

    fun search(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun setFilter(filter: PaperFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        var filtered = state.allPapers

        // Apply search query
        if (state.searchQuery.isNotBlank()) {
            val query = state.searchQuery.lowercase()
            filtered = filtered.filter { paper ->
                paper.paperName.lowercase().contains(query) ||
                        paper.unitCode.lowercase().contains(query) ||
                        paper.unitName.lowercase().contains(query)
            }
        }

        // Apply filter
        filtered = when (state.selectedFilter) {
            PaperFilter.ALL -> filtered
            PaperFilter.FINAL_EXAM -> filtered.filter { it.paperType == PaperType.FINAL_EXAM }
            PaperFilter.MIDTERM -> filtered.filter { it.paperType == PaperType.MIDTERM }
            PaperFilter.CAT -> filtered.filter {
                it.paperType == PaperType.CAT_1 || it.paperType == PaperType.CAT_2
            }
            PaperFilter.ASSIGNMENT -> filtered.filter { it.paperType == PaperType.ASSIGNMENT }
        }

        _uiState.update { it.copy(filteredPapers = filtered) }
    }
}
