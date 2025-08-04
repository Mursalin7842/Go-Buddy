package mursalin.companion.gobuddy.presentation.navigation

/*
 * Sealed class to define all the navigation routes in the app.
 * Using a sealed class provides type safety and allows for easy passing of arguments.
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object SignUp : Screen("signup_screen")
    object ForgotPassword : Screen("forgot_password_screen")
    object Dashboard : Screen("dashboard_screen")
    object ProjectList : Screen("project_list_screen")
    object TaskBoard : Screen("task_board_screen/{projectId}") {
        fun createRoute(projectId: String) = "task_board_screen/$projectId"
    }
    object TaskDetail : Screen("task_detail_screen/{taskId}") {
        fun createRoute(taskId: String) = "task_detail_screen/$taskId"
    }
    object Chat : Screen("chat_screen")
    object Achievements : Screen("achievements_screen")
    object Settings : Screen("settings_screen")
}