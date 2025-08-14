package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mursalin.companion.gobuddy.domain.use_case.ai.AiProjectPlan
import mursalin.companion.gobuddy.domain.use_case.ai.GenerateProjectPlanUseCase
import mursalin.companion.gobuddy.domain.use_case.auth.CheckSessionUseCase
import mursalin.companion.gobuddy.domain.use_case.project.AddProjectUseCase
import java.util.*
import javax.inject.Inject

data class AddProjectState(
    val title: String = "",
    val description: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val titleError: String? = null,
    val isProjectAdded: Boolean = false,
    val userId: String? = null,
    // AI related state
    val aiIdea: String = "",
    val isGenerating: Boolean = false,
    val generatedPlan: AiProjectPlan? = null,
    val generationError: String? = null
)

@HiltViewModel
class AddProjectViewModel @Inject constructor(
    private val addProjectUseCase: AddProjectUseCase,
    private val checkSessionUseCase: CheckSessionUseCase,
    private val generateProjectPlanUseCase: GenerateProjectPlanUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddProjectState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            checkSessionUseCase().onSuccess { user ->
                _state.update { it.copy(userId = user.id) }
            }
        }
    }

    fun onTitleChanged(title: String) {
        _state.update { it.copy(title = title, titleError = null) }
    }

    fun onDescriptionChanged(description: String) {
        _state.update { it.copy(description = description) }
    }

    fun onStartDateChanged(date: Date) {
        _state.update { it.copy(startDate = date) }
    }

    fun onEndDateChanged(date: Date) {
        _state.update { it.copy(endDate = date) }
    }

    fun onAiIdeaChanged(idea: String) {
        _state.update { it.copy(aiIdea = idea) }
    }

    fun generateProjectPlan() {
        if (_state.value.aiIdea.isBlank()) {
            _state.update { it.copy(generationError = "Please enter an idea to generate a plan.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isGenerating = true, generationError = null) }
            generateProjectPlanUseCase(_state.value.aiIdea)
                .onSuccess { plan ->
                    _state.update {
                        it.copy(
                            isGenerating = false,
                            generatedPlan = plan,
                            title = plan.title,
                            description = plan.description
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isGenerating = false,
                            generationError = exception.message ?: "An unknown error occurred."
                        )
                    }
                }
        }
    }

    fun addProject() {
        viewModelScope.launch {
            if (_state.value.title.isBlank()) {
                _state.update { it.copy(titleError = "Title cannot be empty") }
                return@launch
            }
            if (_state.value.userId == null) {
                _state.update { it.copy(error = "User not logged in") }
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            addProjectUseCase(
                userId = _state.value.userId!!,
                title = _state.value.title,
                description = _state.value.description,
                startDate = _state.value.startDate,
                endDate = _state.value.endDate
            ).onSuccess {
                _state.update { it.copy(isLoading = false, isProjectAdded = true) }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null, generationError = null) }
    }
}
