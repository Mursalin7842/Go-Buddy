package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import mursalin.companion.gobuddy.domain.model.Priority
import mursalin.companion.gobuddy.domain.model.Task
import mursalin.companion.gobuddy.domain.model.TaskStatus
import java.util.Date
import javax.inject.Inject

data class TaskBoardState(
    val projectName: String = "",
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class TaskBoardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TaskBoardState())
    val state = _state.asStateFlow()

    init {
        val projectId = savedStateHandle.get<String>("projectId") ?: ""
        loadTasksForProject(projectId)
    }

    private fun loadTasksForProject(projectId: String) {
        _state.value = TaskBoardState(
            projectName = "Go Buddy App Development",
            isLoading = false,
            tasks = generateDummyTasks(projectId)
        )
    }

    private fun generateDummyTasks(projectId: String): List<Task> {
        return listOf(
            Task(id = "1", projectId = projectId, title = "Design Login Screen", description = "", dueDate = Date(), priority = Priority.HIGH, status = TaskStatus.TODO),
            Task(id = "2", projectId = projectId, title = "Develop Splash Screen Animation", description = "", dueDate = Date(), priority = Priority.HIGH, status = TaskStatus.DONE),
            Task(id = "3", projectId = projectId, title = "Set up Hilt for DI", description = "", dueDate = Date(), priority = Priority.MEDIUM, status = TaskStatus.DONE),
            Task(id = "4", projectId = projectId, title = "Implement Login API Call", description = "", dueDate = Date(), priority = Priority.MEDIUM, status = TaskStatus.DOING),
            Task(id = "5", projectId = projectId, title = "Create Task Board UI", description = "", dueDate = Date(), priority = Priority.HIGH, status = TaskStatus.DOING),
            Task(id = "6", projectId = projectId, title = "Refactor Database Schema", description = "", dueDate = Date(), priority = Priority.LOW, status = TaskStatus.STUCK),
            Task(id = "7", projectId = projectId, title = "Write Unit Tests for ViewModel", description = "", dueDate = Date(), priority = Priority.LOW, status = TaskStatus.TODO),
            Task(id = "8", projectId = projectId, title = "Fix Navigation Bug", description = "", dueDate = Date(), priority = Priority.HIGH, status = TaskStatus.STUCK)
        )
    }
}
