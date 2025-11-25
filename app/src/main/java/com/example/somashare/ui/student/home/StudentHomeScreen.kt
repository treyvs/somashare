// File: ui/student/home/StudentHomeScreen.kt
package com.example.somashare.ui.student.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.somashare.data.local.PreferencesManager
import com.example.somashare.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    navController: NavController,
    preferencesManager: PreferencesManager,
    viewModel: StudentHomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Welcome, ${uiState.userName}", fontSize = 20.sp)
                        Text(
                            "Year ${uiState.year} | Semester ${uiState.semester}",
                            fontSize = 12.sp,
                            color = Color(0xFFC7D2FE)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(Icons.Default.Notifications, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4F46E5),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = { StudentBottomNavBar(navController) }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingScreen()
            uiState.error != null -> ErrorScreen(uiState.error!!) { viewModel.loadHomeData() }
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Quick Stats
                    item {
                        QuickStatsCard(
                            papersAvailable = uiState.totalPapers,
                            recentlyOpened = uiState.recentPapers.size
                        )
                    }

                    // Recommended Units
                    item {
                        SectionHeader(
                            title = "Your Units",
                            onSeeAll = { navController.navigate("student_search") }
                        )
                    }

                    items(uiState.units.take(4)) { unit ->
                        UnitCard(unit = unit) {
                            navController.navigate("unit_details/${unit.unitId}")
                        }
                    }

                    // Recent Papers
                    item {
                        SectionHeader(
                            title = "Recently Viewed",
                            onSeeAll = { navController.navigate("student_recent") }
                        )
                    }

                    items(uiState.recentPapers.take(5)) { paper ->
                        PaperCard(
                            paper = paper,
                            onView = { navController.navigate("pdf_viewer/${paper.paperId}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickStatsCard(papersAvailable: Int, recentlyOpened: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                icon = Icons.Default.Description,
                value = papersAvailable.toString(),
                label = "Papers Available"
            )
            StatItem(
                icon = Icons.Default.History,
                value = recentlyOpened.toString(),
                label = "Recently Viewed"
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onSeeAll) {
            Text("See All", color = Color(0xFF4F46E5))
        }
    }
}