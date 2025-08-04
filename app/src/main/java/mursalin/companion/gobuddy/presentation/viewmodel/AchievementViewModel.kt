package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mursalin.companion.gobuddy.domain.model.Achievement
import mursalin.companion.gobuddy.domain.repository.AchievementRepository
import javax.inject.Inject

data class AchievementState(
    val achievements: List<Achievement> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AchievementState())
    val state = _state.asStateFlow()

    init {
        loadAchievements()
    }

    private fun loadAchievements() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            achievementRepository.getAchievements()
                .onSuccess { achievements ->
                    _state.update { it.copy(isLoading = false, achievements = achievements) }
                }
                .onFailure { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message) }
                }
        }
    }
}