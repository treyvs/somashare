package com.example.somashare.ui.lecturer.home

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
fun LecturerHomeScreen(
    navController: NavController,
    preferencesManager: PreferencesManager,
    viewModel: LecturerHomeViewModel = viewModel()
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
                            "Lecturer Portal",
                            fontSize = 12.sp,
                            color = Color(0xFFC7D2FE)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4F46E5),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = { LecturerBottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("lecturer_upload") },
                containerColor = Color(0xFF4F46E5)
            ) {
                Icon(Icons.Default.Add, "Upload", tint = Color.White)
            }
        }
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
                    item {
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
                                    icon = Icons.Default.CloudUpload,
                                    value = uiState.uploadedCount.toString(),
                                    label = "Uploaded"
                                )
                                StatItem(
                                    icon = Icons.Default.Visibility,
                                    value = uiState.totalViews.toString(),
                                    label = "Total Views"
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "My Uploaded Papers",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (uiState.uploadedPapers.isEmpty()) {
                        item {
                            EmptyStateScreen(
                                icon = Icons.Default.CloudUpload,
                                title = "No Papers Yet",
                                message = "Start by uploading your first paper",
                                actionText = "Upload Now",
                                onActionClick = { navController.navigate("lecturer_upload") }
                            )
                        }
                    } else {
                        items(uiState.uploadedPapers) { paper ->
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
}