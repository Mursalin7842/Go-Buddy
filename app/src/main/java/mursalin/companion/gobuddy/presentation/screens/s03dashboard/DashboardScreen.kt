// FILE: app/src/main/java/mursalin/companion/gobuddy/presentation/screens/dashboard/DashboardScreen.kt
package mursalin.companion.gobuddy.presentation.screens.s03dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mursalin.companion.gobuddy.domain.model.Project
import mursalin.companion.gobuddy.presentation.navigation.Screen
import mursalin.companion.gobuddy.presentation.theme.GoBuddyTheme
import mursalin.companion.gobuddy.presentation.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                navigationIcon = { IconButton(onClick = { /* Open drawer */ }) { Icon(Icons.Default.Menu, "Menu") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navigate to add project */ }) {
                Icon(Icons.Default.Add, "Add Project")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { WelcomeHeader("Mursalin") }
            item { TodaysFocusSection() }
            item {
                ProjectSummarySection(
                    projects = state.projects,
                    isLoading = state.isLoading,
                    onProjectClick = { projectId ->
                        navController.navigate(Screen.TaskBoard.createRoute(projectId))
                    },
                    onViewAllClick = {
                        navController.navigate(Screen.ProjectList.route)
                    }
                )
            }
        }
    }
}

@Composable
fun WelcomeHeader(name: String) {
    Column {
        Text("Welcome Back, $name!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Let's make today productive.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
    }
}

@Composable
fun TodaysFocusSection() {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Today's Focus", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Text("• Design the new login screen.")
            Text("• Fix the bug in the payment gateway.")
        }
    }
}

@Composable
fun ProjectSummarySection(
    projects: List<Project>,
    isLoading: Boolean,
    onProjectClick: (String) -> Unit,
    onViewAllClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Projects Overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(onClick = onViewAllClick) {
                    Text("View All")
                }
            }
            Spacer(Modifier.height(16.dp))
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                // Show a summary, e.g., the first 3 projects
                projects.take(3).forEach { project ->
                    ProjectCard(project = project, onClick = { onProjectClick(project.id) })
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(project.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(progress = { project.progress }, modifier = Modifier.fillMaxWidth().height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    GoBuddyTheme {
        DashboardScreen(rememberNavController())
    }
}
