package mursalin.companion.gobuddy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mursalin.companion.gobuddy.presentation.screens.s02auth.ForgotPasswordScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.LoginScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.SignUpScreen
import mursalin.companion.gobuddy.presentation.screens.s03dashboard.DashboardScreen
import mursalin.companion.gobuddy.presentation.screens.s01splash.SplashScreen
import mursalin.companion.gobuddy.presentation.screens.s05task_board.TaskBoardScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = hiltViewModel(),
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                authViewModel = hiltViewModel(),
                onNavigateToLogin = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                authViewModel = hiltViewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.TaskBoard.route) {
            TaskBoardScreen(navController = navController)
        }
    }
}