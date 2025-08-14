package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mursalin.companion.gobuddy.domain.model.Project
import mursalin.companion.gobuddy.domain.use_case.project.GetProjectsUseCase
import javax.inject.Inject

data class DashboardState(
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        loadProjects()
    }

    fun loadProjects() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getProjectsUseCase()
                .onSuccess { projects ->
                    _state.update { it.copy(isLoading = false, projects = projects) }
                }
                .onFailure { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message) }
                }
        }
    }
}
