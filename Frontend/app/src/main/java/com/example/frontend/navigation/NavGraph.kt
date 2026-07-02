package com.example.frontend.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.frontend.ui.screen.auth.ForgotPasswordScreen
import com.example.frontend.ui.screen.auth.LoginScreen
import com.example.frontend.ui.screen.auth.RegisterScreen
import com.example.frontend.ui.screen.student.StudentDetailScreen
import com.example.frontend.ui.screen.student.StudentListScreen
import com.example.frontend.ui.viewmodel.AuthViewModel
import com.example.frontend.ui.viewmodel.GradeViewModel
import com.example.frontend.ui.viewmodel.StudentViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = viewModel()
    val studentViewModel: StudentViewModel = viewModel()
    val gradeViewModel: GradeViewModel = viewModel()

    val startDestination = if (authViewModel.isLoggedIn) {
        Screen.StudentList.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                isLoading = authViewModel.isLoading,
                errorMessage = authViewModel.errorMessage,
                onLoginClick = { email, password ->
                    authViewModel.login(email, password) {
                        navController.navigate(Screen.StudentList.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onRegisterClick = {
                    authViewModel.clearError()
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {
                    authViewModel.clearError()
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                isLoading = authViewModel.isLoading,
                errorMessage = authViewModel.errorMessage,
                onRegisterClick = { username, email, password ->
                    authViewModel.register(username, email, password) {
                        navController.navigate(Screen.StudentList.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                },
                onLoginClick = {
                    authViewModel.clearError()
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.StudentList.route) {
            StudentListScreen(
                studentViewModel = studentViewModel,
                authViewModel = authViewModel,
                onStudentClick = { studentId ->
                    navController.navigate(Screen.StudentDetail.createRoute(studentId))
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.StudentDetail.route,
            arguments = listOf(navArgument("studentId") { type = NavType.LongType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getLong("studentId") ?: 0L
            StudentDetailScreen(
                studentId = studentId,
                studentViewModel = studentViewModel,
                gradeViewModel = gradeViewModel,
                authViewModel = authViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onStudentDeleted = {
                    navController.popBackStack()
                }
            )
        }
    }
}