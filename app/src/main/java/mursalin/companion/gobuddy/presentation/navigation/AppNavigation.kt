package mursalin.companion.gobuddy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mursalin.companion.gobuddy.presentation.screens.s02auth.ForgotPasswordScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.LoginScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.SignUpScreen
import mursalin.companion.gobuddy.presentation.screens.s03dashboard.DashboardScreen
import mursalin.companion.gobuddy.presentation.screens.s01splash.SplashScreen
import mursalin.companion.gobuddy.presentation.viewmodel.AuthViewModel

/*
 * This composable sets up the navigation graph for the entire application.
 * It uses a NavHost to define all possible destinations and manages the
 * navigation state.
 */
@Composable
fun AppNavigation() {
    val navController = androidx.navigation.compose.rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                // After splash, navigate to login and clear the back stack
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            // PRODUCTION: Use hiltViewModel() to get the ViewModel instance
            val authViewModel: AuthViewModel = hiltViewModel()
            LoginScreen(
                authViewModel = authViewModel,
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
            val authViewModel: AuthViewModel = hiltViewModel()
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.ForgotPassword.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            ForgotPasswordScreen(
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        // Add other destinations (TaskBoard, Chat, etc.) here
    }
}