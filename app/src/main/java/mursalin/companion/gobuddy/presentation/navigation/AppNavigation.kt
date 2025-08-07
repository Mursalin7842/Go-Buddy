package mursalin.companion.gobuddy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mursalin.companion.gobuddy.presentation.components.LoadingAnimation
import mursalin.companion.gobuddy.presentation.screens.s09achievements.AchievementsScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.ForgotPasswordScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.LoginScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.SignUpScreen
import mursalin.companion.gobuddy.presentation.screens.s07chat.ChatScreen
import mursalin.companion.gobuddy.presentation.screens.s03dashboard.DashboardScreen
import mursalin.companion.gobuddy.presentation.screens.s04project_list.ProjectListScreen
import mursalin.companion.gobuddy.presentation.screens.s10settings.SettingsScreen
import mursalin.companion.gobuddy.presentation.screens.s05task_board.TaskBoardScreen
import mursalin.companion.gobuddy.presentation.viewmodel.AuthEvent
import mursalin.companion.gobuddy.presentation.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Get the AuthViewModel which is shared across auth-related screens
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.state.collectAsState()

    // State to hold the dynamic start destination. Null means we are still checking.
    var startDestination by remember { mutableStateOf<String?>(null) }

    // This effect runs once to check if a session is already active.
    LaunchedEffect(key1 = Unit) {
        authViewModel.onEvent(AuthEvent.CheckSession)
    }

    // This effect observes the result of the session check.
    LaunchedEffect(authState.isLoading, authState.user) {
        // Wait until the loading check is complete
        if (!authState.isLoading) {
            startDestination = if (authState.user != null) {
                // If a user session exists, start at the Dashboard
                Screen.Dashboard.route
            } else {
                // If no user session, start at the Login screen
                Screen.Login.route
            }
        }
    }

    // Only display the navigation graph once the start destination has been determined.
    if (startDestination != null) {
        NavHost(navController = navController, startDestination = startDestination!!) {
            // The Splash Screen route is no longer needed here because the logic above replaces it.

            composable(Screen.Login.route) {
                LoginScreen(
                    authViewModel = authViewModel, // Re-use the same ViewModel
                    onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                    onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            // Clear the entire back stack on successful login
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.SignUp.route) {
                SignUpScreen(
                    authViewModel = authViewModel, // Re-use the same ViewModel
                    onNavigateToLogin = { navController.popBackStack() },
                    onSignUpSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            // Clear the entire back stack on successful signup
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    authViewModel = authViewModel, // Re-use the same ViewModel
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController = navController)
            }
            composable(Screen.ProjectList.route) {
                ProjectListScreen(navController = navController)
            }
            composable(Screen.TaskBoard.route) {
                TaskBoardScreen(navController = navController)
            }
            composable(Screen.Chat.route) {
                ChatScreen(navController = navController)
            }
            composable(Screen.Achievements.route) {
                AchievementsScreen(navController = navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController)
            }
        }
    } else {
        // While checking the session, display a loading animation.
        // This effectively serves as your new splash screen.
        LoadingAnimation()
    }
}