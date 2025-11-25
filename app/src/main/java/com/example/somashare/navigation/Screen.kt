package com.example.somashare.navigation

sealed class Screen(val route: String) {
    // Auth
    object Splash : Screen("splash")
    object Login : Screen("login")
    object RoleSelection : Screen("role_selection")
    object SignUp : Screen("signup/{role}") {
        fun createRoute(role: String) = "signup/$role"
    }

    // Student
    object StudentHome : Screen("student_home")
    object StudentSearch : Screen("student_search")
    object StudentRecent : Screen("student_recent")
    object StudentProfile : Screen("student_profile")

    // Lecturer
    object LecturerHome : Screen("lecturer_home")
    object LecturerUpload : Screen("lecturer_upload")
    object LecturerManage : Screen("lecturer_manage")
    object LecturerProfile : Screen("lecturer_profile")

    // Admin
    object AdminHome : Screen("admin_home")
    object AdminUsers : Screen("admin_users")
    object AdminPapers : Screen("admin_papers")
    object AdminUnits : Screen("admin_units")
    object AdminProfile : Screen("admin_profile")

    // Common
    object PdfViewer : Screen("pdf_viewer/{paperId}") {
        fun createRoute(paperId: String) = "pdf_viewer/$paperId"
    }
    object UnitDetails : Screen("unit_details/{unitId}") {
        fun createRoute(unitId: String) = "unit_details/$unitId"
    }
}