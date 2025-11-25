package com.example.somashare.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun StudentBottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("student_home", Icons.Default.Home, "Home"),
        BottomNavItem("student_search", Icons.Default.Search, "Search"),
        BottomNavItem("student_recent", Icons.Default.History, "Recent"),
        BottomNavItem("student_profile", Icons.Default.Person, "Profile")
    )

    BottomNavBar(navController, items)
}

@Composable
fun LecturerBottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("lecturer_home", Icons.Default.Home, "Home"),
        BottomNavItem("lecturer_upload", Icons.Default.Upload, "Upload"),
        BottomNavItem("lecturer_manage", Icons.Default.FolderOpen, "Manage"),
        BottomNavItem("lecturer_profile", Icons.Default.Person, "Profile")
    )

    BottomNavBar(navController, items)
}

@Composable
fun AdminBottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("admin_home", Icons.Default.Dashboard, "Dashboard"),
        BottomNavItem("admin_users", Icons.Default.People, "Users"),
        BottomNavItem("admin_papers", Icons.Default.Description, "Papers"),
        BottomNavItem("admin_units", Icons.Default.School, "Units")
    )

    BottomNavBar(navController, items)
}

@Composable
private fun BottomNavBar(
    navController: NavController,
    items: List<BottomNavItem>
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF4F46E5)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(items.first().route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF4F46E5),
                    selectedTextColor = Color(0xFF4F46E5),
                    indicatorColor = Color(0xFFEEF2FF),
                    unselectedIconColor = Color(0xFF9CA3AF),
                    unselectedTextColor = Color(0xFF9CA3AF)
                )
            )
        }
    }
}