package com.example.frontend.navigation

sealed class Screen(val route: String) {
    object Login    : Screen("login")
    object Register : Screen("register")
    object StudentList : Screen("student_list")
    object StudentDetail : Screen("student_details/{studentId}") {
        fun createRoute(studentId: Long) = "student_details/$studentId"
    }
    object Grade    : Screen("grade")
    object ForgotPassword : Screen("Forgot_Password")
}