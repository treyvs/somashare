package com.example.somashare.ui.admin.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun AdminHomeScreen(
    navController: NavController,
    preferencesManager: PreferencesManager,
    viewModel: AdminHomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4F46E5),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = { AdminBottomNavBar(navController) }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingScreen()
            uiState.error != null -> ErrorScreen(uiState.error!!) { viewModel.loadDashboardData() }
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "System Overview",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                    }

                    item {
                        StatsGrid(
                            totalUsers = uiState.totalUsers,
                            totalPapers = uiState.totalPapers,
                            totalUnits = uiState.totalUnits,
                            pendingVerifications = uiState.pendingVerifications
                        )
                    }

                    item {
                        QuickActionsCard(
                            onManageUsers = { navController.navigate("admin_users") },
                            onVerifyPapers = { navController.navigate("admin_papers") },
                            onManageUnits = { navController.navigate("admin_units") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatsGrid(
    totalUsers: Int,
    totalPapers: Int,
    totalUnits: Int,
    pendingVerifications: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.People,
                    value = totalUsers.toString(),
                    label = "Total Users"
                )
                StatItem(
                    icon = Icons.Default.Description,
                    value = totalPapers.toString(),
                    label = "Total Papers"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.School,
                    value = totalUnits.toString(),
                    label = "Total Units"
                )
                StatItem(
                    icon = Icons.Default.PendingActions,
                    value = pendingVerifications.toString(),
                    label = "Pending"
                )
            }
        }
    }
}

@Composable
fun QuickActionsCard(
    onManageUsers: () -> Unit,
    onVerifyPapers: () -> Unit,
    onManageUnits: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onManageUsers,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
            ) {
                Icon(Icons.Default.People, null)
                Spacer(Modifier.width(8.dp))
                Text("Manage Users")
            }

            Button(
                onClick = onVerifyPapers,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Icon(Icons.Default.VerifiedUser, null)
                Spacer(Modifier.width(8.dp))
                Text("Verify Papers")
            }

            Button(
                onClick = onManageUnits,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
            ) {
                Icon(Icons.Default.School, null)
                Spacer(Modifier.width(8.dp))
                Text("Manage Units")
            }
        }
    }
}
