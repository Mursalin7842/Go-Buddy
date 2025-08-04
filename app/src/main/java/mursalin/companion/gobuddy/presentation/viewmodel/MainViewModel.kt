package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import mursalin.companion.gobuddy.domain.model.Theme
import mursalin.companion.gobuddy.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * A ViewModel for the MainActivity to observe app-wide settings like theme.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {

    // Expose the theme as a StateFlow so the MainActivity can observe it.
    val theme = settingsRepository.theme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Theme.SYSTEM
    )
}