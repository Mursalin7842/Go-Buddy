package mursalin.companion.gobuddy.presentation.screens.s05task_board

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import mursalin.companion.gobuddy.domain.model.Task
import mursalin.companion.gobuddy.presentation.components.LoadingAnimation
import mursalin.companion.gobuddy.presentation.components.TaskCard
import mursalin.companion.gobuddy.presentation.navigation.Screen
import mursalin.companion.gobuddy.presentation.viewmodel.TaskboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBoardScreen(
    navController: NavController,
    viewModel: TaskboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Board") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.projectId?.let {
                    navController.navigate(Screen.AddTask.createRoute(it))
                }
            }) {
                Icon(Icons.Default.Add, "Add Task")
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
                    text = state.error!!,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TaskColumn("To Do", state.todoTasks, Modifier.weight(1f))
                    TaskColumn("Doing", state.doingTasks, Modifier.weight(1f))
                    TaskColumn("Done", state.doneTasks, Modifier.weight(1f))
                    TaskColumn("Stuck", state.stuckTasks, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TaskColumn(title: String, tasks: List<Task>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxHeight().padding(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            if (tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tasks")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tasks) { task ->
                        TaskCard(task = task, onClick = { /* TODO: Navigate to task details */ })
                    }
                }
            }
        }
    }
}
