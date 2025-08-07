package mursalin.companion.gobuddy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mursalin.companion.gobuddy.presentation.screens.s09achievements.AchievementsScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.ForgotPasswordScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.LoginScreen
import mursalin.companion.gobuddy.presentation.screens.s02auth.SignUpScreen
import mursalin.companion.gobuddy.presentation.screens.s07chat.ChatScreen
import mursalin.companion.gobuddy.presentation.screens.s03dashboard.DashboardScreen
import mursalin.companion.gobuddy.presentation.screens.s04project_list.ProjectListScreen
import mursalin.companion.gobuddy.presentation.screens.s11_add_project.AddProjectScreen
import mursalin.companion.gobuddy.presentation.screens.s12_add_task.AddTaskScreen
import mursalin.companion.gobuddy.presentation.screens.s10settings.SettingsScreen
import mursalin.companion.gobuddy.presentation.screens.s01splash.SplashScreen
import mursalin.companion.gobuddy.presentation.screens.s05task_board.TaskBoardScreen
import mursalin.companion.gobuddy.presentation.viewmodel.AuthEvent
import mursalin.companion.gobuddy.presentation.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    if (authState.user != null) {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
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
            SignUpScreen(
                authViewModel = authViewModel,
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
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        // This composable was missing, which caused the crash.
        composable(Screen.AddProject.route) {
            AddProjectScreen(navController = navController)
        }
        composable(
            route = Screen.AddTask.route,
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) {
            AddTaskScreen(navController = navController)
        }
        composable(Screen.ProjectList.route) {
            ProjectListScreen(navController = navController)
        }
        composable(
            route = Screen.TaskBoard.route,
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) {
            TaskBoardScreen(navController = navController)
        }
        composable(Screen.Chat.route) {
            ChatScreen(navController = navController)
        }
        composable(Screen.Achievements.route) {
            AchievementsScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                onLogout = {
                    authViewModel.onEvent(AuthEvent.Logout)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        authViewModel.onEvent(AuthEvent.CheckSession)
    }
}
