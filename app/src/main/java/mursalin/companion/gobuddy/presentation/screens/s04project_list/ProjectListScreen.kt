package mursalin.companion.gobuddy.presentation.screens.s04project_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import mursalin.companion.gobuddy.presentation.components.LoadingAnimation
import mursalin.companion.gobuddy.presentation.components.ProjectCard
import mursalin.companion.gobuddy.presentation.navigation.Screen
import mursalin.companion.gobuddy.presentation.viewmodel.ProjectListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
    navController: NavController,
    viewModel: ProjectListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProjects()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Projects") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Corrected navigation call
                navController.navigate(Screen.AddProject.route)
            }) {
                Icon(Icons.Default.Add, "Add New Project")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading) {
                LoadingAnimation(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "An unknown error occurred.",
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else if (state.projects.isEmpty()) {
                Text(
                    text = "No projects found.\nTap the '+' button to add your first project!",
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.projects) { project ->
                        ProjectCard(
                            project = project,
                            onClick = {
                                // Navigate to task board for this project
                                navController.navigate(Screen.TaskBoard.route + "/${project.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}
