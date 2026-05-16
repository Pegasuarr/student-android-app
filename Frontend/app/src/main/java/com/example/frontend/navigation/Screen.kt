package com.example.frontend.navigation

sealed class Screen(val route: String) {
    object Login    : Screen("login")
    object Register : Screen("register")
    object StudentList : Screen("student_list")
    object Grade    : Screen("grade")
}