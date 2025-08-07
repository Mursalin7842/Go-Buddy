package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mursalin.companion.gobuddy.domain.model.Task
import mursalin.companion.gobuddy.domain.use_case.task.GetTasksForProjectUseCase
import javax.inject.Inject

data class TaskboardState(
    val projectId: String? = null,
    val todoTasks: List<Task> = emptyList(),
    val doingTasks: List<Task> = emptyList(),
    val doneTasks: List<Task> = emptyList(),
    val stuckTasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TaskboardViewModel @Inject constructor(
    private val getTasksForProjectUseCase: GetTasksForProjectUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TaskboardState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("projectId")?.let { projectId ->
            _state.update { it.copy(projectId = projectId) }
            loadTasks(projectId)
        }
    }

    private fun loadTasks(projectId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getTasksForProjectUseCase(projectId)
                .onSuccess { tasks ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            todoTasks = tasks.filter { t -> t.status.equals("TODO", ignoreCase = true) },
                            doingTasks = tasks.filter { t -> t.status.equals("DOING", ignoreCase = true) },
                            doneTasks = tasks.filter { t -> t.status.equals("DONE", ignoreCase = true) },
                            stuckTasks = tasks.filter { t -> t.status.equals("STUCK", ignoreCase = true) }
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message) }
                }
        }
    }
}
