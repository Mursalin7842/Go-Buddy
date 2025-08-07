package mursalin.companion.gobuddy.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgotPassword : Screen("forgot_password")
    object Dashboard : Screen("dashboard")
    object ProjectList : Screen("project_list")
    object AddProject : Screen("add_project") // New Screen
    object TaskBoard : Screen("task_board")
    object TaskDetail : Screen("task_detail")
    object Chat : Screen("chat")
    object RemindersSetting : Screen("reminders_setting")
    object Achievements : Screen("achievements")
    object Settings : Screen("settings")
}
