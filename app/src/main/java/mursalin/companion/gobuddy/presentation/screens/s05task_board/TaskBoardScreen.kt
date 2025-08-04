package mursalin.companion.gobuddy.presentation.screens.s05task_board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import mursalin.companion.gobuddy.domain.model.Priority
import mursalin.companion.gobuddy.domain.model.Task
import mursalin.companion.gobuddy.domain.model.TaskStatus
import mursalin.companion.gobuddy.presentation.theme.GoBuddyTheme
import mursalin.companion.gobuddy.presentation.viewmodel.TaskBoardViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBoardScreen(
    navController: NavController,
    viewModel: TaskBoardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.projectName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Show Add Task Dialog */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        // The main content of the screen is a horizontally scrolling row of columns
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Create a column for each task status
            items(TaskStatus.values()) { status ->
                TaskColumn(
                    status = status,
                    tasks = state.tasks.filter { it.status == status }
                )
            }
        }
    }
}

/**
 * A composable that represents a single column in the Kanban board (e.g., "To Do").
 */
@Composable
fun TaskColumn(status: TaskStatus, tasks: List<Task>) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        // Column Header
        Text(
            text = status.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        // List of tasks in the column
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tasks) { task ->
                TaskCard(task = task)
            }
        }
    }
}

/**
 * A reusable composable to display a single task card.
 */
@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Display a priority tag
            PriorityTag(priority = task.priority)
        }
    }
}

/**
 * A small, styled tag to indicate task priority.
 */
@Composable
fun PriorityTag(priority: Priority) {
    val (text, color) = when (priority) {
        Priority.HIGH -> "High" to MaterialTheme.colorScheme.errorContainer
        Priority.MEDIUM -> "Medium" to MaterialTheme.colorScheme.tertiaryContainer
        Priority.LOW -> "Low" to MaterialTheme.colorScheme.secondaryContainer
    }
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .background(color, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TaskBoardScreenPreview() {
    GoBuddyTheme {
        // For the preview, we can't use hiltViewModel(), so we'll just show the UI structure.
        // A full preview would require creating a dummy ViewModel.
        Text("Task Board Preview")
    }
}