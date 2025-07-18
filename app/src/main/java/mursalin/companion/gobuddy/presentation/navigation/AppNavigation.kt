package mursalin.companion.gobuddy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import mursalin.companion.gobuddy.presentation.screens.auth.ForgotPasswordScreen
import mursalin.companion.gobuddy.presentation.screens.auth.LoginScreen
import mursalin.companion.gobuddy.presentation.screens.auth.SignUpScreen
import mursalin.companion.gobuddy.presentation.viewmodel.AuthViewModel

// Defines the routes for the navigation graphs
object Route {
    const val AUTH = "auth_graph"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"
    const val FORGOT_PASSWORD = "forgot_password"
    const val DASHBOARD = "dashboard_graph" // Example for after login
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = Route.AUTH) {

        // This is a nested graph for the entire authentication flow.
        // It groups all auth-related screens together.
        navigation(startDestination = Route.LOGIN, route = Route.AUTH) {

            composable(Route.LOGIN) {
                LoginScreen(
                    state = authState,
                    onEvent = authViewModel::onEvent,
                    onNavigateToSignUp = { navController.navigate(Route.SIGN_UP) },
                    onNavigateToForgotPassword = { navController.navigate(Route.FORGOT_PASSWORD) }
                    // Add a navigateToDashboard lambda here for after successful login
                )
            }

            composable(Route.SIGN_UP) {
                SignUpScreen(
                    state = authState,
                    onEvent = authViewModel::onEvent,
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            composable(Route.FORGOT_PASSWORD) {
                ForgotPasswordScreen(
                    state = authState,
                    onEvent = authViewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // Add other navigation graphs or screens for your app here
        // For example, the main part of your app after a user is logged in:
        navigation(startDestination = "dashboard_main", route = Route.DASHBOARD) {
            /*
            composable("dashboard_main") {
                // Your DashboardScreen would go here
            }
            // ... other screens accessible from the dashboard
            */
        }
    }
}
