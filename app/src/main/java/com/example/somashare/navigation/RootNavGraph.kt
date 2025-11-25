package com.example.somashare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.somashare.data.local.PreferencesManager
import com.example.somashare.ui.auth.*
import com.example.somashare.ui.splash.SplashScreen
import com.example.somashare.ui.student.home.StudentHomeScreen
import com.example.somashare.ui.lecturer.home.LecturerHomeScreen
import com.example.somashare.ui.admin.home.AdminHomeScreen

@Composable
fun RootNavGraph(
    navController: NavHostController,
    preferencesManager: PreferencesManager
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(navController, preferencesManager)
        }

        // Auth Flow
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(navController)
        }

        composable(
            route = Screen.SignUp.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "STUDENT"
            SignUpScreen(navController, role)
        }

        // Student Routes
        composable(Screen.StudentHome.route) {
            StudentHomeScreen(navController, preferencesManager)
        }

        // Lecturer Routes
        composable(Screen.LecturerHome.route) {
            LecturerHomeScreen(navController, preferencesManager)
        }

        // Admin Routes
        composable(Screen.AdminHome.route) {
            AdminHomeScreen(navController, preferencesManager)
        }

        // Common Routes
        composable(
            route = Screen.PdfViewer.route,
            arguments = listOf(navArgument("paperId") { type = NavType.StringType })
        ) { backStackEntry ->
            val paperId = backStackEntry.arguments?.getString("paperId") ?: ""
            // PdfViewerScreen(paperId, navController)
        }

        composable(
            route = Screen.UnitDetails.route,
            arguments = listOf(navArgument("unitId") { type = NavType.StringType })
        ) { backStackEntry ->
            val unitId = backStackEntry.arguments?.getString("unitId") ?: ""
            // UnitDetailsScreen(unitId, navController)
        }
    }
}
