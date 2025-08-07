package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    val userId: String? = null
)

@HiltViewModel
class AddProjectViewModel @Inject constructor(
    private val addProjectUseCase: AddProjectUseCase,
    private val checkSessionUseCase: CheckSessionUseCase
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
        _state.update { it.copy(error = null) }
    }
}
