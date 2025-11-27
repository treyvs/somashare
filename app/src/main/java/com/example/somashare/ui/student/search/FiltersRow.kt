package com.example.somashare.ui.student.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersRow(
    selectedFilter: PaperFilter,
    onFilterSelected: (PaperFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter == PaperFilter.ALL,
            onClick = { onFilterSelected(PaperFilter.ALL) },
            label = { Text("All") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF4F46E5),
                selectedLabelColor = Color.White
            )
        )
        FilterChip(
            selected = selectedFilter == PaperFilter.FINAL_EXAM,
            onClick = { onFilterSelected(PaperFilter.FINAL_EXAM) },
            label = { Text("Finals") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF4F46E5),
                selectedLabelColor = Color.White
            )
        )
        FilterChip(
            selected = selectedFilter == PaperFilter.MIDTERM,
            onClick = { onFilterSelected(PaperFilter.MIDTERM) },
            label = { Text("Midterms") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF4F46E5),
                selectedLabelColor = Color.White
            )
        )
        FilterChip(
            selected = selectedFilter == PaperFilter.CAT,
            onClick = { onFilterSelected(PaperFilter.CAT) },
            label = { Text("CATs") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF4F46E5),
                selectedLabelColor = Color.White
            )
        )
        FilterChip(
            selected = selectedFilter == PaperFilter.ASSIGNMENT,
            onClick = { onFilterSelected(PaperFilter.ASSIGNMENT) },
            label = { Text("Assignments") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF4F46E5),
                selectedLabelColor = Color.White
            )
        )
    }
}
