package com.example.somashare.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.somashare.data.local.PreferencesManager
import com.example.somashare.data.model.UserRole
import com.example.somashare.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    navController: NavController,
    preferencesManager: PreferencesManager
) {
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in, navigate based on role
            val role = preferencesManager.userRole.first()
            when (role) {
                UserRole.STUDENT.name -> navController.navigate("student_home") {
                    popUpTo("splash") { inclusive = true }
                }
                UserRole.LECTURER.name -> navController.navigate("lecturer_home") {
                    popUpTo("splash") { inclusive = true }
                }
                UserRole.ADMIN.name -> navController.navigate("admin_home") {
                    popUpTo("splash") { inclusive = true }
                }
                else -> navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        } else {
            // User not logged in
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4F46E5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                color = Color.White,
                shape = MaterialTheme.shapes.large
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "SS",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5)
                    )
                }
            }

            Text(
                text = "SomaShare",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Share Knowledge, Excel Together",
                fontSize = 14.sp,
                color = Color(0xFFC7D2FE)
            )

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp
            )
        }
    }
}
