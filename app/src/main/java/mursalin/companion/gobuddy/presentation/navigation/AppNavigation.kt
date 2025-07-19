package mursalin.companion.gobuddy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import mursalin.companion.gobuddy.presentation.screens.`02_auth`.ForgetPasswordScreen
import mursalin.companion.gobuddy.presentation.screens.`02_auth`.LoginScreen
import mursalin.companion.gobuddy.presentation.screens.`02_auth`.SignUpScreen
import mursalin.companion.gobuddy.presentation.screens.`03_dashboard`.DashboardScreen
import mursalin.companion.gobuddy.presentation.screens.`01_splash`.SplashScreen
import mursalin.companion.gobuddy.presentation.viewmodel.AuthViewModel

object Route {
    const val SPLASH = "splash"
    const val AUTH = "auth_graph"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"
    const val FORGOT_PASSWORD = "forgot_password"
    const val DASHBOARD = "dashboard_graph"
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = Route.SPLASH) {
        composable(Route.SPLASH) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Route.AUTH) {
                    popUpTo(Route.SPLASH) { inclusive = true }
                }
            })
        }

        navigation(startDestination = Route.LOGIN, route = Route.AUTH) {
            composable(Route.LOGIN) {
                LoginScreen(
                    state = authState,
                    onEvent = authViewModel::onEvent,
                    onNavigateToSignUp = { navController.navigate(Route.SIGN_UP) },
                    onNavigateToForgotPassword = { navController.navigate(Route.FORGOT_PASSWORD) },
                    onLoginSuccess = {
                        navController.navigate(Route.DASHBOARD) {
                            popUpTo(Route.AUTH) { inclusive = true }
                        }
                    }
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

        navigation(startDestination = "dashboard_main", route = Route.DASHBOARD) {
            composable("dashboard_main") {
                DashboardScreen()
            }
        }
    }
}